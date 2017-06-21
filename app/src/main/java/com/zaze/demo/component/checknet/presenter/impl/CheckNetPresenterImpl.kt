package com.zaze.demo.component.checknet.presenter.impl

import com.zaze.aarrepo.commons.base.ZBasePresenter
import com.zaze.aarrepo.utils.RootCmd
import com.zaze.aarrepo.utils.StringUtil
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.checknet.NetworkUtil
import com.zaze.demo.component.checknet.presenter.CheckNetPresenter
import com.zaze.demo.component.checknet.view.CheckNetView
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
        val dhcpInfo = NetworkUtil.getDhcpInfo(context)
        val wifiInfo = NetworkUtil.getConnectionInfo(context)
        view.showIP(NetworkUtil.intToInetAddress(dhcpInfo.ipAddress).hostAddress)
        // --------------------------------------------------
        val dns1 = NetworkUtil.intToInetAddress(dhcpInfo.dns1).hostAddress
        val dns2 = NetworkUtil.intToInetAddress(dhcpInfo.dns2).hostAddress
        view.showDNS(StringUtil.format("%s,%s", dns1, dns2))
        // --------------------------------------------------
        view.showGateway(NetworkUtil.intToInetAddress(dhcpInfo.gateway).hostAddress)
        //
        view.showSSID(wifiInfo.ssid)
        //
        view.showNetStatus("${wifiInfo.rssi}")
        //
        Single.fromCallable {
            RootCmd.execRootCmdForRes("ping -c 1 www.baidu.com")
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.showConnection(result.successMsg);

                })
    }

}
