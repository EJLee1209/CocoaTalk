package com.dldmswo1209.cocoatalk.viewController

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dldmswo1209.cocoatalk.adapter.TalkListAdapter
import com.dldmswo1209.cocoatalk.databinding.ActivityChatRoomBinding
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.model.User
import java.time.LocalDate
import java.time.LocalDateTime

class ChatRoomActivity() : AppCompatActivity() {
    private val binding by lazy{
        ActivityChatRoomBinding.inflate(layoutInflater)
    }
    private lateinit var chatAdapter : TalkListAdapter
    private val msgList = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val user = intent.getSerializableExtra("user") as User
        val friend = intent.getSerializableExtra("friend") as User

        chatAdapter = TalkListAdapter(user, friend)
        binding.chatRecyclerView.adapter = chatAdapter

        binding.chatRoomNameTextView.text = friend.name

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.sendButton.setOnClickListener {
            val message = binding.chatEditText.text.toString()
            if(message == "") return@setOnClickListener
            val msg = Message(1,user.uid, message, LocalDateTime.now().toString())
            msgList.add(msg)
            chatAdapter.submitList(msgList)
            chatAdapter.notifyDataSetChanged()

            binding.chatEditText.text.clear()
        }

    }
}