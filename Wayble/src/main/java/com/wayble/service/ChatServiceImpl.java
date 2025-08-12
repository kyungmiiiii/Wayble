package com.wayble.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wayble.dao.ChatDao;
import com.wayble.dao.CommonDao;
import com.wayble.dto.ChatDetailDto;
import com.wayble.dto.MessageInfoDto;
import com.wayble.dto.PrivateChatUserDto;

@Service
public class ChatServiceImpl implements ChatService {
	@Autowired
	ChatDao dao;
	@Autowired
	CommonDao cDao;

	// 내가 참여한 오픈채팅 미리보기 목록
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [채팅방상 상세정보(chatRoomIdx), 마지막 메시지(lastMessage), 안 읽은 알림수(unread)]
	@Override
	public List<HashMap<String, Object>> loadMyOpenChatList(int userIdx) {
		List<HashMap<String, Object>> list = new ArrayList<>();
		for(ChatDetailDto c : dao.getMyOpenChatList(userIdx)) {
			HashMap<String, Object> map = new HashMap<>();
			int chatRoomIdx = c.getChatRoomIdx();
			map.put("chatRoom", c);
			map.put("lastMessage", dao.getLastMessage(chatRoomIdx));
			map.put("unread",dao.countUnread(userIdx, chatRoomIdx));
			list.add(map);
		}
		return list;
	}

	// 내가 참여한 1:1 채팅방 미리보기 목록
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 유저 기본키(userIdx), 채팅방 기본키(chatRoomIdx), 프로필(profile), 닉네임(nickname), 마지막 메시지(lastLog), 안 읽은 메시지(unread)
	@Override
	public List<HashMap<String, Object>> loadMyChatList(int userIdx) {
		List<HashMap<String, Object>> list = new ArrayList<>();
		for(PrivateChatUserDto dto : dao.getMyChatList(userIdx)) {
			HashMap<String, Object> map = new HashMap<>();
			int cRIdx = dto.getChatRoomIdx();
			map.put("userIdx", dto.getUserIdx());
			map.put("chatRoomIdx", cRIdx);
			map.put("profile", dto.getProfile());
			map.put("nickname", dto.getNickname());
			map.put("lastLog", dao.getLastMessage(cRIdx));
			map.put("unread", dao.countUnread(userIdx, cRIdx));
			list.add(map);
		}
		return list;
	}
	
	// 메시지 불러오기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 메시지 기본 정보(MessageInfoDto)
	@Override
	public List<MessageInfoDto> loadMessage(int chatRoomIdx) {
		return dao.getMessage(chatRoomIdx);
	}

	// 메시지 저장하기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 보낸 유저 기본키(userIdx), 메시지(content)
	// 리턴값 : 메시지 기본 정보(MessageInfoDto)
	@Override
	public int saveMessage(int chatRoomIdx, int userIdx, String content) {
		return dao.saveMessage(chatRoomIdx, userIdx, content);
	}

	// 채팅방 인덱스로 참여하고 있는 사람 불러오기
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : [채팅 멤버 유저 기본키]
	@Override
	public List<Integer> getChatMemberByChatRoomIdx(int chatRoomIdx) {
		return dao.getChatMemberByChatRoomIdx(chatRoomIdx);
	}

