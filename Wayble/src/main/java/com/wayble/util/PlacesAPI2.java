package com.wayble.util;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PlacesAPI2 {
	
	// 명소에 맞는 사진 가져오기
	// 파라미터 : 구글 명소 기본키(placeId)
	// 리턴값 : 사진URL
	public static String main(String placeId) throws Exception {
		HttpUtils htppUtils = new HttpUtils();
		String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="
				+ placeId
				+ "&key=" + CommonKey.googleKey;
		String method = "GET";
		String result = "";
		HttpURLConnection conn = null;
		
        //HttpURLConnection 객체 생성
		conn = htppUtils.getHttpURLConnection(url, method);
		//URL 연결에서 데이터를 읽을지에 대한 설정 ( defualt true )
        // conn.setDoInput(true); 
		//API에서 받은 데이터를 StringBuilder 형태로 리턴하여 줍니다. 
        result = htppUtils.getHttpRespons(conn);
        //해당 정보를 확인합니다.
//		System.out.println("GET = " + result);

		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject)parser.parse(result);
		jsonObj = (JSONObject)jsonObj.get("result");
		JSONArray jArr = (JSONArray)jsonObj.get("photos");
		jsonObj = (JSONObject)jArr.get(0);
		String photoReference = (String)jsonObj.get("photo_reference");
		
		String srcAttr = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference="+photoReference+"&key=" + CommonKey.googleKey;
		return srcAttr;
	}
}


