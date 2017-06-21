package com.zaze.demo.component.checknet.ui


import android.os.Bundle
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.checknet.presenter.CheckNetPresenter
import com.zaze.demo.component.checknet.presenter.impl.CheckNetPresenterImpl
import com.zaze.demo.component.checknet.view.CheckNetView
import kotlinx.android.synthetic.main.activity_check_net.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-06-16 03:53 1.0
 */
class CheckNetActivity : ZBaseActivity(), CheckNetView {
    var presenter: CheckNetPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_net)
        presenter = CheckNetPresenterImpl(this)
        presenter!!.checkNet()
    }

    override fun showIP(ip: String) {
        check_net_ip_tv.text = "IP : $ip"
    }

    override fun showDNS(dns: String) {
        check_net_dns_tv.text = "DNS : $dns"
    }

    override fun showGateway(gateway: String) {
        check_net_gateway_tv.text = "GATEWAY : $gateway"
    }

    override fun showSSID(ssid: String) {
        check_net_wifi_tv.text = "SSID : $ssid"
    }

    override fun showNetStatus(status: String) {
        check_net_status_tv.text = "STATUS : $status"
    }

    override fun showConnection(connection: String) {
        check_net_connect_tv.text = "CONNECTION : $connection"
    }
}