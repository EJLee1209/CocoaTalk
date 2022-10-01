package com.dldmswo1209.cocoatalk.viewController

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.adapter.TalkListAdapter
import com.dldmswo1209.cocoatalk.databinding.ActivityChatRoomBinding
import com.dldmswo1209.cocoatalk.entity.MessageEntity
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewModel.ChatRoomViewModel
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.EOFException
import java.net.URISyntaxException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoomActivity() : AppCompatActivity() {
    private val binding by lazy{
        ActivityChatRoomBinding.inflate(layoutInflater)
    }
    private lateinit var chatAdapter : TalkListAdapter
    private val msgList = mutableListOf<MessageEntity>()
    private lateinit var viewModel: ChatRoomViewModel
    private lateinit var mSocket: Socket
    private var room: ChatRoom? = null
    lateinit var user: User
    lateinit var friend: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ChatRoomViewModel::class.java]

        user = intent.getSerializableExtra("user") as User
        val friend_id = intent.getStringExtra("friend_id").toString()

        if(intent.getSerializableExtra("room") == null){
            viewModel.createChatRoom(user.id, friend_id, "", "")
        }else {
            room = intent.getSerializableExtra("room") as ChatRoom
        }

        viewModel.isCreated.observe(this, Observer {
            if(it){
                Toast.makeText(this, "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "이미 채팅방이 존재합니다.", Toast.LENGTH_SHORT).show()
            }
        })


        viewModel.findUser(friend_id)

        viewModel.findPerson.observe(this, Observer {
            friend = it

            chatAdapter = TalkListAdapter(user, friend)
            binding.chatRecyclerView.adapter = chatAdapter

            binding.chatRoomNameTextView.text = friend.name

            viewModel.getMessage(room!!.id)

        })

        viewModel.messageList.observe(this, Observer {
            it.forEach { msg->
                msgList.add(msg)
            }
            chatAdapter.submitList(msgList)
            chatAdapter.notifyDataSetChanged()
            binding.chatRecyclerView.scrollToPosition(msgList.size-1)
        })

        try{
            Log.d("testt", "Connecting...")
            mSocket = IO.socket("http://192.168.123.100:8080")
            mSocket.connect()


        }catch (e: URISyntaxException){
            Log.d("testt", e.toString())
        }catch (e: Exception){
            Log.d("testt", e.toString())
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)

        // 채팅방 정보 가져와야함
        // 일단 chatFragment 에서 채팅방 클릭으로 채팅방에 진입했다고 가정하고 진행
        mSocket.on("connect user", onNewUser)
        mSocket.on("chat message", onNewMessage)


        val userData = JSONObject()
        try{
            userData.put("username", "${user.uid} Connected")
            userData.put("roomNumber", room?.id)
            Log.e("username", "${user.uid} Connected")

            // 서버로 전달
            mSocket.emit("connect user", userData)
        } catch (e: JSONException){
            e.printStackTrace()
        } catch (e: EOFException){
            Log.d("testt", e.toString())
        }


        binding.backButton.setOnClickListener {
            finish()
        }


        binding.sendButton.setOnClickListener {
            val message = binding.chatEditText.text.toString()
            if(message == "") return@setOnClickListener
            sendMessage()
        }




    }
    internal var onConnect = Emitter.Listener {
        mSocket.emit("connectReceive", "OK")
    }
    internal var onNewUser = Emitter.Listener { args->
        runOnUiThread {
            val length = args.size

            if(length == 0){
                return@runOnUiThread
            }

            var username = args[0].toString()
            try{
                val `object` = JSONObject(username)
                username = `object`.getString("username")
            }catch (e: JSONException){
                e.printStackTrace()
            }

        }
    }
    internal var onNewMessage = Emitter.Listener { args->

        runOnUiThread {
            val data = args[0] as JSONObject
            val sender_id: Int
            val receiver_id: Int
            val text: String
            val time: String

            try{
                Log.e("testt",data.toString())

                sender_id = data.getInt("sender_id")
                receiver_id = data.getInt("receiver_id")
                text = data.getString("text")
                time = data.getString("time")

                val msg = MessageEntity(0,room!!.id, sender_id, receiver_id, text, time)
                viewModel.saveMessage(msg)
                msgList.add(msg)
                chatAdapter.submitList(msgList)
                chatAdapter.notifyDataSetChanged()
                binding.chatRecyclerView.scrollToPosition(msgList.size-1)

            }catch (e: Exception){
                return@runOnUiThread
            }
        }
    }
    private fun sendMessage(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val time = current.format(formatter)

        val text = binding.chatEditText.text.toString()
        binding.chatEditText.text.clear()

        val jsonObject = JSONObject()
        try{
            jsonObject.put("sender_id", user.uid)
            jsonObject.put("receiver_id", friend.uid)
            jsonObject.put("text", text)
            jsonObject.put("time", time)
            jsonObject.put("roomNumber", room?.id)
        }catch (e: JSONException){
            e.printStackTrace()
        }

        mSocket.emit("chat message", jsonObject)

        // 메세지 DB 저장 부분


    }

    override fun onDestroy() {
        mSocket.disconnect()
        mSocket.close()
        super.onDestroy()
    }
}























