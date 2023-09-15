package com.zaze.accessibility

import com.zaze.core.model.data.AdRule
import com.zaze.core.model.data.AdRules
import com.zaze.utils.FileUtil
import com.zaze.utils.JsonUtil
import com.zaze.utils.ext.jsonToList
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-10 21:34
 */
object AdRulesLoader {
    fun parseLTTRules(jsonInputStream: InputStream): AdRules {
        val jsonStr = FileUtil.readByBytes(jsonInputStream).toString()
        val jsonArray = JSONArray(jsonStr)
        val adRulesList = ArrayList<AdRules>()
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            jsonObj.keys().forEach { key ->
                // key = "320552729"  随机数
                // value = "{\"popup_rules\":[{\"id\":\"立即更新\",\"action\":\"稍后更新\"}]}"
                val ruleObj = JSONObject(jsonObj.optString(key))
                adRulesList.add(parseAdRules(ruleObj))
                // 解析 popup_rules
//                JsonUtil.parseJson(value, AdRules::class.java)?.let { rules ->
////                    ZLog.i(ZTag.TAG, "解析规则 success")
//                    adRulesList.add(rules)
//                }
            }
        }
        val rules = mutableListOf<AdRule?>()
        adRulesList.forEach {
            rules.addAll(it.popupRules ?: emptyList())
        }
//        ZLog.i(ZTag.TAG, "adRulesList size: ${adRulesList.size}")
        return AdRules(null, rules.filterNotNull())
    }

    fun parseRules(jsonInputStream: InputStream): List<AdRules> {
        val jsonStr = FileUtil.readByBytes(jsonInputStream).toString()
        val jsonArray = JSONArray(jsonStr)
        val adRulesList = ArrayList<AdRules>()
        for (i in 0 until jsonArray.length()) {
            adRulesList.add(parseAdRules(jsonArray.getJSONObject(i)))
        }
        return adRulesList
    }

    private fun parseAdRules(jsonObj: JSONObject): AdRules {
        val popupRules = jsonObj.optString("popup_rules")
        val packageName = if (jsonObj.has("package_name")) {
            jsonObj.optString("package_name")
        } else {
            null
        }
        return AdRules(packageName, popupRules.jsonToList(AdRule::class.java))
    }
}