package com.wayble.dto;

import java.util.Date;

public class PlannerDetailDto {
	private int plannerIdx;			// 플래너 기본키
	private int userIdx;			// 만든 유저 기본키
	private int cityIdx;			// 도시 기본키
	private String title;			// 플래너 제목
	private Date startDate;			// 시작일
	private Date endDate;			// 종료일
	private String completeYN;		// 작성완료
	
	private String nickname;		// 닉네임
	private String profile;			// 프로필
	
	public PlannerDetailDto() { }

	public PlannerDetailDto(int plannerIdx, int userIdx, int cityIdx, String title, Date startDate, Date endDate,
			String completeYN, String nickname, String profile) {
		this.plannerIdx = plannerIdx;
		this.userIdx = userIdx;
		this.cityIdx = cityIdx;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.completeYN = completeYN;
		this.nickname = nickname;
		this.profile = profile;
	}

	public int getPlannerIdx() {
		return plannerIdx;
	}

	public void setPlannerIdx(int plannerIdx) {
		this.plannerIdx = plannerIdx;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public int getCityIdx() {
		return cityIdx;
	}

	public void setCityIdx(int cityIdx) {
		this.cityIdx = cityIdx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCompleteYN() {
		return completeYN;
	}

	public void setCompleteYN(String completeYN) {
		this.completeYN = completeYN;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
}