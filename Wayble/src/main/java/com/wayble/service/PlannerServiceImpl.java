package com.wayble.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wayble.dao.CommonDao;
import com.wayble.dao.PlannerDao;
import com.wayble.dto.CityDto;
import com.wayble.dto.DayDto;
import com.wayble.dto.MemoDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PartnerInfoDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.PlannerDetailDto;
import com.wayble.dto.PlannerDto;
import com.wayble.dto.PlannerInfoDto;
import com.wayble.dto.ReviewDetailDto;
import com.wayble.dto.UserPlaceDto;
import com.wayble.dto.UserPlaceNameDto;
import com.wayble.dto.UsersDto;
import com.wayble.util.Distance;
import com.wayble.util.PlacesAPI1;
import com.wayble.util.ResponsePlaces;
import com.wayble.util.TravelDate;

@Service
public class PlannerServiceImpl implements PlannerService {
	@Autowired
	PlannerDao pDao;
	@Autowired
	CommonDao coDao;

//	작업자 : 서수련 ---------------------------------------------------------------
	
	// 플래너 시작
	// 파라미터 : 유저 기본키(userIdx), 도시 기본키(cityIdx), 여행 날짜(travelDate)
	// 리턴값 : 플래너 기본키
	@Override
	public int startPlanner(int userIdx, int cityIdx, String travelDate) {
		return pDao.startPlanner(userIdx, cityIdx, travelDate);
	}

	// 플래너 정보 가져오기
	// 파라미터 : 플래너 기본키(plannerIdx), 도시 기본키(cityIdx)
	// 리턴값 : 플래너 기본키(plannerIdx), 나라(country), 도시(city), 여행시작일(startDate), 여행종료일(endDate), 플래너제목(title), 도시위도(cityLat), 도시경도(cityLng), 여행일(travelDate)
	@Override
	public HashMap<String, Object> loadPlannerBasicInfo(int plannerIdx, int cityIdx) {
		HashMap<String, Object> map = new HashMap<>();
		PlannerInfoDto p = pDao.getPlannerInfoDto(plannerIdx);
		CityDto c = coDao.getCityDtoByCityIdx(cityIdx);
		map.put("plannerIdx", plannerIdx);
		map.put("country", p.getCountry());
		map.put("city", p.getCity());
		map.put("startDate", p.getStartDate().substring(0, 10));
		map.put("endDate", p.getEndDate().substring(0, 10));
		map.put("title", p.getTitle());
		map.put("cityLat", c.getLat());
		map.put("cityLng", c.getLng());
		map.put("travelDate", TravelDate.main(p.getStartDate(), p.getEndDate()));
		return map;
	}

	// 여행 일자 만큼 dayIdx 만들기
	// 파라미터 : 플래너 기본키(plannerIdx), 여행 날짜(travelDate)
	// 리턴값 : [여행일차 기본키(dayIdx)]
	@Override
	public List<Integer> getDayIdxList(int plannerIdx, int travelDate) {
		List<Integer> list = new ArrayList<>();
		for(int i=1; i<=travelDate; i++) {
			int dayIdx = pDao.generateDayIdx(plannerIdx);
			list.add(dayIdx);
		}
		return list;
	}

