package com.zaze.demo.component.checknet;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-07-17 - 11:07
 */
public class NetworkInfo {
    private String ip;      // 192.168.2.34
    private List<String> dns;     // 192.168.2.1  0.0.0.0
    private String gateWay; // 192.168.2.1
    private String ssid;    // xhoffice
    private int status;     // 网络状态 -43

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getDns() {
        return dns;
    }

    public void setDns(List<String> dns) {
        this.dns = dns;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
