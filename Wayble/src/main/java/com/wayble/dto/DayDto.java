package com.wayble.dto;

public class DayDto {
	private int dayIdx;			// 여행 일차 기본키
	private int plannerIdx;		// 플래너 기본키

	public DayDto() {}
	public DayDto(int dayIdx, int plannerIdx) {
		this.dayIdx = dayIdx;
		this.plannerIdx = plannerIdx;
	}
	
	public int getDayIdx() {
		return dayIdx;
	}
	public void setDayIdx(int dayIdx) {
		this.dayIdx = dayIdx;
	}
	public int getPlannerIdx() {
		return plannerIdx;
	}
	public void setPlannerIdx(int plannerIdx) {
		this.plannerIdx = plannerIdx;
	}
	
	

	
	
	
}