	// 검색어로 명소 불러오기
	// 파라미터 : 도시 기본키(cityIdx), 검색(search), 토큰(token)
	// 리턴값 : 다음페이지 토큰(token), [명소 정보(PlaceInfoDto)]
	@Override
	public ResponsePlaces getPlaceListBySearh(int cityIdx, String search, String token) {
		CityDto dto = coDao.getCityDtoByCityIdx(cityIdx);
		ResponsePlaces result = null;
		try {
			result = PlacesAPI1.main(dto, search, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
//	작업자 : 이경미 -------------------------------------------------------------------------------------
	
	// 플래너 전체 리스트
	// 파라미터 : 플래너 기본키(pIdx)
	// 리턴값 : 명소 상세정보(PlannerDetailDto) 
	@Override
	public PlannerDetailDto getPlannerDetailByIdx(int pIdx) {
		return pDao.getPlannerDetailByIdx(pIdx);
	}
	
	// 도시 상세 보여주기
	// 파라미터 : 도시 기본키(cIdx)
	// 리턴값 : 도시정보(CityDto)
	@Override
	public CityDto getCityDetail(int cIdx) {
	    return pDao.getCityDetail(cIdx);
	}
	
	// 플래너별 일차 가져오기
	// 파라미터 : 플래너 기본키(pIdx)
	// 리턴값 : [여행일차정보](DayDto)
	@Override
	public List<DayDto> getDaysByPlannerIdx(int pIdx) {
	    return pDao.getDaysByPlannerIdx(pIdx);
	}
	
	// 일차별로 선택된 명소 띄워주기
	// 파라미터 : 여행 일차 기본키(dayIdx)
	// 리턴값 : [선택된 명소 정보](UserPlaceNameDto)
	@Override
	public List<UserPlaceNameDto> getPlacesByDayIdx(int dayIdx) {
	    return pDao.getPlacesByDayIdx(dayIdx);
	}
	
	// 동행인 리스트
	// 파라미터 : 플래너 기본키(plannerIdx) 
	// 리턴값 : [파트너정보](PartnerInfoDto)
	@Override
	public List<PartnerInfoDto> getPartnerInfoList(int pIdx) {
	    return pDao.getPartnerInfoList(pIdx);
	}
	
	// 동행인 삭제
	// 파라미터 : 플래너 기본키(pIdx), 만든 유저 기본키(uIdx)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int deletePartner(int pIdx, int uIdx) {
	    return pDao.deletePartner(pIdx, uIdx);
	}
	
	// 동행인 추가
    // 파라미터 : PartnerDto
	// 리턴값 : -
	@Override
	public void addPartner(PartnerDto dto) {
	    pDao.addPartner(dto);
	}
	
	// 동행인 초대 코드
    // 파라미터 : PartnerDto
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int acceptPartner(PartnerDto partner) {
	   	return pDao.acceptPartner(partner);
	}
	
	// 명소별 작성된 메모 상세 보여주기
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : [메모정보](MemoDto)
	@Override
	public List<MemoDto> getMemoDetailList(int upIdx) {
	    return pDao.getMemoDetailList(upIdx);
	}
	
	// 플래너 제목 수정
    // 파라미터 : 플래너 제목(title), 플래너 기본키(plannerIdx)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int updateTitle(String title, int plannerIdx) {
	    return pDao.updateTitle(title, plannerIdx);
	}
	
	// 메모 존재 여부 확인
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx)
	// 리턴값 : 메모 존재 유무
	@Override
	public boolean memoExists(int userPlaceIdx, int userIdx) {
		return pDao.memoExists(userPlaceIdx, userIdx);
	}
	
	// 메모 작성
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx), 메모(content)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int insertMemo(int userPlaceIdx, int userIdx, String content) {
        return pDao.insertMemo(userPlaceIdx, userIdx, content);
	}
	
	// 메모 수정
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx), 메모(content)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int updateMemo(int userPlaceIdx, int userIdx, String content) {
	    return pDao.updateMemo(userPlaceIdx, userIdx, content);
	}
	
	// 메모 삭제
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int deleteMemo(int userPlaceIdx) {
	    return pDao.deleteMemo(userPlaceIdx);
	}
	
	// 명소 삭제
    // 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int deleteUserPlace(int userPlaceIdx) {
	    return pDao.deleteUserPlace(userPlaceIdx);
	}
	
	// 플래너에 저장된 일차별 명소 순서 바꾸기
	// 파라미터 : Map
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int updateDayIdxByOldDayIdx(Map<String, Integer> paramMap) {
	    return pDao.updateDayIdxByOldDayIdx(paramMap);
	}
	
	// 플래너에 저장된 일차별 안에 명소 순서 바꾸기
    // 파라미터 : UserPlaceDto
	// 리턴값 : 영향 받은 행의 개수
	@Override
	public int updateUserPlaceDayIdx(UserPlaceDto dto) {
	    return pDao.updateUserPlaceDayIdx(dto);
	}
	
	// 동행인 초대 코드 확인
	// 파라미터 : 인증코드(code)
	// 리턴값 : 플래너 기본키
	@Override
	public int getPlannerIdxByCode(String code) {
	    return pDao.getPlannerIdxByCode(code);
	}

	