	// 안 읽었던 알림 읽기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 로그인한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void readAllMessage(int chatRoomIdx, int userIdx) {
		dao.readAllMessage(chatRoomIdx, userIdx);
	}

	// 안 읽은 알림 추가하기
	// 파라미터 : 알림 받은 유저 기본키(userIdx), 메시지 기본키(messageIdx)
	// 리턴값 : -
	@Override
	public void addUnread(int userIdx, int messageIdx) {
		dao.addUnread(userIdx, messageIdx);
	}

	// 전체 오픈채팅방 미리보기 목록
	// 파라미터 : -
	// 리턴값 : 채팅방정보(ChatDetailDto)
	@Override
	public List<HashMap<String, Object>> loadOpenChatList() {
		List<HashMap<String, Object>> list = new ArrayList<>();
		for(ChatDetailDto dto : dao.loadOpenChatList()) {
			HashMap<String, Object> map = new HashMap<>();
			int chatRoomIdx = dto.getChatRoomIdx();
			map.put("dto", dto);
			map.put("lastMessage", dao.getLastMessage(chatRoomIdx));
			list.add(map);
		}
		return list;
	}

	// 검색으로 오픈채팅방 찾기
	// 파라미터 : 검색어(search)
	// 리턴값 : [채팅방정보(dto), 미자막 메시지(lastMessage)]
	@Override
	public List<HashMap<String, Object>> loadOpenChatListBySearch(String search) {
		List<HashMap<String, Object>> list = new ArrayList<>();
		for(ChatDetailDto dto : dao.loadOpenChatListBySearch(search)) {
			HashMap<String, Object> map = new HashMap<>();
			int chatRoomIdx = dto.getChatRoomIdx();
			map.put("dto", dto);
			map.put("lastMessage", dao.getLastMessage(chatRoomIdx));
			list.add(map);
		}
		return list;
	}

	// 채팅방 인덱스로 채팅방 상세 얻기
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : 채팅방정보(ChatDetailDto)
	@Override
	public ChatDetailDto getChatDetailDtoByChatRoomIdx(int chatRoomIdx) {
		return dao.getChatDetailDtoByChatRoomIdx(chatRoomIdx);
	}

	// 채팅방 참여하기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 참여하는 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void joinOpenChat(int chatRoomIdx, int userIdx) {
		dao.addChatMember(chatRoomIdx, userIdx);
		String nickname = cDao.getUserInfo(userIdx).getNickname();
		dao.saveMessage(chatRoomIdx, 0, nickname + "님이 채팅방에 참여하였습니다.");
	}

	// 오픈채팅방 만들고 기본세팅
	// 파라미터 : 만든 유저 기본키(userIdx), 방제목(name), 카테고리(category), 방설명(description), 제한인원(limit)
	// 리턴값 : 채팅방 기본키(chatRoomIdx)
	@Override
	public int makeOpenChat(int captainUserIdx, String name, String category, String description, int limit) {
		ChatDetailDto dto = new ChatDetailDto();
		dto.setCaptainUserIdx(captainUserIdx);
		dto.setName(name);
		dto.setCategory(category);
		dto.setDescription(description);
		dto.setLimit(limit);
		int chatRoomIdx = dao.makeOpenChat(dto);
		dao.addChatMember(chatRoomIdx, captainUserIdx);
		return chatRoomIdx;
	}

	// 채팅방 나가기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 나가는 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deleteChatMember(int chatRoomIdx, int userIdx) {
		dao.deleteChatMember(chatRoomIdx, userIdx);
	}

	// 채팅방 인덱스로 1:1 채팅방 상세
	// 파라미터 : 로그인한 유저 기본키(userIdx), 1:1 채팅방 기본키(chatRoomIdx)
	// 리턴값 : 1:1채팅유저기본키(info), 마지막 메시지(lastLog), 안 읽은 메시지 수(unread)
	@Override
	public Map<String, Object> getPrivateChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("info", dao.getPrivateChatRoomByChatRoomIdx(userIdx, chatRoomIdx));
		ret.put("lastLog", dao.getLastMessage(chatRoomIdx));
		ret.put("unread", dao.countUnread(userIdx, chatRoomIdx));
		return ret;
	}
	
	// 채팅방 인덱스로 오픈채팅방 상세
	// 파라미터 : 로그인한 유저 기본키(userIdx), 오픈채팅방 기본키(chatRoomIdx)
	// 리턴값 : 오픈채팅유저기본키(info), 마지막 메시지(lastMessage), 안 읽은 메시지 수(unread)
	@Override
	public Map<String, Object> getOpenChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("info", dao.getOpenChatRoomByChatRoomIdx(userIdx, chatRoomIdx));
		map.put("lastMessage", dao.getLastMessage(chatRoomIdx));
		map.put("unread",dao.countUnread(userIdx, chatRoomIdx));
		return map;
	}

	// 1:1 채팅방 만들고 멤버 추가
	// 파라미터 : 로그인한 유저 기본키(userIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : 채팅방 기본키(chatRoomIdx)
	@Override
	public int startPrivateChat(int loginUserIdx, int userIdx) {
		int chatRoomIdx = dao.makePrivateChat(loginUserIdx);
		dao.addChatMember(chatRoomIdx, loginUserIdx);
		dao.addChatMember(chatRoomIdx, userIdx);
		return chatRoomIdx;
	}

	// 1:1 채팅방이 있는지 없는지 확인
	// 파라미터 : 로그인한 유저 기본키(userIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : 있으면 채팅방 기본키(chatRoomIdx) 없으면 -1
	@Override
	public int findPrivateChat(int loginUserIdx, int userIdx) {
		for(PrivateChatUserDto p : dao.getMyChatList(loginUserIdx)) {
			if(p.getUserIdx() == userIdx) { return p.getChatRoomIdx(); } 
		}
		return -1;
	}
	

}
