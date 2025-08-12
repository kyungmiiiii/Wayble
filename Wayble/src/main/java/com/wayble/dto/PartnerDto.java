package com.wayble.dto;

public class PartnerDto {
	private int plannerIdx;		// 플래너 기본키
	private int userIdx;		// 동행인 유저 기본키
	private String joined;		// 수락여부
	private String code;		// 인증코드
	
	public PartnerDto() { }
	public PartnerDto(int plannerIdx, int userIdx, String joined, String code) {
		this.plannerIdx = plannerIdx; this.userIdx = userIdx;
		this.joined = joined; this.code = code;
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
	public String getJoined() {
		return joined;
	}
	public void setJoined(String joined) {
		this.joined = joined;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	} 
	
	
}
