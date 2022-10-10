package com.dldmswo1209.cocoatalk.bottomSheetDialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.adapter.RoomListAdapter
import com.dldmswo1209.cocoatalk.databinding.FragmentFriendProfileBottomBinding
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

// 친구 프로필 클릭시 아래에서 튀어나오는 BottomSheetDialog
class FriendProfileBottomFragment(val friend: User) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFriendProfileBottomBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state =
                BottomSheetBehavior.STATE_EXPANDED // bottomSheetDialog 가 완전히 펼쳐진 상태로 보여지게 됨
            behavior.skipCollapsed = true // 드래그하면 dialog 가 바로 닫힘
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFriendProfileBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        val user = (activity as MainActivity).user
        var rooms = listOf<ChatRoom>()
        viewModel.getAllMyChatRoom(user.id) // 현재 유저의 모든 채팅방을 가져옴
        viewModel.myChatRooms.observe(viewLifecycleOwner, Observer {
            rooms = it //rooms 에 저장
        })

        viewModel.room.observe(this, Observer {
            // viewModel 의 room을 관찰
            (activity as MainActivity).openChatRoomDrawer(it) // 채팅방 열기(NavigationDrawer)
        })

        binding.chatImageView.setOnClickListener {
            dialog?.dismiss()
            var room: ChatRoom? = null
            // 모든 채팅방 리스트에서 채팅방을 찾음
            rooms.forEach {
                if ((user.id == it.from_id && friend.id == it.to_id) ||
                    (user.id == it.to_id && friend.id == it.from_id)
                ) {
                    room = it // 찾아서 저장하고
                }
            }
            if(room != null){ // room 을 찾았으면
                (activity as MainActivity).openChatRoomDrawer(room!!) // 드로어를 여는 메서드에 room 을 전달하고 호출
                Log.d("testt", "call openChatRoomDrawer(room!!)")
            }else{ // room 을 못찾았으면 (채팅방이 존재하지 않음)
                lifecycleScope.launch {
                    async {
                        viewModel.createChatRoom(user.id, friend.id,"","") // 채팅방 생성
                        Log.d("testt", "call createChatRoom")
                        delay(200)
                    }.await()
                    async {
                        viewModel.getRoom(user.id, friend.id) // 생성한 채팅방 가져오기 (room 관찰자를 위에서 구현해놨음)
                        Log.d("testt", "call getRoom")
                    }
                }
            }
        }
    }
    fun initView(){
        // 뷰 초기화
        binding.nameTextView.text = friend.name
        if (friend.image == null || friend.image == "") {
            Glide.with(binding.root)
                .load(R.drawable.profile_default)
                .circleCrop()
                .into(binding.profileImageView)
        } else {
            Glide.with(binding.root)
                .load("https://18f4-119-67-181-215.jp.ngrok.io/get/profileImage?imageName=${friend.image}")
                .circleCrop()
                .into(binding.profileImageView)
        }
        binding.stateMsgTextView.text = friend.state_msg
    }
}
