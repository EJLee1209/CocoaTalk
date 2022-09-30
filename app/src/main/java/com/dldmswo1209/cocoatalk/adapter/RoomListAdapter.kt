package com.dldmswo1209.cocoatalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.databinding.ChatRoomItemBinding
import com.dldmswo1209.cocoatalk.model.ChatRoom
import com.dldmswo1209.cocoatalk.model.User

class RoomListAdapter(val user: User): ListAdapter<ChatRoom, RoomListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: ChatRoomItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(chatRoom: ChatRoom){
            binding.subjectTextView.text = chatRoom.subject
            binding.timeTextView.text= chatRoom.time
            if(chatRoom.from_id == user.id){
                binding.roomNameTextView.text = chatRoom.to_name
                if(chatRoom.to_image != "" && chatRoom.to_image != null) {
                    Glide.with(binding.root)
                        .load(chatRoom.to_image.toUri())
                        .circleCrop()
                        .into(binding.imageView)
                }
            }else{
                binding.roomNameTextView.text = chatRoom.from_name
                if(chatRoom.from_image != "" && chatRoom.from_image != null) {
                    Glide.with(binding.root)
                        .load(chatRoom.from_image.toUri())
                        .circleCrop()
                        .into(binding.imageView)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChatRoomItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object:  DiffUtil.ItemCallback<ChatRoom>(){
            override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
                return oldItem == newItem
            }

        }
    }
}