package com.wayble.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wayble.dto.AlarmDto;
import com.wayble.dto.CityDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.UsersDto;

@Repository
public class CommonDaoImpl implements CommonDao {
	@Autowired
	SqlSession sqlSession;
	
// 	작업자 : 서수련 -----------------------------------------------------------------------
	// 유저인덱스 받아서 유저정보 가져오기
	// 파라미터 : 유저기본키(userIdx)
	// 리턴값 : 유저 정보(UsersDto)
	@Override
	public UsersDto getUserInfo(int userIdx) {
		return sqlSession.selectOne("CommonMapper.getUserInfo", userIdx);
	}
	
	// 위도 경도에 맞는 명소기본키 가져오기
	// 파라미터 : 위도(lat) ,경도(lng)
	// 리턴값 : 명소 기본키
	@Override
	public int checkPlaceByLoc(double lat, double lng) {
		HashMap<String, Double> map = new HashMap<>();
		map.put("lat", lat);
		map.put("lng", lng);
		Integer plannerIdx = sqlSession.selectOne("CommonMapper.checkPlaceByLoc", map);
		if(plannerIdx == null) return 0;
		return plannerIdx;
	}
	
	// 명소 저장
	// 파라미터 : 플래너 테이블(dto)
	// 리턴값 : 명소 기본키
	@Override
	public int savePlace(PlaceDto dto) {
		sqlSession.insert("CommonMapper.savePlace", dto);
		return dto.getPlaceIdx();
	}
	
	// 유저플레이스 추가(선택된 명소 추가)
	// 파라미터 : 여행일차 기본키(dayIdx), 명소 기본키(placeIdx), 명소별 저장 순서(saveIdx)
	// 리턴값 : -
	@Override
	public void saveUserPlace(int dayIdx, int placeIdx, int saveIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("dayIdx", dayIdx);
		map.put("placeIdx", placeIdx);
		map.put("saveIdx", saveIdx);
		sqlSession.insert("CommonMapper.saveUserPlace", map);
	}

	// 알람추가
	// 파라미터 : AlarmDto
	// 리턴값 : 알림 기본키
	@Override
	public int addAlarm(AlarmDto dto) {
		sqlSession.insert("CommonMapper.addAlarm", dto);
		return dto.getAlarmIdx();
	}
	
	// 채팅알람삭제 (알림출처 = 채팅 인것)
	// 파라미터 : 유저 기본키(sendUserIdx)
	// 리턴값 : -
	@Override
	public void deleteChatAlarm(int sendUserIdx) {
		sqlSession.delete("CommonMapper.deleteChatAlarm", sendUserIdx);
	}
	
	// 알람정보 가져오기(내림차순)
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : 알림 정보(AlarmDto)
	@Override
	public List<AlarmDto> getAlarmDetailList(Integer loginUserIdx) {
		List<AlarmDto> listRet = sqlSession.selectList("CommonMapper.alarmList", loginUserIdx);
		return listRet;
	}
	
	// 알람 모두 읽음처리
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void readAllAlarm(int userIdx) {
		sqlSession.update("CommonMapper.readAlarm", userIdx);
	}
	
	// 안 읽은 알림이 있나없나 체크
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 안 읽은 알림 유무 
	@Override
	public boolean checkUnreadAlarm(int userIdx) {
		int unread = sqlSession.selectOne("CommonMapper.checkUnreadAlarm", userIdx);
		if(unread != 0) return true;
		return false;
	}
	
	// 검색한 도시정보 가져오기
	// 파라미터 : 도시기본키(cityIdx)
	// 리턴값 : 도시정보(CityDto)
	@Override
	public CityDto getCityDtoByCityIdx(int cityIdx) {
		return sqlSession.selectOne("CommonMapper.getCityDtoByCityIdx", cityIdx);
	}

//	작업자 : 이경미 ----------------------------------------------------------------------------
	
	// 유저정보 가져오기
	// 파라미터 : 이메일(email)
	// 리턴값 : 유저 정보(UsersDto)
	@Override
	public UsersDto getUserByEmail(String email) {
		return sqlSession.selectOne("CommonMapper.getUserByEmail", email);
	}
	
//	작업자 : 최호준 ----------------------------------------------------------------------------
	
