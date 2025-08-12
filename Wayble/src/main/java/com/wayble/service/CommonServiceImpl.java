package com.wayble.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wayble.dao.CommonDao;
import com.wayble.dao.PlannerDao;
import com.wayble.dao.SnsDao;
import com.wayble.dto.AlarmDto;
import com.wayble.dto.CityDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.UsersDto;

@Service
public class CommonServiceImpl implements CommonService{
	@Autowired
	CommonDao coDao;
	@Autowired
	PlannerDao pDao;
	@Autowired
	SnsDao sDao;
	
	// 유저정보 가져오기
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : 유저정보(UsersDto)
	@Override
	public UsersDto getUserInfo(int userIdx) {
		return coDao.getUserInfo(userIdx);
	}
	
	// 도시정보 가져오기
	// 파라미터 : 도시 기본키(cityIdx)
	// 리턴값 : 도시정보(CityDto)
	@Override
	public CityDto getCityDtoByCityIdx(int cityIdx) {
		return coDao.getCityDtoByCityIdx(cityIdx);
	}
	
	// 위도,경도에 맞는 명소가져오기
	// 파라미터 : PlaceDto
	// 리턴값 : 명소 기본키(placeIdx)
	@Override
	public int checkAndSavePlace(PlaceDto dto) {
		int placeIdx = coDao.checkPlaceByLoc(dto.getLat(), dto.getLng());
		if(placeIdx == 0) {
			placeIdx = coDao.savePlace(dto);
		}
		return placeIdx;
	}
	
	// 유저플레이스 추가(선택된 명소 추가)
	// 파라미터 : 여행일차 기본키(dayIdx)	, 명소 기본키(placeIdx), 명소별 저장 순서(saveIdx)
	// 리턴값 :-
	@Override
	public void saveUserPlace(int dayIdx, int placeIdx, int saveIdx) {
		coDao.saveUserPlace(dayIdx, placeIdx, saveIdx);
	}
	
	// 누가 채팅보냈는지 알림추가
	// 파라미터 : 유저 기본키(userIdx), 보낸유저 기본키(sendUserIdx)
	// 리턴값 : 알림 기본키(alarmIdx)
	@Override
	public int addChatAlarm(int userIdx, int sendUserIdx) {
		AlarmDto dto = new AlarmDto();
		String nickname = coDao.getUserInfo(sendUserIdx).getNickname();
		dto.setUserIdx(userIdx);
		dto.setContent(nickname + "님이 새 메시지를 보냈습니다.");
		dto.setOrigin("채팅");
		dto.setSendUserIdx(sendUserIdx);
		coDao.addAlarm(dto);
		return dto.getAlarmIdx();
	}
	
	// 채팅알람삭제 (알림출처 = 채팅 인것)
	// 파라미터 : 보낸유저 기본키(sendUserIdx)
	// 리턴값 : -
	@Override
	public void deleteChatAlarm(int sendUserIdx) {
		coDao.deleteChatAlarm(sendUserIdx);
	}
	
	// 누가 댓글달았는지 알림추가
	// 파라미터 : 보낸유저 기본키(sendUserIdx), 게시물 기본키(postIdx)
	// 리턴값 : 게시물 주인 유저 기본키(userIdx)
	@Override
	public int addReplyAlarm(int sendUserIdx, int postIdx) {
		AlarmDto dto = new AlarmDto();
		String nickname = coDao.getUserInfo(sendUserIdx).getNickname();
		int userIdx = sDao.getPostDtoByPostIdx(postIdx).getUserIdx();
		dto.setUserIdx(userIdx);
		dto.setContent(nickname + "님이 게시글에 댓글을 남겼습니다.");
		dto.setOrigin("댓글");
		dto.setSendUserIdx(sendUserIdx);
		coDao.addAlarm(dto);
		return userIdx;
	}
	
	// 알람 시간표시
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx)
	// 리턴값 : [알림 정보(AlarmDto)]
	@Override
	public List<AlarmDto> getAlarmDetailList(Integer loginUserIdx) {
		 List<AlarmDto> list1 = coDao.getAlarmDetailList(loginUserIdx);
	      
	      for(AlarmDto dto : list1) {
	         String myTime = dto.getCreatedTime();
	         // 날짜계산 / 시간계산
	         System.out.println(myTime);      // "2025-04-26 16:07:00.0"
	         int year = Integer.parseInt(myTime.substring(0, 4));
	         int month = Integer.parseInt(myTime.substring(5, 7));
	         int day = Integer.parseInt(myTime.substring(8, 10));
	         int hour = Integer.parseInt(myTime.substring(11, 13));
	         int minute = Integer.parseInt(myTime.substring(14, 16));
	         LocalDateTime alarmTime = LocalDateTime.of(year, month, day, hour, minute);

	         // alarmTime vs now() ----> __분 전, __시간 전, ___일 전
	         long days = ChronoUnit.DAYS.between(alarmTime, LocalDateTime.now());
	         long hours = ChronoUnit.HOURS.between(alarmTime, LocalDateTime.now());
	         long minutes = ChronoUnit.MINUTES.between(alarmTime, LocalDateTime.now());

	         if(days>=1) {
	            myTime = days + "일 전";
	         } else if(hours>=1) {
	            myTime = hours + "시간 전";
	         } else {
	            myTime = minutes + "분 전";
	         }
	         dto.setCreatedTime(myTime);
	      }
	      return list1;
	}
	
	// 알람 모두 읽음처리
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void readAllAlarm(int userIdx) {
		coDao.readAllAlarm(userIdx);
	}
	
	// 안 읽은 알림이 있나없나 체크
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 있으면 true, 없으면 false
	@Override
	public boolean checkUnreadAlarm(int userIdx) {
		return coDao.checkUnreadAlarm(userIdx);
	}

