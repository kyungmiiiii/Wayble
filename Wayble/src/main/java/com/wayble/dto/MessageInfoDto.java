package com.wayble.dto;

public class MessageInfoDto {
	private String name;		// 채팅방 이름
	private String profile;		// 프로필
	private String nickname;	// 닉네임
	private int userIdx;		// 유저 기본키
	private String sendTime;	// 보낸 시간
	private String content;		// 메시지 내용
	
	public MessageInfoDto() {}
	public MessageInfoDto(String name, String profile, String nickname, int userIdx, String sendTime, String content) {
		this.name = name;
		this.profile = profile;
		this.nickname = nickname;
		this.userIdx = userIdx;
		this.sendTime = sendTime;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	

	
	
	

}
