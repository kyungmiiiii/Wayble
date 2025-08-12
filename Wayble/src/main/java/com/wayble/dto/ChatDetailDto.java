package com.wayble.dto;

public class ChatDetailDto {
	private int chatRoomIdx;		// 채팅방 기본키
	private int captainUserIdx;		// 방장 유저 기본키
	private String name;			// 채팅방 이름
	private String category;		// 채팅방 유형
	private String description;		// 채팅방 설명
	private int limit;				// 최대인원
	private int currJoin;			// 현재 참여 인원
	
	public ChatDetailDto() {}
	public ChatDetailDto(int chatRoomIdx, int captainUserIdx, String name, String category, String description, int limit, int currJoin) {
		this.chatRoomIdx = chatRoomIdx;
		this.captainUserIdx = captainUserIdx;
		this.name = name;
		this.category = category;
		this.description = description;
		this.limit = limit;
		this.currJoin = currJoin;
	}

	public int getChatRoomIdx() {
		return chatRoomIdx;
	}

	public void setChatRoomIdx(int chatRoomIdx) {
		this.chatRoomIdx = chatRoomIdx;
	}

	public int getCaptainUserIdx() {
		return captainUserIdx;
	}

	public void setCaptainUserIdx(int captainUserIdx) {
		this.captainUserIdx = captainUserIdx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getCurrJoin() {
		return currJoin;
	}

	public void setCurrJoin(int currJoin) {
		this.currJoin = currJoin;
	}
	
	
	
	

}