//	작업자 : 이경미 ---------------------------------------------------------------------------------------------------
	
	// 이메일로 유저정보 가져오기
	// 파라미터 : 이메일(email)
	// 리턴값 : [유저정보(UsersDto)]
	@Override
	public UsersDto getUserByEmail(String email) {
		System.out.println("DB 조회할 이메일: " + email);
		UsersDto user = coDao.getUserByEmail(email);
		System.out.println("조회 결과: " + user);
		return user;
	}
	
//	작업자 : 최호준 ---------------------------------------------------------------------------------------------------
	
	// 메인화면에 도시 8개 보여주기
	// 파라미터 : -
	// 리턴값 : [도시정보(CityDto)]
	@Override
	public List<CityDto> getcityInfo() {      // 메인화면 8개 뿌려주기
		List<CityDto> cityList = coDao.getcityList();   // 전체 cityDto가 나옴
		List<CityDto> ret = new ArrayList<>();   // cityDto를 담아줄 목적으로 만든거   
		for(int i=0; i<8; i++) {   
			ret.add(cityList.get(i));
		}
		return ret;
	}
	
	// 로그인
	// 파라미터 : 이메일(email), 비밀번호(password)
	// 리턴값 : 유저 기본키(userIdx)
	@Override
	public int loginUserCheck(String email, String password) {
		return coDao.checkLogin(email, password);
	}
	
	// 닉네임+프로필사진 가져오기
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : 유저정보(UsersDto)
	@Override
	public UsersDto getNicknameAndProfileFromIdx(int userIdx) {
		return coDao.getUserInfo(userIdx);
	}
	
	// 회원가입
	// 파라미터 : 이메일,비밀번호,닉네임
	// 리턴값 : -
	@Override
	public void addSignUp(String email, String password, String nickname) {   // 비밀번호 몇글자이상, 특수문자 이런거 추가하기!
		coDao.doSignUp(email, password, nickname);
	}
	
	// 비밀번호 변경
	// 파라미터 : 비밀번호(password), 새로운비밀번호(newPassword), 유저 기본키(userIdx)
	// 리턴값 : 영향받은 행의 개수 
	@Override
	public int updatePw(String password, String newPassword, int userIdx) {
		return coDao.doUpdatePassword(password, newPassword, userIdx);
	}
	
	// 닉네임 변경
	// 파라미터 : 새로운닉네임(newNickname), 유적 기본키(userIdx)
	// 리턴값 : 영향받은 행의 개수 
	@Override
	public int updateNickname(String newNickname, int userIdx) {
		return coDao.doUpdateNickname(newNickname, userIdx);
	}
	
	// 프로필사진 변경
	// 파라미터 : 프로필사진(profile), 유저 기본키(userIdx)
	// 리턴값 : 영향받은 행의 개수 
	@Override
	public int updateProfile(String profile, int userIdx) {
		return coDao.doUpdateProfile(profile, userIdx);
	}
	
	// 도시 정보가져오기
	// 파라미터 : 도시 기본키(cityIdx)
	// 리턴값 : 도시정보(CityDto)
	@Override
	public CityDto getCityInfoSortFromIdx(int cityIdx) {
		return coDao.getCityInfoSort(cityIdx);
	}
	
	// 메인화면에 검색한 도시 최대 8개 보여주기
	// 파라미터 : 검색어(도시 or 나라이름)(search)
	// 리턴값 : [도시정보(CityDto)]
	@Override
	public List<CityDto> getSearchCityFromKeyword(String search) {
		List<CityDto> resultCity = coDao.getSearchCity(search);   // 검색한거가 전체가 나옴
		List<CityDto> ret = new ArrayList<>();   // 
		for(int i=0; i<resultCity.size(); i++) { 
			if(i == 8) break;
			ret.add(resultCity.get(i));
		}
		return ret;
	}
	
	// 모든유저들의 닉네임 가져오기
	// 파라미터 : -
	// 리턴값 : [닉네임(userNickname)]
	@Override
	public List<String> getUsersNickname() {
		List<String> userNickname = new ArrayList<>();
		for(UsersDto users : coDao.getUsers()) {
			userNickname.add(users.getNickname());
		}
		return userNickname;
	}
	
	// 도시 정보가져오기
	// 파라미터 : 플래너 기본키(plannerIdx)
	// 리턴값 : [도시정보(CityDto)]
	@Override
	public CityDto cityinfoFromPlannerIdx(int plannerIdx) {
		return coDao.cityinfoFromPlannerIdx(plannerIdx);
	}
}
