package com.duke.photobyduke.net;

import android.util.Xml;

import com.duke.photobyduke.entity.URLgetRecentInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UrlConfig {
	private static ArrayList<URLgetRecentInfo> urlList;

	private static void fetchUrlDataFromXml(InputStream in) {
		urlList = new ArrayList<URLgetRecentInfo>();

//		final XmlResourceParser xmlParser = activity.getApplication()
//				.getResources().getXml(R.xml.url);
		
        XmlPullParser xmlParser = Xml.newPullParser(); 
		try {
			xmlParser.setInput(in, "utf-8");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
 

		int eventCode;
		try {
			eventCode = xmlParser.getEventType();
			while (eventCode != XmlPullParser.END_DOCUMENT) {
//				LogWrapper.logD("eventCode:" + eventCode);

				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
//					LogWrapper.logD("xmlName:" + xmlParser.getName());
					if ("photo".equals(xmlParser.getName())) {
						final String id = xmlParser.getAttributeValue(null,
								"id");
						final URLgetRecentInfo urlData = new URLgetRecentInfo();
						urlData.setId(id);
						urlData.setOwner(xmlParser.getAttributeValue(null, "owner"));
						urlData.setSecret(xmlParser.getAttributeValue(null, "secret"));
						urlData.setServer(xmlParser.getAttributeValue(null, "server"));
						urlData.setFarm(xmlParser.getAttributeValue(null, "farm"));
						urlData.setTitle(xmlParser.getAttributeValue(null, "title"));
						urlData.setIsPublic(xmlParser.getAttributeValue(null, "ispublic"));
						urlData.setIsFamily(xmlParser.getAttributeValue(null, "isfamily"));
						urlData.setIsFriend(xmlParser.getAttributeValue(null, "isfriend"));
//						LogWrapper.logD("duke:" + xmlParser.getAttributeValue(null, "duke"));
						urlList.add(urlData);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				eventCode = xmlParser.next();
			}
		} catch (final XmlPullParserException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
////			xmlParser.close();
		}
	}

	public static ArrayList<URLgetRecentInfo> getURLList(InputStream in) {
		// 如果urlList还没有数据（第一次），或者被回收了，那么（重新）加载xml
		if (urlList == null || urlList.isEmpty()){
			fetchUrlDataFromXml(in);			
		} 

		return urlList;
	}
}