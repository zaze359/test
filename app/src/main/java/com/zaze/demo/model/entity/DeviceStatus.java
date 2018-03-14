package com.zaze.demo.model.entity;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 14:03
 */
public class DeviceStatus {
    private String tag;
    private String name;
    private String content;

    public DeviceStatus() {
    }

    public DeviceStatus(String tag, String name, String content) {
        this.tag = tag;
        this.name = name;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
