package com.dldmswo1209.cocoatalk.bottomSheetDialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.databinding.FragmentAddFriendBottomBinding
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

// 친구추가 버튼 클릭시 아래서 튀어나오는 BottomSheetDialog
class AddFriendBottomFragment() : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddFriendBottomBinding
    private val mainViewModel : MainViewModel by activityViewModels()

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
        binding = FragmentAddFriendBottomBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 로그인한 유저의 id
        val user_id = (activity as MainActivity).user.id

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }
        binding.addFriendButton.setOnClickListener {
            val friend_id = binding.friendIdEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    mainViewModel.addFriend(user_id, friend_id)
                    delay(100)
                    Log.d("testt", "add friend")
                }.await()
                Log.d("testt", "getAllMyFriend")
                mainViewModel.getAllMyFriend(user_id)
            }
            dialog?.dismiss()
        }





    }
}