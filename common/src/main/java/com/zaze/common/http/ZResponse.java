package com.zaze.common.http;

import com.zaze.utils.JsonUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.Map;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-12-21 - 00:37
 */
public class ZResponse {
    private ZRequest request;
    private int code;
    private String responseBody;
    private Map<String, Object> headers;
    private String message;

    public ZResponse(ZRequest request) {
        this.request = request;
    }

    public ZRequest getRequest() {
        return request;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setRequest(ZRequest request) {
        this.request = request;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    // --------------------------------------------------

    /**
     * 输出log
     */
    public void log() {
        StringBuilder builder = new StringBuilder();
        builder.append("url: ").append(request.getUrl())
                .append("\nresponseBody: ").append(responseBody)
                .append("\nmessage: ").append(message)
                .append("\nheaders: ");
        try {
            builder.append(JsonUtil.mapToJson(headers).toString(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ZLog.v(ZTag.TAG_HTTP, builder.toString());
    }
    // --------------------------------------------------

    /**
     * 是否成功
     *
     * @return true成功
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    /**
     * Session 是否失效
     *
     * @return boolean
     */
    public boolean isSessionFailed() {
        return 107000401 == code;
    }


    /**
     * 从异常中拷贝数据
     *
     * @param throwable throwable
     * @return 变更后的LResponse
     */
    public ZResponse copyFrom(@NotNull Throwable throwable) {
        setCode(-1);
        setResponseBody(throwable.getMessage());
        return this;
    }
}
