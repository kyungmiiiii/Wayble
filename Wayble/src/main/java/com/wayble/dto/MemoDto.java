package com.wayble.dto;

public class MemoDto {
	private int userPlaceIdx;	// 선택된 명소 기본키
	private int userIdx;		// 최근 수정한 유저 기본키
	private String content;		// 메모
	private String nickname;	// 닉네임
	
	public MemoDto() { }
	public MemoDto(int userPlaceIdx, int userIdx, String content, String nickname) {
		this.userPlaceIdx = userPlaceIdx;
		this.userIdx = userIdx;
		this.content = content;
		this.nickname = nickname;
	}

	public int getUserPlaceIdx() {
		return userPlaceIdx;
	}

	public void setUserPlaceIdx(int userPlaceIdx) {
		this.userPlaceIdx = userPlaceIdx;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
