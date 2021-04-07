//package com.zaze.demo.share
//
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
//
///**
// * Description :
// * @author : zaze
// * @version : 2020-09-11 - 17:39
// */
//object ShareUtils {
//    const val SEND_TEXT = "这段文字发送自Demo示例程序"
//
//
//    var mTargetScene = SendMessageToWX.Req.WXSceneSession
//
//    fun buildTransaction(type: String?): String {
//        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
//    }
//}