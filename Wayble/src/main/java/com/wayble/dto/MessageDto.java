package com.wayble.dto;

public class MessageDto {
	private int messageIdx;		// 메시지 기본키
	private int chatRoomIdx;	// 채팅방 기본키
	private int userIdx;		// 보낸 유저 기본키
	private String sendTime;	// 보낸 시간
	private String content;		// 메시지 내용
	
	public MessageDto( ) {}
	public MessageDto(int messageIdx, int chatRoomIdx, int userIdx, String sendTime, String content) {
		this.messageIdx = messageIdx; this.chatRoomIdx = chatRoomIdx;
		this.userIdx = userIdx; this.sendTime = sendTime;
		this.content = content;
	}

	public int getMessageIdx() {
		return messageIdx;
	}

	public void setMessageIdx(int messageIdx) {
		this.messageIdx = messageIdx;
	}

	public int getChatRoomIdx() {
		return chatRoomIdx;
	}

	public void setChatRoomIdx(int chatRoomIdx) {
		this.chatRoomIdx = chatRoomIdx;
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

