package com.dldmswo1209.cocoatalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.databinding.MyChatItemBinding
import com.dldmswo1209.cocoatalk.databinding.OtherChatItemBinding
import com.dldmswo1209.cocoatalk.model.Message
import com.dldmswo1209.cocoatalk.model.User

class TalkListAdapter(
    val sender: User,
    val receiver: User
): ListAdapter<Message, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return currentList[position].sender_uid
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class MyViewHolder(val binding: MyChatItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatTextView.text = message.text
            binding.timeTextView.text = message.time
        }
    }

    inner class OtherViewHolder(val binding: OtherChatItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.chatTextView.text = message.text
            binding.timeTextView.text = message.time
            if(receiver.image != "" && receiver.image != null){
                Glide.with(binding.root)
                    .load("https://18f4-119-67-181-215.jp.ngrok.io/get/profileImage?imageName=${receiver.image}")
                    .circleCrop()
                    .into(binding.profileImageView)
            }
            binding.nameTextView.text = receiver.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            sender.uid ->{
                MyViewHolder(MyChatItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
            }else->{
                OtherViewHolder(OtherChatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(currentList[position].sender_uid){
            sender.uid->{
                (holder as MyViewHolder).bind(currentList[position])
                holder.setIsRecyclable(false)
            }else->{
                (holder as OtherViewHolder).bind(currentList[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<Message>(){
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }
}