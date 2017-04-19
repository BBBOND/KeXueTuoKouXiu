package com.kim.kexuetuokouxiu.utils;

import com.kim.kexuetuokouxiu.bean.Comment;
import com.kim.kexuetuokouxiu.bean.Programme;
import com.kim.kexuetuokouxiu.bean.ScienceTalkShow;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;

/**
 * 解析工具
 * Created by Weya on 2016/11/10.
 */

public class ParseUtil {

    public static ScienceTalkShow parseXml2ScienceTalkShowWithProgrammes(String xml) {
        ScienceTalkShow scienceTalkShow = parseXml2ScienceTalkShow(xml);
        if (scienceTalkShow == null)
            return null;
        else
            scienceTalkShow.setProgrammes(parseXml2ProgrammeList(xml));
        return scienceTalkShow;
    }

    public static ScienceTalkShow parseXml2ScienceTalkShow(String xml) {
        ScienceTalkShow scienceTalkShow = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
                    System.getProperty(XmlPullParserFactory.PROPERTY_NAME),
                    Thread.currentThread().getContextClassLoader().getClass());
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
                        if ("title".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setTitle(parser.nextText());
                        } else if ("link".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setLink(parser.nextText());
                        } else if ("description".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setDescription(parser.nextText());
                        } else if ("lastBuildDate".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setLastBuildDate(parser.nextText());
                        } else if ("language".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setLanguage(parser.nextText());
                        } else if ("itunes:summary".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setSummary(parser.nextText());
                        } else if ("itunes:name".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setAuthorName(parser.nextText());
                        } else if ("itunes:email".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setAuthorEmail(parser.nextText());
                        } else if ("copyright".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setCopyright(parser.nextText());
                        } else if ("itunes:subtitle".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setSubtitle(parser.nextText());
                        } else if ("url".equals(nodeName)  && scienceTalkShow != null) {
                            scienceTalkShow.setImage(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            scienceTalkShow = null;
            e.printStackTrace();
        }
        return scienceTalkShow;
    }

    public static RealmList<Programme> parseXml2ProgrammeList(String xml) {
        RealmList<Programme> programmeList = null;
        Programme programme = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance(
                    System.getProperty(XmlPullParserFactory.PROPERTY_NAME),
                    Thread.currentThread().getContextClassLoader().getClass());
            XmlPullParser parser = factory.newPullParser();
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        programmeList = new RealmList<Programme>();
                        break;
                    case XmlPullParser.START_TAG:
                        programme = formatProgramme(programme, parser, nodeName);
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(nodeName) && programmeList != null) {
                            programmeList.add(programme);
                            programme = null;
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            programmeList = null;
            e.printStackTrace();
        }
        return programmeList;
    }

    private static Programme formatProgramme(Programme programme, XmlPullParser parser, String nodeName) throws XmlPullParserException, IOException {
        if ("item".equals(nodeName)) {
            programme = new Programme();
        } else if ("title".equals(nodeName) && programme != null) {
            programme.setTitle(parser.nextText());
        } else if ("link".equals(nodeName) && programme != null) {
            programme.setLink(parser.nextText());
        } else if ("pubDate".equals(nodeName) && programme != null) {
            programme.setPubDate(parser.nextText());
        } else if ("dc:creator".equals(nodeName) && programme != null) {
            programme.setCreator(parser.nextText());
        } else if ("category".equals(nodeName) && programme != null) {
            programme.setCategory(parser.nextText());
        } else if ("description".equals(nodeName) && programme != null) {
            programme.setDescription(parser.nextText());
        } else if ("content:encoded".equals(nodeName) && programme != null) {
            programme.setSummary(parser.nextText());
        } else if ("wfw:commentRss".equals(nodeName) && programme != null) {
            programme.setCommentRss(parser.nextText());
        } else if ("slash:comments".equals(nodeName) && programme != null) {
            programme.setComments(parser.nextText());
        } else if ("enclosure".equals(nodeName) && programme != null) {
            String url = parser.getAttributeValue("url", "");
            programme.setMediaUrl(url);
            try {
                programme.setId(url != null && !url.equals("") ? EncryptUtils.MD5HEX(url) : UUID.randomUUID().toString());
            } catch (NoSuchAlgorithmException e) {
                programme.setId(UUID.randomUUID().toString());
            }
        } else if ("itunes:duration".equals(nodeName) && programme != null) {
            programme.setDuration(parser.nextText());
        }
        return programme;
    }

    public static List<Comment> parseXml2Comments(String xml) {
        List<Comment> comments = null;
        Comment comment = null;
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
                        comments = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("item".equals(nodeName)) {
                            comment = new Comment();
                        } else if ("title".equals(nodeName) && comment != null) {
                            comment.setTitle(parser.nextText());
                        } else if ("link".equals(nodeName) && comment != null) {
                            comment.setLink(parser.nextText());
                        } else if ("dc:creator".equals(nodeName) && comment != null) {
                            comment.setCreator(parser.nextText());
                        } else if ("pubDate".equals(nodeName) && comment != null) {
                            comment.setPubDate(parser.nextText());
                        } else if ("description".equals(nodeName) && comment != null) {
                            comment.setDescription(parser.nextText());
                        } else if ("content:encoded".equals(nodeName) && comment != null) {
                            comment.setContent(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(nodeName) && comments != null && comment != null) {
                            comments.add(comment);
                            comment = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            comments = null;
        }
        return comments;
    }
}
