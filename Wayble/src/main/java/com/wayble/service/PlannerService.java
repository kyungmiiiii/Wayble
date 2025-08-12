package com.wayble.service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wayble.dto.CityDto;
import com.wayble.dto.DayDto;
import com.wayble.dto.MemoDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PartnerInfoDto;
import com.wayble.dto.PlannerDetailDto;
import com.wayble.dto.PlannerInfoDto;
import com.wayble.dto.ReviewDetailDto;
import com.wayble.dto.UserPlaceDto;
import com.wayble.dto.UserPlaceNameDto;
import com.wayble.util.ResponsePlaces;

public interface PlannerService {
//	작업자 : 서수련 ------------------------------------------------------------------------
	int startPlanner(int userIdx, int cityIdx, String travelDate); // 플래너 시작
	HashMap<String, Object> loadPlannerBasicInfo(int plannerIdx, int cityIdx); // 플래너 기본 정보 불러오기
	List<Integer> getDayIdxList(int plannerIdx, int travelDate); // 여행 일차 기본키 불러오기
	ResponsePlaces getPlaceListBySearh(int cityIdx, String search, String token); // 명소 검색
	
//	작업자 : 이경미 -------------------------------------------------------------------------
	PlannerDetailDto getPlannerDetailByIdx(int pIdx); // 플래너 전체 리스트
	CityDto getCityDetail(int cIdx); // 도시 상세 보여주기
	List<DayDto> getDaysByPlannerIdx(int dIdx); // 플래너별 일차 가져오기
	List<UserPlaceNameDto> getPlacesByDayIdx(int dayIdx); // 일차별로 선택된 명소 띄워주기
	List<PartnerInfoDto> getPartnerInfoList(int pIdx); // 동행인 리스트
	int deletePartner(int pIdx, int uIdx); // 동행인 삭제
	void addPartner(PartnerDto dto); // 동행인 추가
	int acceptPartner(PartnerDto partner); // 동행인 초대 코드
	List<MemoDto> getMemoDetailList(int upIdx); // 명소별 작성된 메모 상세 보여주기
	int updateTitle(String title, int plannerIdx); // 플래너 제목 수정
	boolean memoExists(int userPlaceIdx, int userIdx); // 메모 존재 여부 확인
	int insertMemo(int userPlaceIdx, int userIdx, String content); // 메모 작성
	int updateMemo(int userPlaceIdx, int userIdx, String content); // 메모 수정
	int deleteMemo(int userPlaceIdx); // 메모 삭제
	int deleteUserPlace(int userPlaceIdx); // 명소 삭제
	int updateDayIdxByOldDayIdx(Map<String, Integer> paramMap); // 플래너에 저장된 일차별 명소 순서 바꾸기
	int updateUserPlaceDayIdx(UserPlaceDto dto); // 플래너에 저장된 일차별 안에 명소 순서 바꾸기
	int getPlannerIdxByCode(String code); // 동행인 초대 코드 확인
	
@Service
public class EmailService {
	@Autowired 
    private JavaMailSender mailSender;

	// 초대 메일 보내기
	// 파라미터 : 보낼 이메일(toEmail), 닉네임(nickname), 초대코드(code)
	// 리턴값 : -
    public void sendInvitationEmail(String toEmail, String nickname, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String subject = "[Wayble] " + nickname + "님 플래너에 초대되셨습니다.";
            helper.setSubject(subject);
            helper.setFrom("wbtest2025@naver.com", "NXDZVER3UD6E");
            helper.setTo(toEmail);

            String ip = "localhost";
            String contextRoot = "/Wayble";
            String url = "http://" + ip + ":9090" + contextRoot + "/accept-invite?code=" + code + "&userEmail=" + URLEncoder.encode(toEmail, "utf-8");

            String htmlMsg = "<div style='font-family:Arial, sans-serif;'>" +
                    "<h2>안녕하세요, Wayble입니다 </h2>" +
                    "<p><strong>" + nickname + "</strong>님이 당신을 플래너에 초대했어요.</p>" +
                    "<a href='" + url + "' " +
                    "style='display:inline-block;padding:10px 20px;background-color:#4CAF50;"
                    + " color:white;text-decoration:none;border-radius:5px;'>수락하기</a>" +
                    "<p style='margin-top:20px;'>감사합니다!<br>Wayble 팀 드림</p>" +
                    "</div>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);
            System.out.println("이메일 전송 완료!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//	작업자 : 최호준 --------------------------------------------------------------------------------
	List<Map<String,Object>> getplannerList(); // 플래너 리뷰
	List<Map<String,Object>> currentPlanner(int userIdx); // 현재 진행 중인 플래너
	List<Map<String,Object>> pastPlanner(int userIdx); // 지난 플래너
	List<Map<String,Object>> getTravelDateForCalendar(Date plannerDate, int userIdx); // 마이페이지 달력
	List<ReviewDetailDto> getReviewList(int plannerIdx); // 플래너리뷰 댓글에 보여줄정보 불러오기
    PlannerInfoDto getPlannerInfoDto(int plannerIdx); // PlannerInfoDto 불러오기 
    Map<String,Object> loadUserplace(int plannerIdx, int date);   // 플래너리뷰에 선택된 유저명소 불러오기
    Map<String, Object> getPlaceDetailAsMap(int userPlaceIdx); // 지도에서 명소정보 가져오기
    Map<String,Object> addReviewComment(int plannerIdx, int userIdx,String content,int rate); // 플래너리뷰에 댓글달기
	
	
}
