package com.wayble.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

@Repository
public class PlannerDaoImpl implements PlannerDao {
	@Autowired
	SqlSession sqlSession;

//	작업자 : 서수련 ------------------------------------------------------------------

	// 플래너 시작
	// 파라미터 : 만든 유저 기본키(userIdx), 도시 기본키(cityIdx), 여행 날짜(travelDate)
	// 리턴값 : 플래너 기본키
	@Override
	public int startPlanner(int userIdx, int cityIdx, String travelDate) {
		String[] start = travelDate.split("-")[0].split("/");
		String[] end = travelDate.split("-")[1].split("/");
		String startDateStr = start[2] + "-" + start[0] + "-" + start[1];
		String endDateStr = end[2] + "-" + end[0] + "-" + end[1];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = (Date)format.parse(startDateStr);
			endDate = (Date)format.parse(endDateStr);
		} catch (ParseException e) {e.printStackTrace();}
		
		PlannerDto dto = new PlannerDto();
		dto.setUserIdx(userIdx);
		dto.setCityIdx(cityIdx);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		sqlSession.insert("PlannerMapper.startPlanner", dto);
		int plannerIdx = dto.getPlannerIdx();
		return plannerIdx;
	}

	// 플래너 정보 가져오기
	// 파라미터 : 플래너 기본키(plannerIdx)
	// 리턴값 : 플래너 기본키
	@Override
	public PlannerInfoDto getPlannerInfoDto(int plannerIdx) {
		return sqlSession.selectOne("PlannerMapper.getPlannerInfo", plannerIdx);
	}

	// 여행 일자 만큼 dayIdx 만들기
	// 파라미터 : 플래너 기본키(plannerIdx)
	// 리턴값 : 여행 일차 기본키
	@Override
	public int generateDayIdx(int plannerIdx) {
		DayDto d = new DayDto();
		d.setPlannerIdx(plannerIdx);
		sqlSession.insert("PlannerMapper.generateDayIdx", d);
		int dayIdx = d.getDayIdx();
		return dayIdx;
	}
	
//	작업자 : 이경미 ------------------------------------------------------------------------
	// 플래너 전체 리스트
	// 파라미터 : 플래너 기본키(pIdx)
	// 리턴값 : 플래너 상세정보(PlannerDetailDto)
	@Override
    public PlannerDetailDto getPlannerDetailByIdx(int pIdx) {
        return sqlSession.selectOne("PlannerMapper.getPlannerDetailByIdx", pIdx);
	}

	// 도시 상세 보여주기
	// 파라미터 : 도시 기본키(cIdx)
	// 리턴값 : 도시정보(CityDto)
	@Override
    public CityDto getCityDetail(int cIdx) {
        return sqlSession.selectOne("PlannerMapper.getCityDetail", cIdx);
	}
	
	// 플래너별 일차 가져오기
	// 파라미터 : 플래너 기본키(pIdx)
	// 리턴값 : [여행일차정보](DayDto)
	@Override
	public List<DayDto> getDaysByPlannerIdx(int pIdx) {
	    return sqlSession.selectList("PlannerMapper.getDaysByPlannerIdx", pIdx);
	}
	
	// 일차별로 선택된 명소 띄워주기
	// 파라미터 : 여행 일차 기본키(dayIdx)
	// 리턴값 : [선택된 명소 정보](UserPlaceNameDto)
	@Override
	public List<UserPlaceNameDto> getPlacesByDayIdx(int dayIdx) {
	    return sqlSession.selectList("PlannerMapper.getPlacesByDayIdx", dayIdx);
	}
	
	// 동행인 리스트
	// 파라미터 : 플래너 기본키(plannerIdx)
	// 리턴값 : [파트너정보](PartnerInfoDto)
	@Override
	public List<PartnerInfoDto> getPartnerInfoList(int plannerIdx) {
	    return sqlSession.selectList("PlannerMapper.getPartnerInfoList", plannerIdx);
	}
	
	// 동행인 삭제
	// 파라미터 : 플래너 기본키(pIdx), 만든 유저 기본키(uIdx)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int deletePartner(int pIdx, int uIdx) {
       Map<String, Object> map = new HashMap<>();
       map.put("plannerIdx", pIdx);
       map.put("userIdx", uIdx);
       return sqlSession.delete("PlannerMapper.deletePartner", map);
    }
   
    // 동행인 추가
    // 파라미터 : PartnerDto
	// 리턴값 : -
    @Override
    public void addPartner(PartnerDto dto) {
       sqlSession.insert("PlannerMapper.addPartner", dto);
    }
    
    // 동행인 초대 코드
    // 파라미터 : PartnerDto
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int acceptPartner(PartnerDto partner) {
    	return sqlSession.update("PlannerMapper.acceptPartner", partner);
    }

    // 명소별 작성된 메모 상세 보여주기
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : [메모정보](MemoDto)
    @Override
    public List<MemoDto> getMemoDetailList(int userPlaceIdx) {
       return sqlSession.selectList("PlannerMapper.getMemoDetailList", userPlaceIdx);
    }
   
    // 플래너 제목 수정
    // 파라미터 : 플래너 제목(title), 플래너 기본키(plannerIdx)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int updateTitle(String title, int plannerIdx) {
       Map<String, Object> map = new HashMap<>();
       map.put("title", title);
       map.put("plannerIdx", plannerIdx);
       return sqlSession.update("PlannerMapper.updateTitle", map);
    }
    
    // 메모 존재 여부 확인
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx)
	// 리턴값 : 메모 존재 유무
    @Override
    public boolean memoExists(int userPlaceIdx, int userIdx) {
    	Map<String, Integer> paramMap = new HashMap<>();
    	paramMap.put("userPlaceIdx", userPlaceIdx);
    	paramMap.put("userIdx", userIdx);
    	return sqlSession.selectOne("memoExists", paramMap);
    }
    
    // 메모 작성
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx), 메모(content)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int insertMemo(int userPlaceIdx, int userIdx, String content) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userPlaceIdx", userPlaceIdx);
        paramMap.put("userIdx", userIdx);
        paramMap.put("content", content);
        return sqlSession.insert("insertMemo", paramMap);
    }
    
    // 메모 수정
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx), 메모(content)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int updateMemo(int userPlaceIdx, int userIdx, String content) {
       Map<String, Object> map = new HashMap<>();
       map.put("userPlaceIdx", userPlaceIdx);
       map.put("userIdx", userIdx);
       map.put("content", content);
       return sqlSession.update("PlannerMapper.updateMemo", map);
    }
   
    // 메모 삭제
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int deleteMemo(int userPlaceIdx) {
       Map<String, Object> map = new HashMap<>();
       map.put("userPlaceIdx", userPlaceIdx);
       return sqlSession.delete("PlannerMapper.deleteMemo", map);
    }
   
    // 명소 삭제
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int deleteUserPlace(int userPlaceIdx) {
       Map<String, Object> map = new HashMap<>();
       map.put("userPlaceIdx", userPlaceIdx);
       return sqlSession.delete("PlannerMapper.deleteUserPlace", map);
    }
    
    // 플래너에 저장된 일차별 명소 순서 바꾸기
    // 파라미터 : Map
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int updateDayIdxByOldDayIdx(Map<String, Integer> paramMap) {
    	return sqlSession.update("PlannerMapper.updateDayIdxByOldDayIdx", paramMap);
    }

    // 플래너에 저장된 일차별 안에 명소 순서 바꾸기
    // 파라미터 : UserPlaceDto
	// 리턴값 : 영향 받은 행의 개수
    @Override
    public int updateUserPlaceDayIdx(UserPlaceDto dto) {
       return sqlSession.update("PlannerMapper.updateUserPlaceDayIdx", dto);
    }
   
    // 동행인 초대 코드 확인
    // 파라미터 : 인증코드(code)
	// 리턴값 : 플래너 기본키
    @Override
    public int getPlannerIdxByCode(String code) {
       return sqlSession.selectOne("PlannerMapper.getPlannerIdxByCode", code);
    }

