package com.dedebkc.intermediate.ui.mainmenu.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dedebkc.intermediate.ui.ViewModelFactory
import com.dedebkc.intermediate.ui.auth.AuthenticationActivity
import com.dedebkc.intermediate.ui.auth.AuthenticationViewModel
import com.dedebkc.intermediate.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {
    private lateinit var factory: ViewModelFactory
    private val settingViewModel: SettingViewModel by activityViewModels { factory }
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels { factory }
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity())

        initObserve()
        initView()
    }

    private fun initView() {
        binding.setLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.localName.text = Locale.getDefault().displayName
        binding.actionLogout.setOnClickListener {
            startActivity(Intent(activity, AuthenticationActivity::class.java))
            activity?.finish()
        }

        binding.actionLogout.setOnClickListener {
            authenticationViewModel.logout()
            settingViewModel.saveIsFirstTime(false)
            startActivity(Intent(activity, AuthenticationActivity::class.java))
            activity?.finish()
        }
    }

    private fun initObserve() {
        settingViewModel.getUserName().observe(viewLifecycleOwner) { userName ->
            binding.tvName.text = userName
        }
        settingViewModel.getUserEmail().observe(viewLifecycleOwner) { userEmail ->
            binding.tvEmail.text = userEmail
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}