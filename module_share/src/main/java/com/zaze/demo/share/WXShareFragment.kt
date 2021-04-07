package com.zaze.demo.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zaze.common.base.AbsFragment
import com.zaze.utils.log.ZLog
import kotlinx.android.synthetic.main.share_wx_frag.*

/**
 * Description :
 * @author : zaze
 * @version : 2020-09-11 - 16:41
 */
class WXShareFragment : AbsFragment() {
    companion object {
        const val TAG = "WXShareFragment"
        const val APP_ID = "wxd930ea5d5a258f4f"
    }

    private lateinit var api: IWXAPI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.share_wx_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        api = WXAPIFactory.createWXAPI(activity, APP_ID, false)
        shareWXRegBtn.setOnClickListener {
            api.registerApp(APP_ID).let {
                ZLog.i(TAG, "registerApp: ${APP_ID} >> $it")
            }
        }
        shareWXSendTextBtn.setOnClickListener {
            val msg = WXMediaMessage()
            msg.mediaObject = WXTextObject().apply {
                text = ShareUtils.SEND_TEXT
            }
            // msg.title = "Will be ignored";
            msg.description = ShareUtils.SEND_TEXT
            msg.mediaTagName = "mediaTagName"
            val req = SendMessageToWX.Req()
            req.transaction = ShareUtils.buildTransaction("text")
            req.message = msg
            req.scene = ShareUtils.mTargetScene
            api.sendReq(req)
        }
    }
}