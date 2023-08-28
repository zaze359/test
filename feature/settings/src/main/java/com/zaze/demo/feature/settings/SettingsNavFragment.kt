package com.zaze.demo.feature.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.findNavHostFragment
import com.zaze.common.base.ext.initToolbar
import com.zaze.demo.feature.settings.databinding.SettingsFragmentNavBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 设置导航页
 */
@AndroidEntryPoint
class SettingsNavFragment : AbsFragment(), NavController.OnDestinationChangedListener {

    private var _binding: SettingsFragmentNavBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentNavBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.appbarLayout.toolbar) {
            title = "设置"
            it.isTitleCentered = false
            it.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavHostFragment(R.id.fragment_container).navController.removeOnDestinationChangedListener(
            this
        )
        _binding = null
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == controller.graph.startDestinationId) { // 去除第一个页面的进入动画。
            findNavHostFragment(R.id.fragment_container).childFragmentManager.fragments.firstOrNull()?.enterTransition = null
        }
        binding.appbarLayout.title = destination.label?.toString() ?: ""
    }
}