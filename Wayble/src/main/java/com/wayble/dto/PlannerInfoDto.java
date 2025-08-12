package com.wayble.dto;

public class PlannerInfoDto {
	private String country;			// 나라 이름
	private String city;			// 도시
	private String title;			// 플래너 제목
	private String startDate;		// 시작일
	private String endDate;			// 종료일
	
	public PlannerInfoDto() {}
	public PlannerInfoDto(String country, String city, String title, String startDate, String endDate) {
		this.country = country;
		this.city = city;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	
	

}
