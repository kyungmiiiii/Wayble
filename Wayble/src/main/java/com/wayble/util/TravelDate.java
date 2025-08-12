package com.wayble.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TravelDate {
	// 날짜로 여행 일수 구하기
	// 파라미터 : 여행 시작일(start), 여행 종료일(end)
	// 리턴값 : 여행일수(diff)
	public static int main(String start, String end) {
		start = start.substring(0, 10);	
		end = end.substring(0, 10);	
		
		LocalDate startDate = LocalDate.parse(start);
	    LocalDate endDate = LocalDate.parse(end);
	    
	    long diff = ChronoUnit.DAYS.between(startDate, endDate);
	    return (int)++diff;
	}
}

	
