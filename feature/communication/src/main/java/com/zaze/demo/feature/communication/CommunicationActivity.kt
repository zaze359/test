package com.zaze.demo.feature.communication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.activity.viewModels
import com.zaze.common.base.AbsActivity
import com.zaze.demo.feature.communication.aidl.RemoteService
import com.zaze.demo.feature.communication.messenger.MessengerService

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 14:38
 */
@Deprecated("see {@link CommunicationScreen.kt}")
class CommunicationActivity : AbsActivity() {

    private val viewModel: CommunicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unBind(this)
    }
}