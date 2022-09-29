package com.dldmswo1209.cocoatalk.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dldmswo1209.cocoatalk.viewController.MainActivity
import com.dldmswo1209.cocoatalk.databinding.FragmentAddFriendBottomBinding
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddFriendBottomFragment() : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentAddFriendBottomBinding
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddFriendBottomBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user_id = (activity as MainActivity).user_id

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }
        binding.addFriendButton.setOnClickListener {
            val friend_id = binding.friendIdEditText.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    mainViewModel.addFriend(user_id, friend_id)
                }.await()
                async {
                    mainViewModel.getAllMyFriend(user_id)
                }
            }
            dialog?.dismiss()
        }





    }
}