package com.wayble.service;

import java.util.List;

import com.wayble.dto.AlarmDto;
import com.wayble.dto.CityDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.UsersDto;

public interface CommonService {
	UsersDto getUserInfo(int userIdx);	// 유저정보 가져오기
	CityDto getCityDtoByCityIdx(int cityIdx);	// 도시정보 가져오기
	int checkAndSavePlace(PlaceDto dto);	// 위도,경도에 맞는 명소가져오기
	void saveUserPlace(int dayIdx, int placeIdx, int saveIdx);	// 유저플레이스 추가(선택된 명소 추가)
	int addChatAlarm(int userIdx, int sendUserIdx);	// 누가 채팅보냈는지 알림추가
	void deleteChatAlarm(int alarmIdx);	// 채팅알람삭제 (알림출처 = 채팅 인것)
	int addReplyAlarm(int sendUserIdx, int postIdx);	// 누가 댓글달았는지 알림추가
	List<AlarmDto> getAlarmDetailList(Integer loginUserIdx);	// 알람 시간표시
	void readAllAlarm(int userIdx);	// 알람 모두 읽음처리
	boolean checkUnreadAlarm(int userIdx); // 안 읽은 알림이 있나없나 체크
	
//	HJ----------------------------------------------------------------
	List<CityDto> getcityInfo();	// 메인화면에 도시 8개 보여주기
	int loginUserCheck(String email, String password);	//	로그인
	UsersDto getNicknameAndProfileFromIdx(int userIdx);	// 닉네임+프로필사진 가져오기
	void addSignUp(String email, String password, String nickname);	// 회원가입
	int updatePw(String password, String newPassword, int userIdx);	// 비밀번호 변경
	int updateNickname(String newNickname, int userIdx);	// 닉네임 변경
	int updateProfile(String profile, int userIdx);	// 프로필사진 변경
	CityDto getCityInfoSortFromIdx(int cityIdx);	// 도시 정보가져오기
	List<CityDto> getSearchCityFromKeyword(String search);	// 메인화면에 검색한 도시 최대 8개 보여주기
	List<String> getUsersNickname();	// 모든유저들의 닉네임 가져오기
	
	CityDto cityinfoFromPlannerIdx(int plannerIdx);	//	도시 정보가져오기
//	KM-----------------------------------------------------------------
	UsersDto getUserByEmail(String email);	// 이메일로 유저정보 가져오기
	
}
