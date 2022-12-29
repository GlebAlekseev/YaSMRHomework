package com.glebalekseevjk.yasmrhomework.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding: FragmentAuthBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.openYandexAuthClickListener = View.OnClickListener {
            val uri = getString(R.string.authorize_url).toUri()
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.intent.putExtra(
                CustomTabsIntent.EXTRA_TITLE_VISIBILITY_STATE,
                CustomTabsIntent.NO_TITLE
            )
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            // todo: delete cookie
            customTabsIntent.launchUrl(requireContext(), uri)
        }
    }
}