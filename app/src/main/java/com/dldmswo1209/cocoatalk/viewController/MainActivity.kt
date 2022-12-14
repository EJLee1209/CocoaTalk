package com.dldmswo1209.cocoatalk.viewController

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.adapter.TalkListAdapter
import com.dldmswo1209.cocoatalk.bottomSheetDialog.AddFriendBottomFragment
import com.dldmswo1209.cocoatalk.databinding.ActivityMainBinding
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.ReadMessage
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var user: User
    lateinit var adapter: TalkListAdapter
    var room: ChatRoom? = null
    var friend: User? = null
    private lateinit var mSocket: Socket
    private val msgList = mutableListOf<Message>()
    private val friendFragment = FriendFragment()
    private val chatFragment = ChatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        user = intent.getSerializableExtra("user") as User

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("testt", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("testt", token)

            mainViewModel.registerToken(user.uid, token)
        })

        initView()
        clickEvent()

        mainViewModel.findPerson.observe(this, Observer {
            friend = it
            adapter = TalkListAdapter(user, it)
            binding.chatRecyclerView.adapter = adapter
            mainViewModel.getMessage(room!!.id)
        })
        mainViewModel.messageList.observe(this, Observer {
            it.forEach { msg->
                msgList.add(msg)
            }
            adapter.submitList(msgList)
            adapter.notifyDataSetChanged()
            binding.chatRecyclerView.scrollToPosition(msgList.size-1)
            connectSocket(room!!)
        })

        binding.sendButton.setOnClickListener {
            val message = binding.chatEditText.text.toString()
            if(message == "") return@setOnClickListener
            sendMessage()
        }

    }
    private fun initView(){
        // ????????? ??????
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // ??? ?????????
        replaceFragment(friendFragment)

        // ???????????? ??????????????? ????????? ????????? ??????
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    private fun clickEvent(){

        // ?????? ??????????????? ??? ?????? ?????????
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.friend ->{
                    replaceFragment(friendFragment)
                    binding.toolbarTextView.text = "??????"
                }

                R.id.chat -> {
                    replaceFragment(chatFragment)
                    binding.toolbarTextView.text = "??????"
                }
            }
            true
        }

        // ???????????? ?????? ?????????
        binding.addFriendButton.setOnClickListener {
            val bottomSheet = AddFriendBottomFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    }
    // ????????? ????????? ????????? ???????????? ????????? ???????????? Navigation Drawer ??? ??????
    fun openChatRoomDrawer(room: ChatRoom){
        // User ????????? ???????????? ?????? ????????????(?????? ???????????? User ??????)
        this.room = room
        if(user.id != room.from_id){
            mainViewModel.findUser(room.from_id)
            binding.chatRoomNameTextView.text = room.from_name
        }else{
            mainViewModel.findUser(room.to_id)
            binding.chatRoomNameTextView.text = room.to_name
        }

        // ????????? ??????
        binding.drawerLayout.openDrawer(GravityCompat.END)
        binding.backButton.setOnClickListener {
            closeChatRoomDrawer()
        }


    }
    fun closeChatRoomDrawer(){
        binding.drawerLayout.closeDrawers()
        Log.d("testt", mSocket.connected().toString())
        msgList.clear()
        mSocket.disconnect()
        mSocket.close()
    }

    // ????????? ??????????????? ???????????? ?????????
    fun connectSocket(room: ChatRoom){
        try{
            mSocket = IO.socket("https://18f4-119-67-181-215.jp.ngrok.io")
            mSocket.connect() // ?????? ??????

        }catch (e: URISyntaxException){
            Log.d("testt", e.toString())
        }catch (e: Exception){
            Log.d("testt", e.toString())
        }

        // socket.on("????????? ??????", ????????? ?????????)
        // socket.on ??? ?????? ????????? ??????
        // ??????????????? emit("?????????", ?????????) ??? ?????? ??????

        mSocket.on(Socket.EVENT_CONNECT, onConnect)

        // connect user ?????? ???????????? ?????? ?????? ????????? ???????????? onNewUser ???????????? ????????? ??????
        mSocket.on("connect user", onNewUser)
        mSocket.on("chat message", onNewMessage)


        // ????????? ????????? ???????????? ???????????? ??????
        val userData = JSONObject() // Json ?????? ??????
        try{
            // Json ??? ????????? put
            userData.put("username", "${user.uid} Connected")
            // ????????? ????????? id ??????
            // ??????????????? ????????? id ??? ???????????? ?????? ????????? ???????????? join ?????????.
            // ???????????? ??????????????? ???????????? ????????? ????????????.
            userData.put("roomNumber", room.id)
            Log.e("username", "${user.uid} Connected")

            // ????????? ??????
            mSocket.emit("connect user", userData) // ????????? ???????????? ????????? ??????

            // ???????????? ???????????? ??????(socket.on)?????? ????????? ???????????? join ?????????.
        } catch (e: JSONException){
            e.printStackTrace()
        } catch (e: EOFException){
            Log.d("testt", e.toString())
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

                val msg = Message(
                    room!!.id,
                    sender_id,
                    receiver_id,
                    text,
                    time,
                    ReadMessage.NOTREAD)
                mainViewModel.saveMessage(msg) // ??????????????????????????? ????????? ??????
                mainViewModel.updateRoom(room!!.id, text, time)
                mainViewModel.getAllMyChatRoom(user.id)

                msgList.add(msg)
                adapter.submitList(msgList)
                adapter.notifyDataSetChanged()
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
            jsonObject.put("receiver_id", friend!!.uid)
            jsonObject.put("text", text)
            jsonObject.put("time", time)
            jsonObject.put("roomNumber", room!!.id)
        }catch (e: JSONException){
            e.printStackTrace()
        }
        if(friend!!.token != null) {
            mainViewModel.sendPushMessage(friend!!.token!!, user.name, text)
        }

        mSocket.emit("chat message", jsonObject)

    }


}





























