package com.dedebkc.intermediate.ui.mainmenu.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.dedebkc.intermediate.R
import com.dedebkc.intermediate.databinding.FragmentDetailImageBinding

class DetailImageFragment : DialogFragment() {
    private var _binding: FragmentDetailImageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val bundle = DetailImageFragmentArgs.fromBundle(arguments as Bundle)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )

        Glide.with(requireActivity())
            .load(bundle.image)
            .fitCenter()
            .into(binding.ivDetailImage)

        binding.tvName.text = "${getString(R.string.name)}: ${bundle.name}"
        binding.tvDetailDescription.text = "${getString(R.string.title_description)}: ${bundle.description}"

//        sharedElementEnterTransition = animation
//        sharedElementReturnTransition = animation

        binding.root.setOnClickListener { dismiss() }
    }
}