package com.dldmswo1209.cocoatalk.viewController

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.adapter.FriendListAdapter
import com.dldmswo1209.cocoatalk.bottomSheetDialog.FriendProfileBottomFragment
import com.dldmswo1209.cocoatalk.bottomSheetDialog.ProfileBottomFragment
import com.dldmswo1209.cocoatalk.databinding.FragmentFriendBinding
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel

// 친구목록 Fragment
class FriendFragment : Fragment() {
    private lateinit var binding: FragmentFriendBinding
    private val mainViewModel : MainViewModel by activityViewModels() // 메인 액티비티와 뷰모델 공유
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 로그인한 User 를 가져옴
        user = (activity as MainActivity).user

        initView()
        initRecyclerView()
        initClickListener()

        mainViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            binding.stateMassageTextView.text = it.state_msg
            binding.nameTextView.text = it.name
            if(it.image == "" || it.image == null){
                Glide.with(requireContext())
                    .load(R.drawable.profile_default)
                    .circleCrop()
                    .into(binding.profileImageView)
            }else{
                Glide.with(requireContext())
                    .load("https://18f4-119-67-181-215.jp.ngrok.io/get/profileImage?imageName=${it.image}")
                    .circleCrop()
                    .into(binding.profileImageView)
            }
        })

    }

    fun initView(){
        mainViewModel.getUserInfo(user.id, user.password)
        binding.nameTextView.text =  user.name
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
        binding.stateMassageTextView.text = user.state_msg
    }

    fun initRecyclerView(){
        // 어답터 생성
        val adapter = FriendListAdapter { user -> // 리사이클러뷰 아이템 클릭시 클릭한 아이템의 User 객체를 넘겨줌
            val bottomSheet = FriendProfileBottomFragment(user) // user 의 프로필을 보여주는 bottomSheetDialog 생성
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.friendListRecyclerView.adapter = adapter

        mainViewModel.getAllMyFriend(user.id) // 현재 로그인한 유저의 모든 친구를 불러옴

        // friendList 관찰자
        mainViewModel.friendList.observe(viewLifecycleOwner, Observer {
            // 친구목록 업데이트
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })
    }

    fun initClickListener(){
        binding.myProfileLayout.setOnClickListener {
            val bottomSheet = ProfileBottomFragment(user) // user 의 프로필을 보여주는 bottomSheetDialog 생성
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }
}