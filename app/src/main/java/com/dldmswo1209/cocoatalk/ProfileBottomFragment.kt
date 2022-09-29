package com.dldmswo1209.cocoatalk

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.dldmswo1209.cocoatalk.databinding.FragmentProfileBottomBinding
import com.dldmswo1209.cocoatalk.model.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileBottomFragment(val user: User) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProfileBottomBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameTextView.text = user.name
        if(user.image == null){
            Glide.with(view)
                .load(R.drawable.profile_default)
                .circleCrop()
                .into(binding.profileImageView)
        }

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }

    }


}