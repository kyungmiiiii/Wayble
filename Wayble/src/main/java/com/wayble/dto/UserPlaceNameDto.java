package com.wayble.dto;

public class UserPlaceNameDto {
	private String name;		// 도시 이름
	private int placeIdx;		// 명소 기본키
	private int userPlaceIdx;	// 선택된 명소 기본키
	private double lat;			// 명소 위도
	private double lng;			// 명소 경도
	
	public UserPlaceNameDto() { }
	public UserPlaceNameDto(String name, int placeIdx, int userPlaceIdx, double lat, double lng) {
		this.name = name; this.placeIdx = placeIdx;
		this.userPlaceIdx = userPlaceIdx;
		this.lat = lat; this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlaceIdx() {
		return placeIdx;
	}

	public void setPlaceIdx(int placeIdx) {
		this.placeIdx = placeIdx;
	}

	public int getUserPlaceIdx() {
		return userPlaceIdx;
	}

	public void setUserPlaceIdx(int userPlaceIdx) {
		this.userPlaceIdx = userPlaceIdx;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
}
