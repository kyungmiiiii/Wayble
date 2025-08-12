package com.wayble.util;

import java.util.ArrayList;

import com.wayble.dto.PlaceInfoDto;

public class ResponsePlaces {
	private String token;	// 다음 페이지 토큰 
	private ArrayList<PlaceInfoDto> list;	// 명소 정보 리스트
	
	public ResponsePlaces(String token, ArrayList<PlaceInfoDto> list) {
		this.token = token;
		this.list = list;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ArrayList<PlaceInfoDto> getList() {
		return list;
	}

	public void setList(ArrayList<PlaceInfoDto> list) {
		this.list = list;
	}
	

}
