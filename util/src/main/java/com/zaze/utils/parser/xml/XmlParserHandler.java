package com.zaze.utils.parser.xml;


import android.text.TextUtils;

import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

/**
 * Description	: SAX 解析类
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class XmlParserHandler {
    private ContentParserHandler parserHandler;
    private XmlParserCallback parser;

    public XmlParserHandler(XmlParserCallback parser) {
        super();
        this.parser = parser;
        parserHandler = new ContentParserHandler();
    }

    public void parser(String content) {
        final String value = content;
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(parserHandler);
            ZLog.i(ZTag.TAG_XML, "parser value : " + value);
            if (!TextUtils.isEmpty(value)) {
                xmlReader.parse(new InputSource(new StringReader(value)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ContentParserHandler extends DefaultHandler {
        private String value = "";

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            parser.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            parser.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            parser.startElement(qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            ZLog.i(ZTag.TAG_XML, ZStringUtil.format("qName : %s; value : %s", qName, value));
            parser.endElement(qName, value);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            value = new String(ch, start, length);
            parser.characters(ch, start, length);
        }
    }
}
