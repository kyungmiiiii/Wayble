package com.wayble.util;

import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TranslateSearch {
	
	// 검색어 특정 나라의 언어로 번역하기 
	// 파라미터 : 검색어(search), 나라(country)
	// 리턴값 : 번역된 검색어(done)
	public static String main(String search, String country) {
		
		switch(country) {
		case "일본": country = "ja"; break;
		case "한국": country = "ko"; break;
		case "대만": country = "zh-TW"; break;
		case "프랑스": country = "fr"; break;
		case "영국": country = "en"; break;
		case "미국": country = "en"; break;
		case "태국": country = "th"; break;
		}
		
		HttpUtils htppUtils = new HttpUtils();
		
		String url = "https://translation.googleapis.com/language/translate/v2?key=" + CommonKey.googleKey
				+ "&q=" + search
				+ "&source=ko"
				+ "&target=" + country;
		String method = "GET";
		String result = "";
		
		HttpURLConnection conn = null;
		conn = htppUtils.getHttpURLConnection(url, method);
		result = htppUtils.getHttpRespons(conn);
		System.out.println("GETTRANSLATE = " + result);
		
		JSONParser parser = new JSONParser();
		JSONObject jObj = null;
		try {
			jObj = (JSONObject)parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		jObj = (JSONObject)jObj.get("data");	
		JSONArray jArr = (JSONArray)jObj.get("translations");
		jObj = (JSONObject)jArr.get(0);
		String done = jObj.get("translatedText") + "";
		
//		System.out.println(done);
		return done;
		
	}

}
