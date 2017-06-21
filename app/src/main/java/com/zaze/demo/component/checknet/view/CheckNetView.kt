package com.zaze.demo.component.checknet.view

import com.zaze.aarrepo.commons.base.ZBaseView


/**
 * Description :
 * @author : zaze
 * @version : 2017-06-16 03:53 1.0
 */
interface CheckNetView : ZBaseView {
    fun showIP(ip: String)
    fun showDNS(dns: String)
    fun showGateway(gateway: String)
    fun showSSID(ssid: String)
    fun showNetStatus(status: String)
    fun showConnection(connection: String)
}
