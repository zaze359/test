package com.zaze.demo.component.checknet.presenter.impl

import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.checknet.presenter.CheckNetPresenter
import com.zaze.demo.component.checknet.view.CheckNetView
import com.zaze.utils.ZCommand
import com.zaze.utils.ZNetUtil
import com.zaze.utils.ZStringUtil
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Description :
 * @author : zaze
 * @version : 2017-06-16 03:53 1.0
 */
open class CheckNetPresenterImpl(view: CheckNetView) : ZBasePresenter<CheckNetView>(view), CheckNetPresenter {

    override fun checkNet() {
        val context = MyApplication.getInstance()
        val dhcpInfo = ZNetUtil.getDhcpInfo(context)
        val wifiInfo = ZNetUtil.getConnectionInfo(context)
        view.showIP(ZNetUtil.intToInetAddress(dhcpInfo.ipAddress).hostAddress)
        // --------------------------------------------------
        val dns1 = ZNetUtil.intToInetAddress(dhcpInfo.dns1).hostAddress
        val dns2 = ZNetUtil.intToInetAddress(dhcpInfo.dns2).hostAddress
        view.showDNS(ZStringUtil.format("%s,%s", dns1, dns2))
        // --------------------------------------------------
        view.showGateway(ZNetUtil.intToInetAddress(dhcpInfo.gateway).hostAddress)
        //
        view.showSSID(wifiInfo.ssid)
        //
        view.showNetStatus("${wifiInfo.rssi}")
        //
        Single.fromCallable {
            ZCommand.execCmdForRes("ping -c 1 www.baidu.com")
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.showConnection(result.successMsg);

                })
    }

}