//	작업자 : 최호준 -----------------------------------------------------------------------------------------------------
	
	// 플래너 리뷰
    // 파라미터 : -
	// 리턴값 : 
	@Override
    public List<Map<String,Object>> getplannerList() {
		List<PlannerDto> reviewlist = pDao.getPlannerReview(); 
        List<Map<String,Object>> ret = new ArrayList<>();
      
        for(int i=0; i<10; i++) {
        	HashMap<String,Object> map = new HashMap<>();
            int cityIdx = reviewlist.get(i).getCityIdx();  
            CityDto city = coDao.getcityInfoSort(cityIdx);
         
            map.put("pic", city.getPic());  
	        map.put("title", reviewlist.get(i).getTitle());
	        map.put("cityIdx",reviewlist.get(i).getCityIdx());
	        map.put("plannerIdx",reviewlist.get(i).getPlannerIdx());
	         
	        ret.add(map);
        }
        return ret;
    }

	// 현재 진행 중인 플래너
    // 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : 
	@Override
    public List<Map<String,Object>> currentPlanner(int userIdx) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<PlannerDto> current = pDao.currentPlanner(userIdx);
        List<Map<String,Object>> ret = new ArrayList<>();
      
        for(int i=0; i<=current.size()-1; i++) {
        	if(i==4) break;
            int cityIdx = current.get(i).getCityIdx();
            CityDto city = coDao.getcityInfoSort(cityIdx);
            String startDateStr = "";
            String endDateStr = "";
            startDateStr = sdf.format(current.get(i).getStartDate());
            endDateStr = sdf.format(current.get(i).getEndDate());
          
	        Map<String,Object> map = new HashMap<>();
	        map.put("country", city.getCountry());
	        map.put("name", city.getName());
	        map.put("plannerIdx", current.get(i).getPlannerIdx());
	        map.put("cityIdx", current.get(i).getCityIdx());
	        map.put("title", current.get(i).getTitle());
	        map.put("startDate", startDateStr);
	        map.put("endDate", endDateStr);
	        map.put("completeYN", current.get(i).getCompleteYN());
	         
	        ret.add(map);
        }
        return ret;
	}

	// 지난 플래너
	// 파라미터 : 유저 기본키(userIdx)
	// 리턴값 : 
	@Override
	public List<Map<String,Object>> pastPlanner(int userIdx) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    List<PlannerDto> old = pDao.pastPlanner(userIdx);
	    List<Map<String,Object>> ret = new ArrayList<>();
	      
	    for(int i=0; i<=old.size()-1; i++) {
	    	if(i==4) break;
	        int cityIdx = old.get(i).getCityIdx();
	        CityDto city = coDao.getcityInfoSort(cityIdx);
	        String startDateStr = "";
	        String endDateStr = "";
	        startDateStr = sdf.format(old.get(i).getStartDate());
	        endDateStr = sdf.format(old.get(i).getEndDate());
	          
	        Map<String,Object> map = new HashMap<>();
	        map.put("country", city.getCountry());
	        map.put("name", city.getName());
	        map.put("plannerIdx", old.get(i).getPlannerIdx());
	        map.put("cityIdx", old.get(i).getCityIdx());
	        map.put("title", old.get(i).getTitle());
	        map.put("startDate", startDateStr);
	        map.put("endDate", endDateStr);
	        map.put("completeYN", old.get(i).getCompleteYN());
	         
	        ret.add(map);
	    }
	    return ret;
	}
	
	// 마이페이지 달력
	// 파라미터 : 플래너 날짜(plannerDate), 유저 기본키(userIdx)
	// 리턴값 : 
	@Override
	public List<Map<String,Object>> getTravelDateForCalendar(Date plannerDate, int userIdx) {
		List<PlannerDto> list1 = pDao.getMyPageCalendar(plannerDate, userIdx);
	    List<Map<String,Object>> ret = new ArrayList<>();
	    Calendar cal = Calendar.getInstance();
	    for(int i=0; i<=list1.size()-1; i++) {
	    	PlannerDto dto = list1.get(i);
	        HashMap<String,Object> map = new HashMap<>();

	        // 여행타이틀을 map으로.
	        String title = dto.getTitle();
	        map.put("title", title);
	         
	        // 시작일을 map으로.
	        Date startTime = dto.getStartDate();

	  	    cal.setTime(startTime);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1; // 0-based → 1월 = 0이므로 +1
			int day = cal.get(Calendar.DAY_OF_MONTH);
			 
			map.put("year1", year);
			map.put("month1", String.format("%02d", month));
			map.put("day1", String.format("%02d", day));
	         
	        // 종료일을 map으로.
	        Date endTime = dto.getEndDate();

            cal.setTime(endTime);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH) + 1; // 0-based → 1월 = 0이므로 +1
            int day2 = cal.get(Calendar.DAY_OF_MONTH);
	         
		    map.put("year2", year2);
		    map.put("month2", String.format("%02d", month2));
		    map.put("day2", String.format("%02d", day2));
	         
	        ret.add(map);
	    }
	    return ret;
	}
	
	// 플래너리뷰 댓글에 보여줄정보 불러오기
	// 파라미터 : 플래너 기본키
