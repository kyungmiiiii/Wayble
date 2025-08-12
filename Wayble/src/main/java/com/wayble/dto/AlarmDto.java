package com.wayble.dto;

public class AlarmDto {
	private int alarmIdx;   	// 알림 기본키 
	private int userIdx;    	// 알림 대상
	private int sendUserIdx;	// 알림 보낸 유저
	private String readYN;		// 알림 확인 여부
	private String origin;		// 알림 출처
	private String content;		// 내용
	private String createdTime;	// 알림 생성 시간
	
	public AlarmDto() {}

	public AlarmDto(int alarmIdx, int userIdx, int sendUserIdx, String readYN, String origin, String content, String createdTime) {
		this.alarmIdx = alarmIdx;
		this.userIdx = userIdx;
		this.sendUserIdx = sendUserIdx;
		this.readYN = readYN;
		this.origin = origin;
		this.content = content;
		this.createdTime = createdTime;
	}

	public int getAlarmIdx() {
		return alarmIdx;
	}

	public void setAlarmIdx(int alarmIdx) {
		this.alarmIdx = alarmIdx;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public int getSendUserIdx() {
		return sendUserIdx;
	}

	public void setSendUserIdx(int sendUserIdx) {
		this.sendUserIdx = sendUserIdx;
	}

	public String getReadYN() {
		return readYN;
	}

	public void setReadYN(String readYN) {
		this.readYN = readYN;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	
	
	
	
}