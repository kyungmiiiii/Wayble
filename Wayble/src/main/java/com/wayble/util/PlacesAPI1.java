package com.wayble.util;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.wayble.dao.CommonDao;
import com.wayble.dto.CityDto;
import com.wayble.dto.PlaceInfoDto;

//import com.wayble.dto.PlaceInfoDto;

// 0. place_id 얻기.
public class PlacesAPI1 {
	// 명소 리스트 불러오기 
	// 파라미터 : 도시정보(dto), 검색어(search), 토큰(token)
	// 리턴값 : 토큰(token), [명소정보(PlaceInfoDto)]
	public static ResponsePlaces main(CityDto dto, String search, String token) throws Exception {
		double cityLat = dto.getLat();
		double cityLng = dto.getLng();
		String country = dto.getCountry();
		
		HttpUtils htppUtils = new HttpUtils();
		//String find = "도톤보리 글리코사인";
		if(search == null || search.equals("")) {
			search = "store|tourist%20attraction|restaurant";
		} else {
			search = TranslateSearch.main(search, country);
		}
		String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?"
						+ (token == null ? "" : "pagetoken=" + token + "&")
						+ "query=" + URLEncoder.encode(search,"UTF-8")
						+ "&location=" + cityLat + "," + cityLng
						+ "&radius=10000&language=ko"
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
		System.out.println("GET1 = " + result);

		JSONParser parser = new JSONParser(); 
		JSONObject jsonObj = (JSONObject)parser.parse(result);
		
		//토큰 얻기
		token = (String)jsonObj.get("next_page_token");
		
		// 결과에서 필요한 가게 정보 얻기
		JSONArray jArr = (JSONArray)jsonObj.get("results");
		ArrayList<PlaceInfoDto> list = new ArrayList<>();
		
		for(int i=0; i<jArr.size(); i++) {
			jsonObj = (JSONObject)jArr.get(i);
			String address = (String)jsonObj.get("formatted_address");
			String placeId = (String)jsonObj.get("place_id");
			String store = (String)jsonObj.get("name");
			double rate = 0;
			int review = 0;
			try {
				rate = (double)jsonObj.get("rating");
				review = (int)((long)jsonObj.get("user_ratings_total"));
			} catch (Exception e) { } 
			
			jsonObj = (JSONObject)jsonObj.get("geometry");
			jsonObj = (JSONObject)jsonObj.get("location");
			
			double lat = (double)jsonObj.get("lat");
			double lng = (double)jsonObj.get("lng");
			
			PlaceInfoDto pDto = new PlaceInfoDto();
			pDto.setAddress(address);
			pDto.setPlaceId(placeId);
			pDto.setStore(store);
			pDto.setLat(lat);
			pDto.setLng(lng);
			pDto.setReview(review);
			pDto.setRate(rate);
			pDto.setPic(PlacesAPI2.main(placeId));
			
			list.add(pDto);
		}
		ResponsePlaces r = new ResponsePlaces(token, list);
		return r;
	}
	
	public static void main(String[] args) {

	}
	
	
}