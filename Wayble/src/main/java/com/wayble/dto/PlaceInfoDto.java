package com.wayble.dto;

public class PlaceInfoDto {
	private String address;			// 명소 주소
	private String placeId;			// 명소 기본키
	private String store;			// 명소
	private String pic;				// 명소 사진
	private double lat;				// 명소 위도
	private double lng;				// 명소 경도
	private int review;				// 명소 리뷰
	private double rate;			// 명소 별점
	
	public PlaceInfoDto() {}

	public PlaceInfoDto(String address, String placeId, String store, String pic, double lat, double lng, int review, double rate) {
		super();
		this.address = address;
		this.placeId = placeId;
		this.store = store;
		this.pic = pic;
		this.lat = lat;
		this.lng = lng;
		this.review = review;
		this.rate = rate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public int getReview() {
		return review;
	}

	public void setReview(int review) {
		this.review = review;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
	
	
	
	

}
