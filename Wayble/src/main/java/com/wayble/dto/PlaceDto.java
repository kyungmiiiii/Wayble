package com.wayble.dto;

public class PlaceDto {
	private int placeIdx;				// 명소 기본키
	private int cityIdx;				// 도시 기본키
	private String name;				// 명소 이름
	private String pic;					// 명소 사진
	private double lat;					// 명소 위도
	private double lng;					// 명소 경도
	private double placeRate;			// 명소 별점
	private int placeReviewCount;		// 명소 리뷰 개수
	
	public PlaceDto() {}
	public PlaceDto(int placeIdx, int cityIdx, String name, String pic, double lat, double lng, double placeRate, int placeReviewCount) {
		this.placeIdx = placeIdx;
		this.cityIdx = cityIdx;
		this.name = name;
		this.pic = pic;
		this.lat = lat;
		this.lng = lng;
		this.placeRate = placeRate;
		this.placeReviewCount = placeReviewCount;
	}
	public int getPlaceIdx() {
		return placeIdx;
	}
	public void setPlaceIdx(int placeIdx) {
		this.placeIdx = placeIdx;
	}
	public int getCityIdx() {
		return cityIdx;
	}
	public void setCityIdx(int cityIdx) {
		this.cityIdx = cityIdx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
	public double getPlaceRate() {
		return placeRate;
	}
	public void setPlaceRate(double placeRate) {
		this.placeRate = placeRate;
	}
	public int getPlaceReviewCount() {
		return placeReviewCount;
	}
	public void setPlaceReviewCount(int placeReviewCount) {
		this.placeReviewCount = placeReviewCount;
	}
	
	

	
	

}
