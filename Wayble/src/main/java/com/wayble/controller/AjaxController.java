package com.wayble.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wayble.dto.AlarmDto;
import com.wayble.dto.ChatDetailDto;
import com.wayble.dto.CityDto;
import com.wayble.dto.MemoDto;
import com.wayble.dto.MessageInfoDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PartnerInfoDto;
import com.wayble.dto.PlaceDto;
import com.wayble.dto.PostDto;
import com.wayble.dto.SocketMessageDto;
import com.wayble.dto.UserPlaceDto;
import com.wayble.dto.UserPlaceNameDto;
import com.wayble.dto.UsersDto;
import com.wayble.service.ChatService;
import com.wayble.service.CommonService;
import com.wayble.service.PlannerService;
import com.wayble.service.PlannerService.EmailService;
import com.wayble.service.SnsService;
import com.wayble.util.GenerateInviteCode;
import com.wayble.util.MailSender;
import com.wayble.util.ResponsePlaces;

@RestController
public class AjaxController {
	@Autowired
	CommonService coSvc;
	@Autowired
	PlannerService pSvc;
	@Autowired
	SnsService sSvc;
	@Autowired
	ChatService chSvc;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	EmailService emailService;
	
// CHAT AJAX ---------------------------------------------------------------------------------------------------
	
	// 서버에서 클라이언트에게 메시지 보내기 (작업자 : 서수련)
	// 파라미터 : SocketMessageDto(클라이언트에서 보내는 메시지)
	@MessageMapping("/chat.send") 
    public void sendMessage(SocketMessageDto dto) {
    	System.out.println("메시지 받음: " + dto.getSender() + " / " + dto.getContent());
    	int messageIdx = chSvc.saveMessage(dto.getChatRoomIdx(), dto.getSender(), dto.getContent());
    	String profile = coSvc.getUserInfo(dto.getSender()).getProfile();
    	String nickname = coSvc.getUserInfo(dto.getSender()).getNickname();
    	dto.setMessageIdx(messageIdx);
    	dto.setSenderProfile(profile);
    	dto.setSenderNickname(nickname);
    	messagingTemplate.convertAndSend("/topic/chat-room/" + dto.getChatRoomIdx(), dto);
    	List<Integer> memberList = chSvc.getChatMemberByChatRoomIdx(dto.getChatRoomIdx());
    	for(Integer userIdx : memberList) {
    		if(userIdx == dto.getSender()) continue;
    		Map<String, Object> alarmData = new HashMap<>();
    		if(dto.getType().equals("private")) {
    			alarmData.put("type", "privateChat");
    			alarmData.put("chatRoomIdx", dto.getChatRoomIdx());
    			messagingTemplate.convertAndSend("/topic/alarm/" + userIdx, alarmData);
    			coSvc.addChatAlarm(userIdx, dto.getSender());
    		} else {
    			alarmData.put("type", "openChat");
    			alarmData.put("chatRoomIdx", dto.getChatRoomIdx());
    			messagingTemplate.convertAndSend("/topic/alarm/" + userIdx, alarmData);
    		}
    		chSvc.addUnread(userIdx, messageIdx);
    	}
    }
	
	// 오픈채팅방 목록 (작업자 : 서수련)
	// 파라미터 : -
	@PostMapping("sns/loadOpenChatList")
	public List<HashMap<String, Object>> loadOpenChatList() {
		return chSvc.loadOpenChatList();
	}
	
	// 검색으로 오픈채팅방 찾기 (작업자 : 서수련)
	// 파라미터 : 검색어(search)
	@PostMapping("sns/loadOpenChatListBySearch")
	public List<HashMap<String, Object>> loadOpenChatListBySearch(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		String search = element.getAsJsonObject().get("search").getAsString();
		return chSvc.loadOpenChatListBySearch(search);
	}
	
	// 오픈채팅 상세정보 (작업자 : 서수련)
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/getOpenChatInfo")
	public Map<String, Object> getOpenChatInfo(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		boolean join = false;
		HashMap<String, Object> ret = new HashMap<>();
		ChatDetailDto dto = chSvc.getChatDetailDtoByChatRoomIdx(chatRoomIdx);
		List<Map<String, String>> memberList = new ArrayList<>();
		for(int i : chSvc.getChatMemberByChatRoomIdx(chatRoomIdx)) {
			HashMap<String, String> map = new HashMap<>();
			map.put("profile", coSvc.getUserInfo(i).getProfile());
			map.put("nickname", coSvc.getUserInfo(i).getNickname());
			if(userIdx == i) join = true;
			memberList.add(map);
		}
		ret.put("join", join);
		ret.put("info", chSvc.getChatDetailDtoByChatRoomIdx(chatRoomIdx));
		ret.put("memberList", memberList);
		return ret;
	}
	
	// 오픈채팅방 참여하기 (작업자 : 서수련)
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/joinOpenChat")
	public void joinOpenChat(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		chSvc.joinOpenChat(chatRoomIdx, userIdx);
	}
	
