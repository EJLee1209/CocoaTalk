package com.dldmswo1209.cocoatalk.viewController

import android.content.Context
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
import com.dldmswo1209.cocoatalk.entity.MessageEntity
import com.dldmswo1209.cocoatalk.model.ChatRoom
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
    private val msgList = mutableListOf<MessageEntity>()
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
        // 액션바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 뷰 초기화
        replaceFragment(friendFragment)

        // 드로어를 슬라이드로 여닫는 기능을 잠금
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    private fun clickEvent(){

        // 하단 네비게이션 뷰 클릭 이벤트
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.friend ->{
                    replaceFragment(friendFragment)
                    binding.toolbarTextView.text = "친구"
                }

                R.id.chat -> {
                    replaceFragment(chatFragment)
                    binding.toolbarTextView.text = "채팅"
                }
            }
            true
        }

        // 친구추가 버튼 클릭시
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
    // 카톡과 유사한 환경을 구현하기 위해서 채팅방을 Navigation Drawer 로 구현
    fun openChatRoomDrawer(room: ChatRoom){
        // User 객체를 가져오기 위한 작업이다(대화 상대방의 User 객체)
        this.room = room
        if(user.id != room.from_id){
            mainViewModel.findUser(room.from_id)
            binding.chatRoomNameTextView.text = room.from_name
        }else{
            mainViewModel.findUser(room.to_id)
            binding.chatRoomNameTextView.text = room.to_name
        }

        // 드로어 오픈
        binding.drawerLayout.openDrawer(GravityCompat.END)
        binding.backButton.setOnClickListener {
            closeChatRoomDrawer()
        }

    }
    fun closeChatRoomDrawer(){
        binding.drawerLayout.closeDrawers()
        msgList.clear()
        mSocket.disconnect()
        mSocket.close()
    }

    // 서버에 소켓연결을 시도하는 메서드
    fun connectSocket(room: ChatRoom){
        try{
            Log.d("testt", "Connecting...")
            mSocket = IO.socket("https://e2fc-119-67-181-215.jp.ngrok.io")
            mSocket.connect() // 소켓 연결
        }catch (e: URISyntaxException){
            Log.d("testt", e.toString())
        }catch (e: Exception){
            Log.d("testt", e.toString())
        }

        // socket.on("이벤트 이름", 이벤트 리스너)
        // socket.on 을 통해 이벤트 수신
        // 서버에서는 emit("이벤트", 데이터) 를 통해 송신

        mSocket.on(Socket.EVENT_CONNECT, onConnect)

        // connect user 라는 이벤트를 수신 대기 하다가 수신하면 onNewUser 리스너가 작업을 처리
        mSocket.on("connect user", onNewUser)
        mSocket.on("chat message", onNewMessage)


        // 서버에 전달할 데이터를 가공하는 과정
        val userData = JSONObject() // Json 객체 생성
        try{
            // Json 에 데이터 put
            userData.put("username", "${user.uid} Connected")
            // 서버에 채팅방 id 전달
            // 서버에서는 채팅방 id 를 사용해서 해당 유저를 채팅방에 join 시킨다.
            // 채팅방에 들어와있는 유저끼리 대화를 하게된다.
            userData.put("roomNumber", room.id)
            Log.e("username", "${user.uid} Connected")

            // 서버로 전달
            mSocket.emit("connect user", userData) // 가공한 데이터를 서버로 전달
            // 서버에서 데이터를 수신(socket.on)하고 유저를 채팅방에 join 시킨다.
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

                val msg = MessageEntity(0,room!!.id, sender_id, receiver_id, text, time)
                mainViewModel.saveMessage(msg) // 로컬데이터베이스에 메시지 저장
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





























