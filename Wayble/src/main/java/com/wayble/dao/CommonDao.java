package com.wayble.dao;

import java.util.List;

import com.wayble.dto.AlarmDto;
import com.wayble.dto.CityDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.UsersDto;

public interface CommonDao {
// 	작업자 : 서수련 ---------------------------------------------------------------------------
	UsersDto getUserInfo(int userIdx); // 유저인덱스 받아서 유저정보 가져오기
	int checkPlaceByLoc(double lat, double lng); // 위도경도에 맞는 명소인덱스 가져오기
	int savePlace(PlaceDto dto); //	명소 저장
	void saveUserPlace(int dayIdx, int placeIdx, int saveIdx); // 유저플레이스 추가(선택된 명소 추가)
	int addAlarm(AlarmDto dto); // 알람추가
	void deleteChatAlarm(int sendUserIdx); // 보낸유저 인덱스로 채팅알람삭제 (알림출처 = 채팅 인것)
	List<AlarmDto> getAlarmDetailList(Integer loginUserIdx); // 유저인덱스로 알람정보 가져오기(내림차순)
	void readAllAlarm(int userIdx);	// 유저인덱스의 알람 모두 읽음처리
	boolean checkUnreadAlarm(int userIdx); // 안 읽은 알림이 있나없나 체크
	CityDto getCityDtoByCityIdx(int cityIdx); // 도시인덱스로 도시정보 가져오기

// 	작업자 : 이경미 ---------------------------------------------------------------------------
	UsersDto getUserByEmail(String email); // 이메일로 유저정보 가져오기
	
// 	작업자 : 최호준 ---------------------------------------------------------------------------
	List<CityDto> getcityList(); // 전체 도시 정보 가져오기
	CityDto getcityInfoSort(int cityIdx); // 도시인덱스로 맞는 도시 정보 가져오기
	int checkLogin(String email, String password); // 로그인
	void doSignUp(String email, String password, String nickname); // 회원가입
	int doUpdatePassword(String password, String newPassword,int userIdx); // 비밀번호변경
	int doUpdateNickname(String newNickname, int userIdx); // 닉네임변경
	int doUpdateProfile(String profile, int userIdx); // 프로필사진 변경
	CityDto getCityInfoSort(int cityIdx); // 도시인덱스로 맞는 도시 정보 가져오기	(중복인듯)
	List<CityDto> getSearchCity(String search); // 해당 글자가 들어간 도시정보 가져오기(홈화면 검색)
	List<UsersDto> getUsers(); // 유저들의 전체정보 가져오기
	CityDto cityinfoFromPlannerIdx(int plannerIdx); // 플래너인덱스로 도시 정보가져오기 (플래너리뷰에서 사용)
	PlaceDto getPlaceDetail(int userPlaceIdx); // 유저플레이스인덱스(선택된명소)로 명소정보 가져오기
}