//	@Override
//	public List<Map<String, Object>> getReviewList(int plannerIdx) {
//		List<ReviewDetailDto> list1 = pDao.getReviewList(plannerIdx);
//	    List<Map<String,Object>> ret = new ArrayList<>();
//	      
//	    for(int i=0; i<=list1.size()-1; i++) {
//	    	Map<String,Object> map = new HashMap<>();
//	        int userIdx = list1.get(i).getUserIdx();
//	        UsersDto dto = coDao.getUserInfo(userIdx);
//	         
//	        map.put("nickname", dto.getNickname());
//	        map.put("profile", dto.getProfile());
//	        map.put("plannerIdx", list1.get(i).getPlannerIdx());
//	        map.put("rate", list1.get(i).getRate());
//	        map.put("userIdx", dto.getUserIdx());
//	        map.put("content", list1.get(i).getContent());
//	         
//	        ret.add(map);
//	    }
//	    return ret;
//	}
	   
	// PlannerInfoDto 불러오기 
	// 파라미터 : 플래너 기본키
	// 리턴값 : 
	@Override
	public PlannerInfoDto getPlannerInfoDto(int plannerIdx) {
		return pDao.getPlannerInfoDto(plannerIdx);
	}
	   
	// 플래너리뷰에 선택된 유저명소 불러오기
	// 파라미터 : 플래너기본키, 날짜
	// 리턴값 : 
	@Override
	public Map<String,Object> loadUserplace(int plannerIdx, int date) {
		Map<String, Object> result = new HashMap<>();
	    List<Map<String, Object>> arr = new ArrayList<>();
	      
	    try {
	    	List<DayDto> dayIdxList = pDao.getDaysByPlannerIdx(plannerIdx);
	        if (date < 0 || date >= dayIdxList.size()) {
	        	result.put("error", "유효하지 않은 날짜입니다.");
	            return result;
	        }
	        int dayIdx = dayIdxList.get(date).getDayIdx();
            List<UserPlaceNameDto> list = pDao.getPlacesByDayIdx(dayIdxList.get(date).getDayIdx());
	           
	        PlannerInfoDto dto = pDao.getPlannerInfoDto(plannerIdx);
	        int travelDate = getTravelDate(dto.getStartDate(), dto.getEndDate());
	           
	        for (int i = 0; i < list.size(); i++) {
	        	UserPlaceNameDto u = list.get(i);
	            PlaceDto pDto = coDao.getPlaceDetail(u.getPlaceIdx());
	            PlaceDto nextPDto = (i < list.size() - 1)
	                   ? coDao.getPlaceDetail(list.get(i + 1).getUserPlaceIdx())
	                   : null;
	               
	            Map<String, Object> placeMap = new HashMap<>();
	            placeMap.put("name", u.getName()); // 명소 이름
	            placeMap.put("placeIdx", u.getPlaceIdx()); // 실제 장소 ID
	            placeMap.put("userPlaceIdx", u.getUserPlaceIdx()); // 사용자 커스텀 장소 ID
	            placeMap.put("lat", pDto.getLat()); // 위도
	            placeMap.put("lng", pDto.getLng()); // 경도
	                
	            if (nextPDto != null) {
	            	double distance = Distance.convertLatLngToMeter(
	            			pDto.getLat(), pDto.getLng(),
	                        nextPDto.getLat(), nextPDto.getLng());
	                placeMap.put("distance", distance); // 두 장소 간 거리 (미터 단위)
	            }
	            arr.add(placeMap);
	        }
	        result.put("list", arr);           // 장소 목록
	        result.put("travelDate", travelDate);
	           
	    } catch(Exception e) {
	    	e.printStackTrace();
	        result.put("error", "데이터 처리 중 오류 발생");
	    }
	    return result; // JSON처럼 프론트로 보내줄 결과
	    }
	   
	    public static int getTravelDate(String start, String end) {
	    	start = start.substring(0, 10);   
	        end = end.substring(0, 10);   
	      
	        LocalDate startDate = LocalDate.parse(start);
	        LocalDate endDate = LocalDate.parse(end);
	        
	        long diff = ChronoUnit.DAYS.between(startDate, endDate);
	        return (int)++diff;
	    }
	   
	// 지도에서 명소정보 가져오기
	// 파라미터 : 선택된 명소 기본키
	// 리턴값 : 
	@Override
	public Map<String, Object> getPlaceDetailAsMap(int userPlaceIdx) {
		Map<String, Object> result = new HashMap<>();
	    try {
	    	PlaceDto dto = coDao.getPlaceDetail(userPlaceIdx);
	        result.put("name", dto.getName());
	        result.put("pic", dto.getPic());
	        result.put("lat", dto.getLat());
	        result.put("lng", dto.getLng());
	        result.put("placeRate", dto.getPlaceRate());
	        result.put("placeReviewCount", dto.getPlaceReviewCount());
	    } catch (Exception e) {
	    	e.printStackTrace();
	        result.put("error", "장소 정보를 불러오는 중 오류 발생");
	    }
	    return result;
	}
	   	   
	// 플래너리뷰에 댓글쓰기
	// 파라미터 : 플래너 기본키, 유저 기본키, 댓글내용, 별점
	// 리턴값 : 
	@Override
	public Map<String,Object> addReviewComment(int plannerIdx, int userIdx,String content,int rate) {
		UsersDto uDto = coDao.getUserInfo(userIdx);           
	    pDao.writeReview(plannerIdx, userIdx, content, rate);
	    Map<String, Object> map = new HashMap<>();
	    map.put("nickname", uDto.getNickname());
	    map.put("profile", uDto.getProfile());
	    map.put("userIdx", uDto.getUserIdx());
	    map.put("plannerIdx", plannerIdx);
	    map.put("content", content);
	    map.put("rate", rate);
	    return map;
	}

	@Override
	public List<ReviewDetailDto> getReviewList(int plannerIdx) {
		// TODO Auto-generated method stub
		return null;
	}
}