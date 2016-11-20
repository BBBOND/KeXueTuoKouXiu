package com.kim.kexuetuokouxiu.utils;

import com.kim.kexuetuokouxiu.bean.Programme;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Weya on 2016/11/10.
 */

public class ParseUtil {

    public static ScienceTalkShow parseXml2Obj(String xml) {
        ScienceTalkShow scienceTalkShow = null;
        Programme programme = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        scienceTalkShow = new ScienceTalkShow();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("title".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setTitle(parser.nextText());
                        } else if ("link".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setLink(parser.nextText());
                        } else if ("description".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setDescription(parser.nextText());
                        } else if ("lastBuildDate".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setLastBuildDate(parser.nextText());
                        } else if ("language".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setLanguage(parser.nextText());
                        } else if ("itunes:summary".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setItunes_summary(parser.nextText());
                        } else if ("itunes:author".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setItunes_author(parser.nextText());
                        } else if ("copyright".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setCopyright(parser.nextText());
                        } else if ("itunes:subtitle".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setItunes_subtitle(parser.nextText());
                        } else if ("url".equals(nodeName) && programme == null) {
                            assert scienceTalkShow != null;
                            scienceTalkShow.setImage(parser.nextText());
                        } else if ("item".equals(nodeName)) {
                            programme = new Programme();
                        } else if ("title".equals(nodeName)) {
                            assert programme != null;
                            programme.setTitle(parser.nextText());
                        } else if ("link".equals(nodeName)) {
                            assert programme != null;
                            programme.setLink(parser.nextText());
                        } else if ("comments".equals(nodeName)) {
                            assert programme != null;
                            programme.setCommentsUrl(parser.nextText());
                        } else if ("pubDate".equals(nodeName)) {
                            assert programme != null;
                            programme.setPubDate(parser.nextText());
                        } else if ("dc:creator".equals(nodeName)) {
                            assert programme != null;
                            programme.setDcCreator(parser.nextText());
                        } else if ("category".equals(nodeName)) {
                            assert programme != null;
                            programme.setCategory(parser.nextText());
                        } else if ("description".equals(nodeName)) {
                            assert programme != null;
                            programme.setDescription(parser.nextText());
                        } else if ("content:encoded".equals(nodeName)) {
                            assert programme != null;
                            programme.setContentEncoded(parser.nextText());
                        } else if ("wfw:commentRss".equals(nodeName)) {
                            assert programme != null;
                            programme.setWfwCommentRss(parser.nextText());
                        } else if ("slash:comments".equals(nodeName)) {
                            assert programme != null;
                            programme.setSlashComments(parser.nextText());
                        } else if ("enclosure".equals(nodeName)) {
                            assert programme != null;
                            programme.setEnclosureUrl(parser.getAttributeValue(0));
                        } else if ("itunes:duration".equals(nodeName)) {
                            assert programme != null;
                            programme.setDuration(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(nodeName) && scienceTalkShow != null) {
                            scienceTalkShow.addProgramme(programme);
                            programme = null;
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scienceTalkShow;
    }
}
