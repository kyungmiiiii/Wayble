package com.wayble.dto;

public class PartnerInfoDto {
	private int plannerIdx;		// 플래너 기본키
	private String nickname;	// 닉네임
	private String profile;		// 프로필
	private int userIdx;		// 유저 기본키
	
	public PartnerInfoDto() { }
	public PartnerInfoDto(int plannerIdx, String nickname, String profile, int userIdx) {
		this.plannerIdx = plannerIdx;
		this.nickname = nickname;
		this.profile = profile;
		this.userIdx = userIdx;
	}

	public int getPlannerIdx() {
		return plannerIdx;
	}

	public void setPlannerIdx(int plannerIdx) {
		this.plannerIdx = plannerIdx;
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

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}
	
	
	
	
}
