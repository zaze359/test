package com.zaze.utils.parser.xml;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-01-16 - 19:51
 */
public class SimpleLayoutParser {
    private static final String TAG_NAME_SPACE = "http://schemas.android.com/apk/res-auto/com.android.launcher3";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_RESOLVE = "resolve";
    private static final String ATTR_WORKSPACE = "workspace";
    private static final String ATTR_CONTAINER = "container";

    private static final String FORMATTED_LAYOUT_RES_WITH_HOSTEAT = "default_layout_%dx%d_h%s";
    private static final String FORMATTED_LAYOUT_RES = "default_layout_%dx%d";
    private static final String LAYOUT_RES = "default_layout";

    private Resources resources;
    private ContentValues mValues;

    public SimpleLayoutParser(Context context) {
        this.resources = context.getResources();
        mValues = new ContentValues();
    }


    public void parser(int layoutId, ArrayList<Long> screenIds) {
        try {
            XmlResourceParser parser = resources.getXml(layoutId);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        startDocument();
                        break;
                    case XmlPullParser.START_TAG:
                        startElement(parser, screenIds);
                        break;
                    case XmlPullParser.TEXT:
                        characters(parser);
                        break;
                    case XmlPullParser.END_TAG:
                        endElement(parser);
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            endDocument();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始解析文件
     */
    private void startDocument() {
        ZLog.i(ZTag.TAG_XML, "startDocument");
    }

    /**
     * 结束解析文件
     */
    private void endDocument() {
        ZLog.i(ZTag.TAG_XML, "endDocument");
    }


    /**
     * 开始解析节点
     */
    private void startElement(XmlResourceParser parser, ArrayList<Long> screenIds) {
        ZLog.i(ZTag.TAG_XML, "-----------------------------------");
        String name = parser.getName();
        ZLog.i(ZTag.TAG_XML, "开始解析 : " + name);
        if (TAG_INCLUDE.equals(name)) {
            final int resId = getAttributeResourceValue(parser, ATTR_WORKSPACE, 0);
            if (resId != 0) {
                parser(resId, screenIds);
            }
        } else if (TAG_RESOLVE.equals(name)) {
            mValues.clear();
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                String attributeName = parser.getAttributeName(i);
                String attributeValue = parser.getAttributeValue(i);
                ZLog.i(ZTag.TAG_XML, attributeName + " = " + attributeValue);
            }
        }
    }

    /**
     * 结束解析节点
     */
    private void endElement(XmlResourceParser parser) {
        ZLog.i(ZTag.TAG_XML, "结束解析 : " + parser.getName());
        ZLog.i(ZTag.TAG_XML, "-----------------------------------");
    }


    /**
     * 解析内容
     *
     * @throws IOException
     * @throws XmlPullParserException
     */
    protected void characters(XmlResourceParser parser) throws IOException, XmlPullParserException {
        ZLog.i(ZTag.TAG_XML, "characters = " + parser.nextText());
    }

    // --------------------------------------------------
    protected static int getAttributeResourceValue(XmlResourceParser parser, String attribute, int defaultValue) {
        int value = parser.getAttributeResourceValue(TAG_NAME_SPACE, attribute, defaultValue);
        if (value == defaultValue) {
            value = parser.getAttributeResourceValue(null, attribute, defaultValue);
        }
        return value;
    }

}