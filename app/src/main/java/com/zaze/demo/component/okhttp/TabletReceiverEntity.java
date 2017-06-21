package com.zaze.demo.component.okhttp;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-06-15 - 14:24
 */
public class TabletReceiverEntity extends BaseEntity {
    private String sReceiverName;
    private String sDevNumber;
    private String sMacAddress;
    private int iReceivePurpose;
    private String sReceiverAccount;


    public String getSReceiverName() {
        return sReceiverName;
    }

    public void setSReceiverName(String sReceiverName) {
        this.sReceiverName = sReceiverName;
    }

    public String getSDevNumber() {
        return sDevNumber;
    }

    public void setSDevNumber(String sDevNumber) {
        this.sDevNumber = sDevNumber;
    }

    public String getSMacAddress() {
        return sMacAddress;
    }

    public void setSMacAddress(String sMacAddress) {
        this.sMacAddress = sMacAddress;
    }

    public int getIReceivePurpose() {
        return iReceivePurpose;
    }

    public void setIReceivePurpose(int iReceivePurpose) {
        this.iReceivePurpose = iReceivePurpose;
    }

    public String getSReceiverAccount() {
        return sReceiverAccount;
    }

    public void setSReceiverAccount(String sReceiverAccount) {
        this.sReceiverAccount = sReceiverAccount;
    }
}
