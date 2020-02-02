package com.zaze.common.http;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-10-26 - 12:35
 */
public abstract class LRequestBody {

    private LRequestBody() {
    }

    /**
     * getContent
     *
     * @return String
     */
    public abstract @NonNull
    String getContent();

    /**
     * media type
     *
     * @return media type
     */
    public abstract @NonNull
    LMediaType getMediaType();


    /**
     * create
     *
     * @param mediaType mediaType
     * @param content   content
     * @return LRequestBody
     */
    public static LRequestBody create(@NonNull final LMediaType mediaType, final Object content) {
        return new LRequestBody() {
            @Override
            public @NonNull
            String getContent() {
                if (content instanceof String) {
                    return (String) content;
                } else if (content instanceof JSONObject) {
                    return content.toString();
                } else if (content instanceof JSONArray) {
                    return content.toString();
                } else {
                    return "";
                }
            }

            @Override
            public @NonNull
            LMediaType getMediaType() {
                return mediaType;
            }
        };
    }

    /**
     * create
     *
     * @param content content
     * @return LRequestBody
     */
    public static LRequestBody create(Object content) {
        return create(new LMediaType(), content);
    }

}
