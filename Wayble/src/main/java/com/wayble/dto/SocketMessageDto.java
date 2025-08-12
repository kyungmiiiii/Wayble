package com.wayble.dto;

public class SocketMessageDto {
    private int sender;					// 보낸 유저 기본키
    private String content;				// 메시지 내용
    private int chatRoomIdx;			// 채팅방 기본키
    private int messageIdx;				// 메시지 기본키
    private String senderProfile;		// 보낸 유저 프로필
    private String senderNickname;		// 보낸 유저 닉네임
    private String type;				// 소켓 메시지 타입
    
    public SocketMessageDto() {}

	public SocketMessageDto(int sender, String content, int chatRoomIdx, int messageIdx, String senderProfile,
			String senderNickname, String type) {
		this.sender = sender;
		this.content = content;
		this.chatRoomIdx = chatRoomIdx;
		this.messageIdx = messageIdx;
		this.senderProfile = senderProfile;
		this.senderNickname = senderNickname;
		this.type = type;
	}

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getChatRoomIdx() {
		return chatRoomIdx;
	}

	public void setChatRoomIdx(int chatRoomIdx) {
		this.chatRoomIdx = chatRoomIdx;
	}

	public int getMessageIdx() {
		return messageIdx;
	}

	public void setMessageIdx(int messageIdx) {
		this.messageIdx = messageIdx;
	}

	public String getSenderProfile() {
		return senderProfile;
	}

	public void setSenderProfile(String senderProfile) {
		this.senderProfile = senderProfile;
	}

	public String getSenderNickname() {
		return senderNickname;
	}

	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    


}