	// 오픈채팅방 만들기 (작업자 : 서수련)
	// 파라미터 : 만든 유저 기본키(userIdx), 제목(title), 카테고리(category), 설명(description), 최대인원(limit)
	@PostMapping("sns/make-open-chat")
	public int makeOpenChat(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		String title = element.getAsJsonObject().get("title").getAsString();
		String category = element.getAsJsonObject().get("category").getAsString();
		String description = element.getAsJsonObject().get("description").getAsString();
		int limit = element.getAsJsonObject().get("limit").getAsInt();
		return chSvc.makeOpenChat(userIdx, title, category, description, limit);
	}	
	
	// 내가 참여한 오픈채팅방 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/loadMyOpenChatList")
	public List<HashMap<String, Object>> loadMyOpenChatList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		return chSvc.loadMyOpenChatList(userIdx);
	}
	
	// 오픈채팅방에서 주고 받은 메시지 (작업자 : 서수련)
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/loadOpenChatMessage")
	public Map<String, Object> loadOpenChatMessage(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("log", chSvc.loadMessage(chatRoomIdx));
		map.put("info", chSvc.getChatDetailDtoByChatRoomIdx(chatRoomIdx));
 		return map;
	}
	
	// 오픈채팅방 나가기 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx), 채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/outOpenChat")
	public void outOpenChat(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		chSvc.deleteChatMember(chatRoomIdx, userIdx);
	}
	
	// 채팅방 기본키로 1:1 채팅방 찾기 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx), 채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/getPrivateChatRoomByChatRoomIdx")
	public Map<String, Object> getPrivateChatRoomByChatRoomIdx(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		return chSvc.getPrivateChatRoomByChatRoomIdx(userIdx, chatRoomIdx);
	}
	
	// 채팅방 기본키로 오픈채팅방 찾기 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx), 채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/getOpenChatRoomByChatRoomIdx")
	public Map<String, Object> getOpenChatRoomByChatRoomIdx(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		return chSvc.getOpenChatRoomByChatRoomIdx(userIdx, chatRoomIdx);
	}
	
	// 1:1 채팅방 목록 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/loadChatList")
	public List<HashMap<String, Object>> loadChatList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		return chSvc.loadMyChatList(userIdx);
	}
	
	// 1:1 채팅 메시지 불러오기 (작업자 : 서수련)
	// 파라미터 :  채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/loadChatMessage")
	public List<MessageInfoDto> loadMessage(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		return chSvc.loadMessage(chatRoomIdx);
	}

	// 상대방이 보낸 채팅 알림 지우기 (작업자 : 서수련)
	// 파라미터 : 보낸 유저 기본키(userIdx)
	@PostMapping("sns/deleteChatAlarm")
	public void deleteChatAlarm(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int sendUserIdx = element.getAsJsonObject().get("sendUserIdx").getAsInt();
		coSvc.deleteChatAlarm(sendUserIdx);
	}
	
	// 메시지 읽음 처리 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx), 채팅방 기본키(chatRoomIdx)
	@PostMapping("sns/readAllMessage")
	public void readAllMessage(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int chatRoomIdx = element.getAsJsonObject().get("chatRoomIdx").getAsInt();
		chSvc.readAllMessage(chatRoomIdx, userIdx);
	}
	
	// 1:1 채팅방 찾기 (작업자 : 서수련)
	// 파라미터 : 상대 유저 기본키(userIdx), 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/getPrivateChatRoomIdx")
	public int getPrivateChatRoomIdx(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int loginUserIdx = element.getAsJsonObject().get("loginUserIdx").getAsInt();
		int result = chSvc.findPrivateChat(loginUserIdx, userIdx);
		if(result != -1) return result;
		return chSvc.startPrivateChat(loginUserIdx, userIdx);
	}
	
