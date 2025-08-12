package com.wayble.util;

public class Distance {
	// 두 장소 사이의 거리 구하기 
	// 파라미터 : 명소1 위도(lat1), 명소1 경도(lng), 명소2 위도(lat2), 명소2 경도(lng2)
	// 리턴값 : 거리
	public static double convertLatLngToMeter(double lat1, double lng1, double lat2, double lng2) {  // lat : 위도, lng : 경도
		final double r = 6371000;  // 지구 반지름
		double radianOfLat1 = Math.toRadians(lat1);
		double radianOfLat2 = Math.toRadians(lat2);
		double diffLat = Math.toRadians(lat2 - lat1);
		double diffLng = Math.toRadians(lng2 - lng1);
		
		double v1 = Math.sin(diffLat/2) * Math.sin(diffLat/2)
				+ (Math.cos(radianOfLat1) * Math.cos(radianOfLat2)
						* Math.sin(diffLng/2) * Math.sin(diffLng/2));
		double v2 = 2 * Math.atan2(Math.sqrt(v1), Math.sqrt(1-v1));
		return Math.round((r * v2) / 1000.0);
	}
	
	public static void main(String[] args) {
		String travelDate = "07/10/2025 - 07/12/2025";
		String[] start = travelDate.split(" - ")[0].split("/");
		String[] end = travelDate.split(" - ")[1].split("/");
		String startDate = start[2] + "-" + start[0] + "-" + start[1];
		String endDate = end[2] + "-" + end[0] + "-" + end[1];
		System.out.println(startDate + " ~ " + endDate);
	}
}
