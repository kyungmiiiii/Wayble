package com.wayble.dto;

public class PrivateChatUserDto {
	private int userIdx;		// 유저 기본키
	private String nickname;	// 닉네임
	private String profile;		// 프로필
	private int chatRoomIdx;	// 채팅방 기본키
	
	public PrivateChatUserDto() {};
	public PrivateChatUserDto(int userIdx, String nickname, String profile, int chatRoomIdx) {
		this.userIdx = userIdx;
		this.nickname = nickname;
		this.profile = profile;
		this.chatRoomIdx = chatRoomIdx;
	}
	
	public int getUserIdx() {
		return userIdx;
	}
	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
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
	public int getChatRoomIdx() {
		return chatRoomIdx;
	}
	public void setChatRoomIdx(int chatRoomIdx) {
		this.chatRoomIdx = chatRoomIdx;
	}
	
	
	
	

}
