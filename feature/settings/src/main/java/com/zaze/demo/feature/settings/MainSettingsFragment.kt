package com.zaze.demo.feature.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsFragment
import com.zaze.demo.feature.settings.adapter.MainSettingAdapter
import com.zaze.demo.feature.settings.databinding.SettingsFragmentMainBinding
import com.zaze.demo.feature.settings.model.SettingsItem

/**
 * 主设置页面
 * 其他类型设置的入口
 */
class MainSettingsFragment : AbsFragment() {
    private var _binding: SettingsFragmentMainBinding? = null
    val binding get() = _binding!!

    private val settings = listOf<SettingsItem>(
        SettingsItem( // 主题
            titleRes = R.string.settings_theme_title,
            summaryRes = R.string.settings_theme_summary,
            iconRes = R.drawable.settings_ic_palette,
            iconBackgroundColor = R.color.blue_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.red_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.purple_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.teal_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.deep_orange_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_notification_title,
            summaryRes = R.string.settings_notification_summary,
            iconRes = R.drawable.settings_ic_notifications_active,
            iconBackgroundColor = R.color.yellow_A400,
            actionIdRes = R.id.action_mainSettings_to_notificationSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.indigo_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.cyan_400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.light_green_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.light_green_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.light_green_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.light_green_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
        SettingsItem( // 关于
            titleRes = R.string.settings_about_title,
            summaryRes = R.string.settings_about_summary,
            iconRes = R.drawable.settings_ic_info_outline,
            iconBackgroundColor = R.color.light_green_A400,
            actionIdRes = R.id.action_mainSettings_to_themeSettings
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsRecycler.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = MainSettingAdapter(requireContext()) { item ->
                findNavController().navigate(item.actionIdRes)
            }.apply {
                submitList(settings)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}