	// 전체 도시 정보 가져오기
	// 파라미터 : -
	// 리턴값 : [CityDto](도시 정보)
	@Override
	public List<CityDto> getcityList() {
		return sqlSession.selectList("CommonMapper.cityInfo");
	}
	
	// 맞는 도시 정보 가져오기
	// 파라미터 : 도시 기본키(cityIdx)
	// 리턴값 : CityDto(도시 정보)
	@Override
	public CityDto getcityInfoSort(int cityIdx) {
		return sqlSession.selectOne("CommonMapper.cityInfoSort", cityIdx);
	}
	
	// 로그인
	// 파라미터 : 이메일(email), 비밀번호(password)
	// 리턴값 : 유저 기본키
	@Override
	public int checkLogin(String email, String password) {   
		HashMap<String,String> map1 = new HashMap<>();
		map1.put("email", email);
		map1.put("password", password);
		Integer userIdx =  sqlSession.selectOne("CommonMapper.loginUsers",map1);
		if(userIdx == null) userIdx = -1;
		return userIdx;
	}
	
	// 회원가입
	// 파라미터 : 이메일(email), 비밀번호(password), 닉네임(nickname)
	// 리턴값 : -
	@Override
    public void doSignUp(String email, String password, String nickname) {
		HashMap<String,String> map = new HashMap<>();
		map.put("email", email);
		map.put("nickname", nickname);
		map.put("password", password);
		sqlSession.insert("CommonMapper.signUp",map);
	}
	
	// 비밀번호 변경
	// 파라미터 : 비밀번호(password), 새로운비밀번호(newPassword), 유저 기본키(userIdx)
	// 리턴값 : 영향 받은 행의 개수 ????????????????????
	@Override
	public int doUpdatePassword(String password, String newPassword,int userIdx) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("password", password);
		map.put("newPassword", newPassword);
		map.put("userIdx", userIdx);
		return sqlSession.update("CommonMapper.updatePassword",map);
	}
	
	// 닉네임 변경
	// 파라미터 : 새로운 닉네임(newNickname), 유저 기본키(userIdx)
	// 리턴값 : 영향 받은 행의 개수 ??????????????????????????
	@Override
	public int doUpdateNickname(String newNickname, int userIdx) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("newNickname", newNickname);
		map.put("userIdx", userIdx);
		return sqlSession.update("CommonMapper.updateNickname",map);
	}
	
	// 프로필사진 변경
	// 파라미터 : 프로필사진(profile), 유저기본키(userIdx)
	// 리턴값 : 영향 받은 행의 개수 ??????????????????????????
	@Override
	public int doUpdateProfile(String profile, int userIdx) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("profile", profile);
		map.put("userIdx", userIdx);
		return sqlSession.update("CommonMapper.updateProfile",map);
	}
	
	// 검색한 도시 정보 가져오기
	// 파라미터 : 도시 기본키(cityIdx)
	// 리턴값 : 도시 정보(CityDto)
	@Override
	public CityDto getCityInfoSort(int cityIdx) {
		return sqlSession.selectOne("CommonMapper.cityInfoSort", cityIdx);
	}
	
	// 해당하는 글자가 들어간 도시정보 가져오기
	// 파라미터 : 검색어(search)
	// 리턴값 : [도시 정보](CityDto)
	@Override
	public List<CityDto> getSearchCity(String search) {
		search = "%"+search+"%";
		return sqlSession.selectList("CommonMapper.searchCitylist",search);
	}
	
	// 유저들의 전체정보 가져오기
	// 파라미터 : -
	// 리턴값 : [유저 정보](UsersDto)
	@Override
	public List<UsersDto> getUsers() {
		return sqlSession.selectList("CommonMapper.usersInfo");
	}
	
	// 도시 정보가져오기 (플래너리뷰에서 사용)
	// 파라미터 : 플래너 기본키(plannerIdx)
	// 리턴값 : 유저 정보(UsersDto)
	@Override
	public CityDto cityinfoFromPlannerIdx(int plannerIdx) {
		return sqlSession.selectOne("CommonMapper.cityinfoFromPlannerIdx",plannerIdx);
	}
	
	// 명소정보 가져오기
	// 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : 명소 정보(PlaceDto)
	@Override
	public PlaceDto getPlaceDetail(int userPlaceIdx) {
	    return sqlSession.selectOne("CommonMapper.getPlaceDetail",userPlaceIdx);
	}

}
