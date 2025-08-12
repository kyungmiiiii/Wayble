package com.wayble.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wayble.dto.CityDto;
import com.wayble.dto.DayDto;
import com.wayble.dto.MemoDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PartnerInfoDto;
import com.wayble.dto.PlannerDetailDto;
import com.wayble.dto.PlannerDto;
import com.wayble.dto.PlannerInfoDto;
import com.wayble.dto.ReviewDetailDto;
import com.wayble.dto.UserPlaceDto;
import com.wayble.dto.UserPlaceNameDto;

public interface PlannerDao {
//	작업자 : 서수련 --------------------------------------------------------------	
	int startPlanner(int userIdx, int cityIdx, String travelDate); // 플래너 시작
	PlannerInfoDto getPlannerInfoDto(int plannerIdx); // 플래너 정보 가져오기
	int generateDayIdx(int plannerIdx); // 여행 일차 만큼 dayIdx 만들기
	
//	작업자 : 이경미 --------------------------------------------------------------
	PlannerDetailDto getPlannerDetailByIdx(int pIdx); // 플래너 전체 리스트
	CityDto getCityDetail(int cIdx); // 도시 상세 보여주기
	List<DayDto> getDaysByPlannerIdx(int pIdx); // 플래너별 일차 가져오기
	List<UserPlaceNameDto> getPlacesByDayIdx(int dayIdx); // 일차별로 선택된 명소 띄워주기
	List<PartnerInfoDto> getPartnerInfoList(int plannerIdx); // 동행인 리스트
	int deletePartner(int pIdx, int uIdx); // 동행인 삭제
	void addPartner(PartnerDto dto); // 동행인 추가
	int updateTitle(String title, int plannerIdx); // 플래너 제목 수정
	List<MemoDto> getMemoDetailList(int userPlaceIdx); // 명소별 작성된 메모 상세 보여주기
	boolean memoExists(int userPlaceIdx, int userIdx); // 메모 존재 여부 확인
	int insertMemo(int userPlaceIdx, int userIdx, String content); // 메모 작성
	int updateMemo(int userPlaceIdx, int userIdx, String content); // 메모 수정
	int deleteMemo(int userPlaceIdx); // 메모 삭제
	int deleteUserPlace(int userPlaceIdx); // 명소 삭제
	int updateDayIdxByOldDayIdx(Map<String, Integer> paramMap); // 플래너에 저장된 일차별 명소 순서 바꾸기
	int updateUserPlaceDayIdx(UserPlaceDto dto); // 플래너에 저장된 일차별 안에 명소 순서 바꾸기
	int getPlannerIdxByCode(String code); // 동행인 초대 코드 확인
	int acceptPartner(PartnerDto partner); // 동행인 초대 코드
	
//	작업자 : 최호준 --------------------------------------------------------------
	List<PlannerDto> getPlannerReview(); // 플래너 리뷰
	List<PlannerDto> currentPlanner(int userIdx); // 현재 진행 중인 플래너
	List<PlannerDto> pastPlanner(int userIdx); // 지난 플래너
	List<PlannerDto> getMyPageCalendar(Date plannerDate, int userIdx); // 플래너 일차별 날짜 가져오기
	List<ReviewDetailDto> getReviewList(int plannerIdx);   // 플래너리뷰의 리뷰내용들 가져오기
	void writeReview(int plannerIdx, int userIdx,String content,int rate);   // 플래너리뷰에 댓글쓰기
	
}