// COMMON AJAX --------------------------------------------------------------------------------------------------
	
	// 안 읽은 알림 유무 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	@PostMapping("/getUnreadAlarm")
	public boolean getUnreadAlarm(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		return coSvc.checkUnreadAlarm(userIdx);
	}
	
	// 알람리스트 불러오기 (알람내용,컨텐츠,시간)(작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/alarm")
	public List<AlarmDto> ajaxAlarmList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		// login_user_idx라는 키를 문자열로 받아서 정수로 변환해서 변수선언
		int loginUserIdx = Integer.parseInt(element.getAsJsonObject().get("login_user_idx").getAsString());
		
		// DB처리
		List<AlarmDto> listAlarm = null;
		try {
			listAlarm = coSvc.getAlarmDetailList(Integer.valueOf(""+loginUserIdx));
		} catch (Exception e) {
			e.printStackTrace();
		}
		coSvc.readAllAlarm(loginUserIdx);
		return listAlarm;
	}
	
	// 도시소개 (홈화면 -> 도시 눌렀을때 이름,설명,사진) (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/cityDetail")	
	public CityDto ajaxcityDetail(@RequestBody String dataBody) {	
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		int cityIdx = element.getAsJsonObject().get("city_idx").getAsInt();
		
		CityDto cityDetail = null;
		try {
			cityDetail = coSvc.getCityInfoSortFromIdx(cityIdx);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return cityDetail; 
	}
	
	// 검색명으로 도시 or 나라 찾기 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/searchCity")
	public List<CityDto> ajaxSearchCity(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		String search = element.getAsJsonObject().get("search").getAsString();
		
		List<CityDto> searchCity = coSvc.getSearchCityFromKeyword(search);
		return searchCity; 
	}
	
	// 회원가입 인증코드 발송 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/RegisterCode")
	public Map<String,Object> RegisterSendCodeMail(@RequestBody String dataBody, HttpSession session) {
	      JsonParser parser = new JsonParser();
	      JsonElement element = parser.parse(dataBody);
	      Map<String,Object> result = new HashMap<>();
	      
	      String email = element.getAsJsonObject().get("email").getAsString();
	      UsersDto user = coSvc.getUserByEmail(email);
	      if(user == null) {
	         System.out.println("서버에서 받은 email: " + email);
	         MailSender ms = new MailSender();
	         String code = "";
	         while(code.length()<4) {
	            char ch = 0;
	            ch = (char)(Math.random()*'z');
	            if(ms.ifEngNum(ch))
	               code += ch;
	         }
	         code = code.toUpperCase();
	         System.out.println("인증code : " + code);
	         
	         ms.sendCodeMail(email, code);
	         session.setAttribute("register_code", code);   // 서버에서 받은코드 세션에 담아줌. 나중에 비교할때 필요
	         result.put("result", true);
	      } else {
	         result.put("result", false);
	         result.put("email_err_msg", "이미 가입된 이메일입니다.");
	      }
	      return result;
	   }
	
	// 회원가입 인증코드 확인 (받은 코드랑 입력한코드랑 일치하는지) (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/RegisterCodecheck")
	public Map<String,Object> RegistCheckcode (@RequestBody String dataBody, HttpSession session) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		String inputCode = element.getAsJsonObject().get("code").getAsString();
		System.out.println("사용자 입력 코드: " + inputCode);
		
		String serverCode = (String)session.getAttribute("register_code");
		System.out.println("서버에서 받아온 코드: " + serverCode);
		
		boolean result = false;
		if(inputCode.equals(serverCode)) {
			result = true;
		}
		Map<String,Object> response = new HashMap<>();
		response.put("result", result);
		
		return response;
	}
	
	// 비밀번호 초기화 코드발송 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/ResetPwcode")
	public Map<String,Object> ResetPwSendCodeMail(@RequestBody String dataBody, HttpSession session) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		String email = element.getAsJsonObject().get("email").getAsString();
		System.out.println("서버에서 받은 email: " + email);
		
		MailSender ms = new MailSender();
		
		String code = "";
		while(code.length()<4) {
			char ch = 0;
			ch = (char)(Math.random()*'z');
			if(ms.ifEngNum(ch))
				code += ch;
		}
		code = code.toUpperCase();
		System.out.println("인증code : " + code);
		
		ms.sendCodeMail(email, code);
		session.setAttribute("register_code", code);	// 서버에서 받은코드 세션에 담아줌. 나중에 비교할때 필요
		session.setAttribute("find_pw_email", email);
		
		Map<String,Object> result = new HashMap<>();
		result.put("result", true);
		
		return result;
	}

	// 비밀번호 초기화 인증코드 확인 (받은 코드랑 입력한코드랑 일치하는지) (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/ResetPwcodecheck")
	public Map<String,Object> ResetPwCheckcode (@RequestBody String dataBody, HttpSession session) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		String inputCode = element.getAsJsonObject().get("code").getAsString();
		System.out.println("사용자 입력 코드: " + inputCode);
		
		String serverCode = (String)session.getAttribute("register_code");
		System.out.println("서버에서 받아온 코드: " + serverCode);
		
		boolean result = false;
		if(inputCode.equals(serverCode)) {
			result = true;
		}
		Map<String,Object> response = new HashMap<>();
		response.put("result", result);
		
		return response;
	}

	// 닉네임 중복확인 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/nicknameCheck")
	public Map<String,Object> RegistChecknickName(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		List<String> usersNickname = coSvc.getUsersNickname();
		String inputNickname = element.getAsJsonObject().get("nickname").getAsString();
		
		boolean result = true;
		if(usersNickname.contains(inputNickname)) {
			result = false;
		}
		Map<String,Object> response = new HashMap<>();
		response.put("result", result);
		return response;
	}
	
	// 비밀번호 변경 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/updatePw")
	public Map<String,Object> doUpdatePassword(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(dataBody);
      
        String password = element.getAsJsonObject().get("currentPw").getAsString();
        String newPassword = element.getAsJsonObject().get("newPw").getAsString();
        int userIdx = (int)element.getAsJsonObject().get("userIdx").getAsInt();
      
        System.out.println(password+" / "+newPassword+" / "+userIdx+" / ");
      
        int result = 0;
        try {
        	result = coSvc.updatePw(password, newPassword, userIdx);
        } catch(Exception e) { e.printStackTrace(); }
      Map<String,Object> response = new HashMap<>();
      response.put("result", result==1);
      return response;
   }
	
	// 닉네임 변경 (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/updateNickname")
	public Map<String,Object> doUpdateNickname(@RequestBody String dataBody){
		JsonParser parser = new JsonParser();
	    JsonElement element = parser.parse(dataBody);
	      
	    String newNickname = element.getAsJsonObject().get("nickname").getAsString();
	    int userIdx = (int)element.getAsJsonObject().get("userIdx").getAsInt();
	      
	    int result = 0;
	    List<String> Usersnickname = coSvc.getUsersNickname();

	    if(Usersnickname.contains(newNickname)) {   // 닉네임 중복체크
	    	result = 0;
	    } else { result = coSvc.updateNickname(newNickname, userIdx); }
	      
	    Map<String,Object> response = new HashMap<>();
	    response.put("result", result==1);
	    return response;
	}
	
	// 서버에서 클라이언트에게 알림 보내기 (작업자 :서수련)
	// 파라미터 : -
	@MessageMapping("/alarm.send")
	public void sendAlarm(HashMap<String, Object> receive) {
		int sender = Integer.parseInt(receive.get("sender") + "");
		String type = receive.get("type") + "";
		Map<String, Object> alarmData = new HashMap<>();
		switch(type){
		case "reply" : 
			int postIdx = Integer.parseInt(receive.get("postIdx") + "");
			int userIdx = coSvc.addReplyAlarm(sender, postIdx);
			alarmData.put("type", "reply");
			messagingTemplate.convertAndSend("/topic/alarm/" + userIdx, alarmData);
			break;	
		}
	}
	
