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
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.adapter.RoomListAdapter
import com.dldmswo1209.cocoatalk.databinding.FragmentFriendProfileBottomBinding
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewController.ChatRoomActivity
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

        binding.nameTextView.text = friend.name
        if (friend.image == null || friend.image == "") {
            Glide.with(view)
                .load(R.drawable.profile_default)
                .circleCrop()
                .into(binding.profileImageView)
        } else {
            Glide.with(view)
                .load(friend.image?.toUri())
                .circleCrop()
                .into(binding.profileImageView)
        }
        binding.stateMsgTextView.text = friend.state_msg

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        val user = (activity as MainActivity).user
        var rooms = listOf<ChatRoom>()
        viewModel.getAllMyChatRoom(user.id)
        viewModel.myChatRooms.observe(viewLifecycleOwner, Observer {
            rooms = it
        })


        binding.chatImageView.setOnClickListener {
            // 채팅방으로 이동
            lateinit var room: ChatRoom
            rooms.forEach {
                if ((user.id == it.from_id && friend.id == it.to_id) ||
                    (user.id == it.to_id && friend.id == it.from_id)
                ) {
                    room = it

                }
            }
            Log.d("testt", room.toString())
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("room", room)
            intent.putExtra("user", user)
            intent.putExtra("friend_id", friend.id)
            startActivity(intent)

        }


    }
}
