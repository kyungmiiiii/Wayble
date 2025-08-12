package com.wayble.dto;

public class CityDto {
	private int cityIdx;			// 도시 기본키
	private String country;			// 나라 이름
	private String name;			// 도시 이름
	private String description;		// 도시 설명
	private String pic;				// 사진
	private double lat;				// 도시 중심 위도
	private double lng;				// 도시 중심 경도
	private String nameEng;			// 도시 영어 이름
	
	public CityDto() {}
	public CityDto(int cityIdx, String country, String name, String description, String pic, double lat, double lng, String nameEng) {
		super();
		this.cityIdx = cityIdx;
		this.country = country;
		this.name = name;
		this.description = description;
		this.pic = pic;
		this.lat = lat;
		this.lng = lng;
		this.nameEng = nameEng;
	}

	public int getCityIdx() {
		return cityIdx;
	}

	public void setCityIdx(int cityIdx) {
		this.cityIdx = cityIdx;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getNameEng() {
		return nameEng;
	}

	public void setNameEng(String nameEng) {
		this.nameEng = nameEng;
	}

}
