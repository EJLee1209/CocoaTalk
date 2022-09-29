package com.dldmswo1209.cocoatalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.databinding.FriendItemBinding
import com.dldmswo1209.cocoatalk.model.User

class FriendListAdapter(val itemClick: (User) -> (Unit)): ListAdapter<User,FriendListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.nameTextView.text = user.name
            if(user.state_msg == null){
                binding.stateMassageTextView.isVisible = false
            }else{
                binding.stateMassageTextView.text = user.state_msg
                binding.stateMassageTextView.isVisible = true
            }
            if(user.image == null) {
                Glide.with(binding.root)
                    .load(R.drawable.profile_default)
                    .into(binding.profileImageView)
            }else{
                Glide.with(binding.root)
                    .load(user.image?.toUri())
                    .into(binding.profileImageView)
            }

            binding.root.setOnClickListener {
                itemClick(user)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

}