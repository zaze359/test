package com.zaze.core.network.http;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-11 - 15:57
 */
public class ZMediaType {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_XML = "application/xml; charset=UTF-8";
    public static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded; charset=UTF-8";

    private String mediaType = APPLICATION_JSON;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
