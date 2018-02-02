package com.zaze.utils.parser.xml;


import org.xml.sax.Attributes;


/**
 * Description	: 解析类
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public interface XmlParserCallback {

    /**
     * 开始解析文件
     */
    void startDocument();

    /**
     * 结束解析文件
     */
    void endDocument();

    void startElement(String qName, Attributes attributes);

    void endElement(String qName, String value);

    void characters(char[] ch, int start, int length);

}
