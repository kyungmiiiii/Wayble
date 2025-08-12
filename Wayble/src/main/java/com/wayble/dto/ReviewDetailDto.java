package com.wayble.dto;

public class ReviewDetailDto {
	private String profile;		// 프로필
	private String nickname;	// 닉네임
	private int rate;			// 별점
	private String content;		// 댓글 내용
	private int reviewIdx;		// 리뷰 기본키
	private int plannerIdx;		// 플래너 기본키
	private int userIdx;		// 유저기본키
	
	public ReviewDetailDto() { }
	public ReviewDetailDto(String profile, String nickname, int rate, String content, int reviewIdx, int plannerIdx,
			int userIdx) {
		this.profile = profile;
		this.nickname = nickname;
		this.rate = rate;
		this.content = content;
		this.reviewIdx = reviewIdx;
		this.plannerIdx = plannerIdx;
		this.userIdx = userIdx;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getReviewIdx() {
		return reviewIdx;
	}

	public void setReviewIdx(int reviewIdx) {
		this.reviewIdx = reviewIdx;
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
	
	
	
	
}