//	작업자 : 최호준 --------------------------------------------------------------------------
	// 플래너 리뷰
    // 파라미터 : -
	// 리턴값 : [플래너정보](PlannerDto)
    @Override
	public List<PlannerDto> getPlannerReview() {
		return sqlSession.selectList("PlannerMapper.plannerReview");
	}
    
	// 현재 진행 중인 플래너
    // 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : [플래너정보](PlannerDto)
    @Override
    public List<PlannerDto> currentPlanner(int userIdx) {
       return sqlSession.selectList("PlannerMapper.currentPlanner",userIdx);
    }
	
	// 지난 플래너
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : [플래너정보](PlannerDto)
	@Override
	public List<PlannerDto> pastPlanner(int userIdx) {
		return sqlSession.selectList("PlannerMapper.pastPlanner", userIdx);
	}
	
	// 마이페이지 달력
	// 파라미터 : 플래너 날짜(plannerDate), 유저 기본키(userIdx)
	// 리턴값 : [플래너정보](PlannerDto)
	@Override
	public List<PlannerDto> getMyPageCalendar(Date plannerDate, int userIdx) {
		Map<String, Object> map1 = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String plannerDateStr = sdf.format(plannerDate);
		map1.put("plannerDate", plannerDateStr);
		map1.put("userIdx", userIdx+"");
		return sqlSession.selectList("PlannerMapper.getMyPageCalendar", map1);
	}
	
	// 플래너리뷰의 리뷰내용들 가져오기
	// 파라미터 : 플래너 기본키
	// 리턴값 : [리뷰정보](ReviewDetailDto)
    @Override
    public List<ReviewDetailDto> getReviewList(int plannerIdx) {
       return sqlSession.selectList("PlannerMapper.reviewList",plannerIdx);
    }
   
    // 플래너리뷰에 댓글쓰기
    // 파라미터 : 플래너 기본키, 유저 기본키, 댓글, 별점
	// 리턴값 : -
    @Override
    public void writeReview(int plannerIdx, int userIdx,String content,int rate) {
       Map<String, Object> map = new HashMap<>();
       map.put("plannerIdx", plannerIdx);
       map.put("userIdx", userIdx);
       map.put("content", content);
       map.put("rate", rate);
       sqlSession.insert("PlannerMapper.reviewComment",map);
    }
}
