package com.dldmswo1209.cocoatalk

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dldmswo1209.cocoatalk.adapter.FriendListAdapter
import com.dldmswo1209.cocoatalk.databinding.FragmentFriendBinding
import com.dldmswo1209.cocoatalk.viewModel.LoginViewModel
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel

class FriendFragment : Fragment() {
    private lateinit var binding: FragmentFriendBinding
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user_id = (activity as MainActivity).user_id
        val adapter = FriendListAdapter { user ->
            val bottomSheet = ProfileBottomFragment(user)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        binding.friendListRecyclerView.adapter = adapter

        mainViewModel.getAllMyFriend(user_id)

        mainViewModel.friendList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

    }
}