// SNS AJAX ----------------------------------------------------------------------------------------------------
	
	// 게시물에 하트 누르거나 취소 했을 때 상태 저장 (작업자 : 서수련)
	// 파라미터 : 게시물 기본키(postIdx), 누른 유저 기본키(userIdx), 현재 하트 상태(check)
	@PostMapping("sns/updatePostHeart")
	public int updatePostHeart(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int postIdx = element.getAsJsonObject().get("postIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		boolean check = element.getAsJsonObject().get("isCheck").getAsBoolean();
		int result = 0;
		if(check) { // 하트를 누름!
			sSvc.heartToPost(postIdx, userIdx);
			result = 1;
		} else { // 하트 취소함
			sSvc.deletePostHeart(postIdx, userIdx);
			result = -1;
		}
		return result;
	}
	
	// 게시물 상세 (작업자 : 서수련)
	// 파라미터 : 게시물 기본키(postIdx), 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/loadPostDetail")
	public HashMap<String, Object> loadPostDetail(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int postIdx = element.getAsJsonObject().get("postIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		return sSvc.loadPostDetail(postIdx, userIdx);
	}
	
	// 게시물에 댓글 달기 (작업자 : 서수련)
	// 파라미터 : 댓글 단 게시물 기본키(postIdx), 댓글 단 유저 기본키(userIdx)
	@PostMapping("sns/addReply")
	public HashMap<String, Object> addReply(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int postIdx = element.getAsJsonObject().get("postIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		String content = element.getAsJsonObject().get("content").getAsString();
		return sSvc.addReply(postIdx, userIdx, content);
	}
	
	// 댓글에 하트 누르기 (작업자 : 서수련)
	// 파라미터 : 하트 누른 댓글 기본키(replyIdx), 하트 누른 유저 기본키(userIdx), 하트 눌림 상태(check)
	@PostMapping("sns/updateReplyHeart")
	public int updateReplyHeart(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int replyIdx = element.getAsJsonObject().get("replyIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		boolean check = element.getAsJsonObject().get("isCheck").getAsBoolean();
		int result = 0;
		if(check) { // 하트를 누름!
			sSvc.heartToReply(replyIdx, userIdx);
			result = 1;
		} else { // 하트 취소함
			sSvc.deleteReplyHeart(replyIdx, userIdx);
			result = -1;
		}
		return result;
	}
	
	// 댓글 삭제하기 (작업자 : 서수련)
	// 파라미터 : 댓글 기본키(replyIdx), 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/deleteReply")
	public void deleteReply(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int replyIdx = element.getAsJsonObject().get("replyIdx").getAsInt();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		sSvc.deleteReply(replyIdx, userIdx);
	}
	
	// 알림 목록 보기 (작업자 : 서수련)
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/loadAlarmList")
	public List<AlarmDto> getAlarmList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		coSvc.readAllAlarm(userIdx);
		return coSvc.getAlarmDetailList(userIdx);
	}
	
	// 팔로잉 팔로워 리스트 보기 (작업자 : 이경미)
	// 파라미터 : 팔로잉인지 팔로워인지(type), 유저 기본키(userIdx)
	@PostMapping("sns/getFollowList")
	public List<UsersDto> getFollowList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		String type = element.getAsJsonObject().get("type").getAsString();
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		if(type.equals("following")) return sSvc.getFollowerList(userIdx);
		else return sSvc.getFollowerList(userIdx);
	}
	
	// 팔로우 하기 (작업자 : 서수련)
	// 파라미터 : 팔로워대상 유저 기본키(userIdx), 팔로우하는 유저 기본키(loginUserIdx)
	@PostMapping("sns/addFollower")
	public void addFollower(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int loginUserIdx = element.getAsJsonObject().get("loginUserIdx").getAsInt();
		sSvc.addFollower(loginUserIdx, userIdx);
	}
	
	// 팔로우하고 있는지 아닌지 확인 (작업자 : 서수련)
	// 파라미터 : 확인 대상 유저 기본키(userIdx), 로그인한 유저 기본키(loginUserIdx)
	@PostMapping("sns/checkFollowing")
	public boolean checkFollowing(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int loginUserIdx = element.getAsJsonObject().get("loginUserIdx").getAsInt();
		return sSvc.checkFollowing(loginUserIdx, userIdx);
	}
	
	// 언팔로우 하기 (작업자 : 이경미)
	// 파라미터 : 취소할 유저 기본키(userIdx), 로그인한 유저 기본키(loginUserIdx) 
	@PostMapping("sns/unfollow")
	public void unfollow(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
		int loginUserIdx = element.getAsJsonObject().get("loginUserIdx").getAsInt();
		sSvc.unfollow(loginUserIdx, userIdx);
	}
	
	// 게시물 삭제 (작업자 : 서수련)
	// 파라미터 : 게시물 기본키(postIdx)
	@PostMapping("sns/deletePost")
	public void deletePost(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int postIdx = element.getAsJsonObject().get("postIdx").getAsInt();
		sSvc.deletePost(postIdx);
	}
    
	// 게시물 기본정보 (작업자 : 서수련)
    // 파라미터 : 로그인한 유저 기본키(userIdx)
	@PostMapping("sns/getPostDtoByPostIdx")
	public PostDto getPostDtoByPostIdx(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int postIdx = element.getAsJsonObject().get("postIdx").getAsInt();
		return sSvc.getPostDtoByPostIdx(postIdx);
	}
    
// PLANNER AJAX ----------------------------------------------------------------------------------------------- 
	
	// 플래너에서 명소 목록 조회 (검색) (작업자 : 서수련)
	// 파라미터 : 도시 기본키(cityIdx), 검색어(search), 다음 페이지 토큰(token)
	@PostMapping("/placeList")
	public ResponsePlaces getPlaceListBySearh(@RequestBody String dataBody) throws Exception {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int cityIdx = element.getAsJsonObject().get("cityIdx").getAsInt();	
		String search = element.getAsJsonObject().get("search").getAsString();	
		String token = element.getAsJsonObject().get("token").getAsString(); 
		return pSvc.getPlaceListBySearh(cityIdx, search, token);
	}
	
	// 저장되어 있는 명소인지 아닌지 확인(없으면 저장) (작업자 : 이경미)
	// 파라미터 : 호텔인지 명소인지(target), 
	//		   호텔{도시 기본키(cityIdx), 호텔이름(hotel), 위도(lat), 경도(lng)}, 
	//		   가게{도시 기본키(cityIdx), 명소이름(name), 명소사진(pic), 위도(lat), 경도(lng), 별점(rate), 리뷰수(review)}
	@PostMapping("/checkPlace")
	public List<Integer> checkAndSavePlace(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		String target = element.getAsJsonObject().get("target").getAsString();
		List<Integer> placeIdxList = new ArrayList<>();
		if(target.equals("hotel")) {
			PlaceDto dto = new PlaceDto();
			dto.setCityIdx(element.getAsJsonObject().get("cityIdx").getAsInt());
			dto.setName(element.getAsJsonObject().get("hotel").getAsString());
			dto.setLat(element.getAsJsonObject().get("lat").getAsDouble());
			dto.setLng(element.getAsJsonObject().get("lng").getAsDouble());
			int placeIdx = coSvc.checkAndSavePlace(dto);
			placeIdxList.add(placeIdx);
		} else if(target.equals("store")) {
			JsonArray list = element.getAsJsonObject().get("placeList").getAsJsonArray();
			for(JsonElement e : list) {
				PlaceDto dto = new PlaceDto();
				dto.setCityIdx(e.getAsJsonObject().get("cityIdx").getAsInt());
				dto.setName(e.getAsJsonObject().get("name").getAsString());
				dto.setPic(e.getAsJsonObject().get("pic").getAsString());
				dto.setLat(e.getAsJsonObject().get("lat").getAsDouble());
				dto.setLng(e.getAsJsonObject().get("lng").getAsDouble());
				dto.setPlaceRate(e.getAsJsonObject().get("rate").getAsDouble());
				dto.setPlaceReviewCount(e.getAsJsonObject().get("review").getAsInt());
				int placeIdx = coSvc.checkAndSavePlace(dto);
				placeIdxList.add(placeIdx);
			}
		}
		return placeIdxList;
	}
	
	// 선택된 명소로 저장하기 (작업자 : 이경미)
	// 파라미터 : 명소 기본키(placeIdx), 호텔인지 명소인지(target), 여행일차 기본키(dayIdx)
	@PostMapping("/saveUserPlace")
	public void saveUserPlace(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		String target = element.getAsJsonObject().get("target").getAsString();
		JsonArray list = element.getAsJsonObject().get("placeIdx").getAsJsonArray();
		if(target.equals("hotel")) {
			int placeIdx = list.get(0).getAsInt();
			int firstDayIdx = element.getAsJsonObject().get("firstDayIdx").getAsInt();
			int travelDate = element.getAsJsonObject().get("travelDate").getAsInt();
			for(int i=1; i<=travelDate; i++) {
				coSvc.saveUserPlace(firstDayIdx++, placeIdx, 0);
			}
		} else {
			int dayIdx = element.getAsJsonObject().get("dayIdx").getAsInt();
			for(int i=0; i<list.size(); i++) {
				int placeIdx = list.get(i).getAsInt();
				coSvc.saveUserPlace(dayIdx, placeIdx, i + 1);
			}
		}
	}
	
	// 해당 유저의 n월에 어떤 여행일정이 있는지 확인 (풀캘린더) (작업자 : 최호준)
	// 파라미터 : -
	@PostMapping("/getPlannerByMonth") 
	public List<Map<String,Object>> getPlannerByMonth(@RequestBody String dataBody, HttpSession session) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		
		int userIdx = (Integer)session.getAttribute("loginUserIdx");
		int year = element.getAsJsonObject().get("year").getAsInt();
		int month = element.getAsJsonObject().get("month").getAsInt();
		
		String plannerDateStr = "" + year + (month <= 9 ? "0" + month : month);
		Date plannerDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		    plannerDate = sdf.parse(plannerDateStr);  // ← 여기서 Date로 변환
		} catch (Exception e) { e.printStackTrace(); }
		return pSvc.getTravelDateForCalendar(plannerDate,userIdx);
	}
	
	// 동행인 초대를 위한 이메일 검색 (작업자 : 이경미)
	// 파라미터 : 이메일(email)
	@PostMapping("/getUserByEmail")
	@ResponseBody
	public UsersDto getUserByEmail(@RequestBody Map<String, String> data) {
	    String email = data.get("email");
	    UsersDto user = coSvc.getUserByEmail(email);
	    System.out.println("검색 이메일: " + email);
	    System.out.println("검색 결과 user: " + user);
	    
	    if(user == null) {
	        System.out.println("사용자 없음");
	        return new UsersDto(-1, "", "", "", "");
	    }
	    return user;
	}
	
	// 동행인 정보 가져오기 (작업자 : 이경미)
	// 파라미터 : -
	@PostMapping("/planner/getPartnerList")
	public List<PartnerInfoDto> getPartnerList(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int plannerIdx = element.getAsJsonObject().get("plannerIdx").getAsInt();
		List<PartnerInfoDto> list = pSvc.getPartnerInfoList(plannerIdx);
		return list;
	}
		
	// 동행인 삭제하기 (작업자 : 이경미)
	// 파라미터 : -
	@PostMapping("/planner/deletePartner")
	public String deletePartner(@RequestBody Map<String, Integer> data) {
		int plannerIdx = data.get("plannerIdx");
		int userIdx = data.get("userIdx");
		int result = pSvc.deletePartner(plannerIdx, userIdx);
		return result > 0 ? "success" : "fail";
	}
		
	// 동행인 추가하기 (작업자 : 이경미)
	// 파라미터 : 이메일(email), 닉네임(nickname), 인증코드(code)
	@PostMapping("/planner/addPartner")
	public ResponseEntity<String> addPartner(@RequestBody PartnerDto dto) {
		try {
			String code = GenerateInviteCode.main();
			dto.setCode(code);
			pSvc.addPartner(dto);
			
			UsersDto user = coSvc.getUserInfo(dto.getUserIdx());
			System.out.println("[addPartner] 조회된 사용자: " + user);
			
			if(user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
			}
			String email = user.getEmail();
			String nickname = user.getNickname();
			
		    emailService.sendInvitationEmail(email, nickname, code);
			System.out.println("[addPartner] 이메일 발송 완료");
			return ResponseEntity.ok("초대가 성공적으로 추가되었습니다. 메일이 발송되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("초대 추가 중 오류 발생");
		}
	}
	
	// 이메일을 받은 동행인이 초대 수락 버튼 클릭 시 (작업자 : 이경미)
	// 파라미터 : -
	@PostMapping("/planner/acceptPartner")
	public ResponseEntity<String> acceptPartner(@RequestBody PartnerDto dto) {
		try {
			pSvc.acceptPartner(dto);
			return ResponseEntity.ok("초대가 수락되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("초대 추가 중 오류 발생");
		}
	}
		
	// 명소별 작성된 메모 내용 가져오기 (작업자 : 이경미)
	// 파라미터 : 선택된 명소 기본키(userPlaceIdx)
	@PostMapping("/planner/getMemoDetailList")
	public List<MemoDto> getMemoDetailList(@RequestBody Map<String, Integer> data, HttpSession session) {
		UsersDto loginUser = (UsersDto) session.getAttribute("loginUser");
	    if (loginUser == null) {
	    	System.out.println("로그인 정보 없음");
	    	return List.of();
	    }
		Integer userPlaceIdx = data.get("userPlaceIdx");
	    if (userPlaceIdx == null) {
	    	return List.of();
	    }
	    List<MemoDto> memo = pSvc.getMemoDetailList(userPlaceIdx);
	    for(MemoDto m : memo) {
	    	System.out.println(m.getNickname() + " : " + m.getContent());
	    }
	    return memo;
	}
		
	// 플래너 제목 수정하기 (작업자 : 이경미)
	// 파라미터 : 제목(title), 플래너 기본키(plannerIdx)
	@PostMapping("/planner/updateTitle")
	@ResponseBody
	public String updateTitle(@RequestBody Map<String, Object> data) {
		String title = (String)data.get("title");
		int plannerIdx = Integer.parseInt(data.get("plannerIdx").toString());
		int result = pSvc.updateTitle(title, plannerIdx);
		return result > 0 ? "success" : "fail";
	}

	// 메모 작성하기(insertMemo) 및 수정하기(updateMemo) (작업자 : 이경미)
	// 파라미터 : 선택된 명소 기본키(userPlaceIdx), 유저 기본키(userIdx), 메모 내용(content)
	@PostMapping("/planner/updateMemo")
	@ResponseBody		
	public String updateMemo(@RequestBody Map<String, Object> data) {
		try {
			int userPlaceIdx = Integer.parseInt(data.get("userPlaceIdx").toString());
			int userIdx = Integer.parseInt(data.get("userIdx").toString());
			String content = (String)data.get("content");
			
			boolean exists = pSvc.memoExists(userPlaceIdx, userIdx);
			
			int result;
			if(exists) {
				result = pSvc.updateMemo(userPlaceIdx, userIdx, content);
				return result > 0 ? "success" : "fail";
			} else {
				result = pSvc.insertMemo(userPlaceIdx, userIdx, content);
				return result > 0 ? "success" : "fail";
			}
		} catch(Exception e) {
			e.printStackTrace(); return "fail";
		}
	}
		
	// 메모 삭제하기 (작업자 : 이경미)
	// 파라미터 : -
	@PostMapping("/planner/deleteMemo")
	public String deleteMemo(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userPlaceIdx = element.getAsJsonObject().get("userPlaceIdx").getAsInt();
		int result = pSvc.deleteMemo(userPlaceIdx);
		return result > 0 ? "success" : "fail";
	}
		
	// 명소 삭제하기 (작업자 : 이경미)
	// 파라미터 : -
	@PostMapping("/planner/deleteUserPlace")
	public int deleteUserPlace(@RequestBody String dataBody) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(dataBody);
		int userPlaceIdx = element.getAsJsonObject().get("userPlaceIdx").getAsInt();
		int dto = pSvc.deleteUserPlace(userPlaceIdx);
		return dto;
	}

	// 플래너에 저장된 일차별 명소 순서 바꾸기 (작업자 : 이경미)
	// 파라미터 : 과거 명소(oldDayIdx), 현재 명소(newDayIdx)
	@PostMapping("/planner/updateDayIdxByOldDayIdx")
	@ResponseBody
	public ResponseEntity<String> updateDayIdxByOldDayIdx(@RequestBody List<Map<String, Integer>> dayOrderList) {
		try {
	        if(dayOrderList == null || dayOrderList.isEmpty()) {
	            return ResponseEntity.badRequest().body("Invalid parameters");
	        }
	        for (Map<String, Integer> data : dayOrderList) {
	            Integer oldDayIdx = data.get("oldDayIdx");
	            Integer newDayIdx = data.get("newDayIdx");
	            
	            if (oldDayIdx == null || newDayIdx == null) {
	                return ResponseEntity.badRequest().body("Invalid data in list");
	            }
	            Map<String, Integer> param = new HashMap<>();
	            param.put("oldDayIdx", oldDayIdx);
	            param.put("newDayIdx", newDayIdx);
	            
	            pSvc.updateDayIdxByOldDayIdx(param);
	        }
	        return ResponseEntity.ok("success");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
	
	// 일차별 명소 가져오기 (작업자 : 이경미)
	// 파라미터 : 여행 일차 기본키(dayIdx)
	@PostMapping("/planner/getPlacesByDayIdx")
	@ResponseBody
	public List<UserPlaceNameDto> getPlacesByDayIdx(@RequestBody Map<String, Integer> body) {
		int dayIdx = body.get("dayIdx");
		return pSvc.getPlacesByDayIdx(dayIdx);
	}
	
	// 일차별 명소 가져오기 (작업자 : 이경미)
	// 파라미터 : 여행 일차 기본키(dayIdx)
	@GetMapping("/getPlacesByDayIdx")
	@ResponseBody
	public List<UserPlaceNameDto> getPlacesByDayIdx(@RequestParam int dayIdx) {
		return pSvc.getPlacesByDayIdx(dayIdx);
	}
		
	// 플래너에 저장된 일차별 안에 명소 순서 바꾸기 (작업자 : 이경미)
	// 파라미터 : UserPlaceDto
	@PostMapping("/planner/updateUserPlaceDayIdx")
	public ResponseEntity<String> updateUserPlaceDayIdx(@RequestBody UserPlaceDto dto) {
        if (dto == null || dto.getUserPlaceIdx() <= 0 || dto.getDayIdx() <= 0 || dto.getSaveIdx() < 0) {
            return ResponseEntity.badRequest().body("Invalid parameters");
        }
        try {
            int result = pSvc.updateUserPlaceDayIdx(dto);
            if (result > 0) {
                return ResponseEntity.ok("success");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No rows updated");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }
		
	// 플래너 일정 변경 내용 저장 (작업자 : 이경미)
	// 파라미터 : 여행 일차 기본키(dayIdx), 명소별 저장 순서(saveIdx)
	@PostMapping("/planner/updateDayOrder")
    public ResponseEntity<String> updateDayOrder(@RequestBody List<Map<String, Integer>> dayOrderList) {
        if (dayOrderList == null || dayOrderList.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty day order list");
        }
        try {
            for (Map<String, Integer> day : dayOrderList) {
            	System.out.println("day map = " + day);
                Object dayIdxObj = day.get("dayIdx");
                Object daySaveIdxObj = day.get("daySaveIdx");
                if (dayIdxObj == null || daySaveIdxObj == null) {
                    System.out.println("dayIdx 또는 daySaveIdx가 null 입니다!");
                    return ResponseEntity.badRequest().body("Invalid dayIdx or daySaveIdx");
                }
                int dayIdx = ((Number) dayIdxObj).intValue();
                int daySaveIdx = ((Number) daySaveIdxObj).intValue();

                Map<String, Integer> param = new HashMap<>();
                param.put("oldDayIdx", dayIdx);
                param.put("newSaveIdx", daySaveIdx);

                int result = pSvc.updateDayIdxByOldDayIdx(param);
                System.out.println("update result = " + result);
            }
            return ResponseEntity.ok("success");
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("fail: " + e.getMessage());
        }
    }
	
	// 유저가 선택한 명소 불러오기 (플래너 리뷰) (작업자 : 최호준)
	// 파라미터 : -
    @PostMapping("/loadUserPlace")
    public Map<String, Object> loadUserPlace(@RequestBody String dataBody) {
       JsonParser parser = new JsonParser();
       JsonElement element = parser.parse(dataBody);
       
       int plannerIdx = element.getAsJsonObject().get("plannerIdx").getAsInt();
       int date = element.getAsJsonObject().get("date").getAsInt() - 1;
       return pSvc.loadUserplace(plannerIdx, date);
    }
    
    // 선택한 명소 정보 (플래너 리뷰) (작업자 : 최호준)
	// 파라미터 : -
    @PostMapping("/getUserPlaceDetail")
    public Map<String, Object> getUserPlaceDetail(@RequestBody String dataBody) {
       JsonParser parser = new JsonParser();
       JsonElement element = parser.parse(dataBody);
       
       int userPlaceIdx = element.getAsJsonObject().get("userPlaceIdx").getAsInt();
       return pSvc.getPlaceDetailAsMap(userPlaceIdx);
    }
    
    // 플래너리뷰에 댓글쓰기 (작업자 : 최호준)
	// 파라미터 : -
    @PostMapping("/wirteReviewComment") 
    public Map<String,Object> addReviewComment(@RequestBody String dataBody, Model model) {
       JsonParser parser = new JsonParser();
       JsonElement element = parser.parse(dataBody);
       
       String content = element.getAsJsonObject().get("content").getAsString();
       int plannerIdx = element.getAsJsonObject().get("plannerIdx").getAsInt();
       int userIdx = element.getAsJsonObject().get("userIdx").getAsInt();
       int rate = element.getAsJsonObject().get("rate").getAsInt();
       
       Map<String,Object> result = new HashMap<>(); 
       if(userIdx == -1) {
          result.put("success", false);
          result.put("loginIdx_msg", "로그인 후 사용가능합니다.");
       } else {
          Map<String,Object> reviewComment = pSvc.addReviewComment(plannerIdx, userIdx, content, rate);
          result.put("success", true);
          result.put("loginIdx_msg", "댓글 작성 완료");
          result.put("data",reviewComment);
       }
       return result;
    }
}
	

