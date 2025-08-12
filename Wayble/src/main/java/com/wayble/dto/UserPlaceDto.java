package com.wayble.dto;

public class UserPlaceDto {
	private int userPlaceIdx;		// 선택된 명소 기본키
	private int dayIdx;				// 여행 일차 기본키
	private int placeIdx;			// 명소 기본키
	private int saveIdx;			// 명소별 저장 순서
	
	public UserPlaceDto() { }
	public UserPlaceDto(int userPlaceIdx, int dayIdx, int placeIdx, int saveIdx) {
		this.userPlaceIdx = userPlaceIdx;
		this.dayIdx = dayIdx;
		this.placeIdx = placeIdx;
		this.saveIdx = saveIdx;
	}
	public int getUserPlaceIdx() {
		return userPlaceIdx;
	}
	public void setUserPlaceIdx(int userPlaceIdx) {
		this.userPlaceIdx = userPlaceIdx;
	}
	public int getDayIdx() {
		return dayIdx;
	}
	public void setDayIdx(int dayIdx) {
		this.dayIdx = dayIdx;
	}
	public int getPlaceIdx() {
		return placeIdx;
	}
	public void setPlaceIdx(int placeIdx) {
		this.placeIdx = placeIdx;
	}
	public int getSaveIdx() {
		return saveIdx;
	}
	public void setSaveIdx(int saveIdx) {
		this.saveIdx = saveIdx;
	}
}
	