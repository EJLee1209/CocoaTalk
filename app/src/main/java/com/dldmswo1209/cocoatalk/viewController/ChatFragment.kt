package com.dldmswo1209.cocoatalk.viewController

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.adapter.RoomListAdapter
import com.dldmswo1209.cocoatalk.databinding.FragmentChatBinding
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel

class ChatFragment : Fragment() {
    val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentChatBinding
    private lateinit var roomAdapter: RoomListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = (activity as MainActivity).user
        mainViewModel.getAllMyChatRoom(user.id)
        roomAdapter = RoomListAdapter(user) { room ->
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("user", user)
            if(user.id != room.from_id){
                intent.putExtra("friend_id", room.from_id)
            }else{
                intent.putExtra("friend_id", room.to_id)
            }
            intent.putExtra("room", room)
            startActivity(intent)
        }
        binding.chatRoomRecyclerview.adapter = roomAdapter

        mainViewModel.myChatRooms.observe(viewLifecycleOwner, Observer {
            roomAdapter.submitList(it)
            roomAdapter.notifyDataSetChanged()
        })

    }
}