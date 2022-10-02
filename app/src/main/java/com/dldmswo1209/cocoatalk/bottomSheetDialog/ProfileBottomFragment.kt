package com.dldmswo1209.cocoatalk.bottomSheetDialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.databinding.FragmentProfileBottomBinding
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

// 내 프로필을 누르면 아래서 튀어나오는 BottomSheetDialog
class ProfileBottomFragment(val user: User) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProfileBottomBinding
    private val mainViewModel : MainViewModel by activityViewModels() // 메인 액티비티와 뷰모델 공유
    private var editMode = false // 프로필 편집을 위한 상태인지 아닌지 구분하기 위한 플래그
    private var imageUri: Uri? = null

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            imageUri = result.data?.data

            imageUri.let {
                Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(binding.profileImageView)
            }
        }
    }

    companion object{
        const val REQ_GALLERY = 1
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // bottomSheetDialog 가 완전히 펼쳐진 상태로 보여지게 됨
            behavior.skipCollapsed = true // 드래그하면 dialog 가 바로 닫힘
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(user)
        clickEvent()

        mainViewModel.userInfo.observe(this, Observer {
            // 프로필 수정시 유저정보 갱신이 필요하므로 userInfo 에 관찰자를 붙임
            initView(it)
        })


    }
    private fun initView(user: User){
        // 뷰 초기화
        binding.nameEditText.setText(user.name)
        if(user.image == "" || user.image == null){
            Glide.with(requireContext())
                .load(R.drawable.profile_default)
                .circleCrop()
                .into(binding.profileImageView)
        }else{
            Glide.with(requireContext())
                .load(user.image?.toUri())
                .circleCrop()
                .into(binding.profileImageView)
        }
        binding.stateMsgTextView.text = user.state_msg
        binding.StateMsgEditText.setText(user.state_msg)
    }

    private fun clickEvent(){
        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        // 프로필 편집 버튼 클릭
        binding.editProfileImageView.setOnClickListener {
            binding.editCompleteButton.isVisible = true
            binding.cameraIcon.isVisible = true
            binding.StateMsgEditText.isVisible = true
            binding.stateMsgTextView.isVisible = false
            binding.nameEditText.isEnabled = true
            binding.editNameImageView.isVisible = true
            editMode = true // 수정모드
        }

        // 프로필 편집 완료버튼 클릭
        binding.editCompleteButton.setOnClickListener {
            binding.editCompleteButton.isVisible = false
            binding.cameraIcon.isVisible = false
            binding.StateMsgEditText.isVisible = false
            binding.stateMsgTextView.isVisible = true
            binding.nameEditText.isEnabled = false
            binding.editNameImageView.isVisible = false

            editMode = false // 일반모드

            val stateMsg = binding.StateMsgEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            var image = ""
            if(imageUri != null) image = imageUri.toString()

            CoroutineScope(Dispatchers.IO).launch {
                async {
                    mainViewModel.profileUpdate(user.uid, name, image, stateMsg)
                    delay(10)
                }.await()
                async {
                    mainViewModel.getUserInfo(user.id, user.password)
                }
            }
        }

        binding.profileImageLayout.setOnClickListener {
            if(!editMode) return@setOnClickListener

            Log.d("testt", "yes")
            selectGallery()
        }
    }

    private fun selectGallery(){
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if(writePermission == PackageManager.PERMISSION_DENIED ||
                readPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                (activity as MainActivity),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQ_GALLERY
            )
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )

            imageResult.launch(intent)
        }



    }


}