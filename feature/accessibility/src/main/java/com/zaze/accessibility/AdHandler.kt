package com.zaze.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.zaze.accessibility.MyAccessibilityService.Companion
import com.zaze.core.data.repository.AdRepository
import com.zaze.core.model.data.AdRule
import com.zaze.core.model.data.AdRules
import com.zaze.utils.TraceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-10 23:34
 */
class AdHandler constructor(private val service: AccessibilityService, adRepository: AdRepository) {
    companion object {
        private const val TAG = "AdHandler"
    }

    private var isLoaded = false

    private val adRulesMap = ConcurrentHashMap<String, AdRules?>()
    suspend fun loadRules() {
        withContext(Dispatchers.IO) {
            TraceHelper.beginSection("加载AD规则...")
            val context = service.applicationContext
            val lttRulesAsync = async(Dispatchers.IO) {
                AdRulesLoader.parseLTTRules(context.assets.open("ltt_rules.json"))
            }
            val zazeRulesAsync = async(Dispatchers.IO) {
//                adRepository.getAllAdRules()
                AdRulesLoader.parseRules(context.assets.open("zaze_rules.json"))
            }
            val lttRules = lttRulesAsync.await()

            adRulesMap.putAll(zazeRulesAsync.await().associateBy { it.packageName ?: "" })
            //
            val normalRules = mutableListOf<AdRule>()
            normalRules.addAll(adRulesMap[""]?.popupRules ?: emptyList())
//            normalRules.addAll(lttRules.popupRules ?: emptyList())
            adRulesMap[""] = AdRules("", normalRules)
//            adRulesMap.values.forEach {
//                val packageName = it?.packageName ?: "通用"
//                it?.popupRules?.forEach {
//                    ZLog.i(ZTag.TAG, "加载AD规则 ${packageName}: $it")
//                }
//            }
//            Log.i(TAG, "AD规则加载完毕!!!")
            isLoaded = true
            TraceHelper.endSection("加载AD规则...")
        }
    }

    fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isLoaded) return
        runCatching {
            TraceHelper.beginSection("处理事件")
//                // 返回 活动窗口
//                Log.i(TAG, "onAccessibilityEvent source: ${event.source}")
            var eventText: String = when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_CLICKED -> "Clicked: "
                AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> "long_clicked: "
                AccessibilityEvent.TYPE_VIEW_SELECTED -> "Selected: "
                AccessibilityEvent.TYPE_VIEW_FOCUSED -> "Focused: "
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "text_changed: "
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    val windowClassName = event.className
                    val currentPackage = event.packageName?.toString() ?: ""
                    "window_state_changed: ${currentPackage}/${windowClassName}"
                }

                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    "notification_state_changed:"
                }

                else -> "eventType: ${event.eventType}: "
            }
            eventText += event.contentDescription ?: event.text

            Log.i(TAG, " --- onAccessibilityEvent: $eventText")
            service.rootInActiveWindow?.let { rootNode ->
//                Log.i(TAG, "rootNode: $rootNode")
                adRulesMap[(rootNode.packageName)]?.popupRules?.forEach {
                    executeRule(rootNode, it)
                } ?: adRulesMap[""]?.popupRules?.forEach {
                    executeRule(rootNode, it)
                }
            }
            TraceHelper.endSection("处理事件")
        }.onFailure {
            Log.e(TAG, " --- onAccessibilityEvent error", it)
        }
    }

    private fun executeRule(node: AccessibilityNodeInfo, adRule: AdRule?) {
        if (adRule == null) return
//        Log.i(TAG, "${node.packageName} executeRule: $adRule")
//        Log.i(TAG, "${node.packageName} id: ${adRule.id?.removePrefix("=")}")
//        Log.i(
//            TAG,
//            "${node.packageName} action: ${adRule.action?.removePrefix("=")}"
//        )

        if (!checkId(node, adRule)) return
        when (val action = adRule.action?.removePrefix("=")) {
            "GLOBAL_ACTION_BACK" -> {
                Log.i(TAG, "匹配到过滤规则: ${node.packageName}; $adRule")
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            }

            else -> {
                // 点击 取消广告
                service.clickNode(AccessibilityHelper.searchNode(node, action)?.apply {
                    Log.i(
                        TAG,
                        "匹配到过滤规则: ${node.packageName}; $adRule"
                    )
                })
            }
        }
    }

    private fun checkId(node: AccessibilityNodeInfo, adRule: AdRule): Boolean {
        if (adRule.id != adRule.action) {
            // 优先使用id来匹配 一下，精确范围，保证需要处理的 UI
            adRule.id?.removePrefix("=")?.split("&")?.forEach {
                if (AccessibilityHelper.searchNode(node, it) == null) {
                    return false
                }
            }
        }
        return true
    }
}