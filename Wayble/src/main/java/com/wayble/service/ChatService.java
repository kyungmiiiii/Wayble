package com.wayble.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wayble.dto.ChatDetailDto;
import com.wayble.dto.MessageInfoDto;

public interface ChatService {
	List<HashMap<String, Object>> loadMyOpenChatList(int userIdx); // 내가 참여한 오픈채팅 미리보기 목록
	List<HashMap<String, Object>> loadMyChatList(int userIdx); // 내가 참여한 1:1 채팅방 미리보기 목록
	List<MessageInfoDto> loadMessage(int chatRoomIdx); // 메시지 불러오기
	int saveMessage(int chatRoomIdx, int userIdx, String content); // 메시지 저장하기
	List<Integer> getChatMemberByChatRoomIdx(int chatRoomIdx); // 채팅방 인덱스로 참여하고 있는 사람 불러오기
	void readAllMessage(int chatRoomIdx, int userIdx); // 안 읽었던 알림 읽기
	void addUnread(int userIdx, int messageIdx);	// 안 읽은 알림 추가하기
	List<HashMap<String, Object>> loadOpenChatList();	// 전체 오픈채팅방 미리보기 목록
	List<HashMap<String, Object>> loadOpenChatListBySearch(String search);	// 검색으로 오픈채팅방 찾기
	ChatDetailDto getChatDetailDtoByChatRoomIdx(int chatRoomIdx);	// 채팅방 인덱스로 채팅방 상세 얻기
	void joinOpenChat(int chatRoomIdx, int userIdx);	// 채팅방 참여하기
	int makeOpenChat(int captainUserIdx, String name, String category, String description, int limit);	// 오픈채팅방 만들고 기본세팅
	void deleteChatMember(int chatRoomIdx, int userIdx);	// 채팅방 나가기
	Map<String, Object> getPrivateChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx);	// 채팅방 인덱스로 1:1 채팅방 상세
	Map<String, Object> getOpenChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx);	// 채팅방 인덱스로 오픈채팅방 상세
	int startPrivateChat(int loginUserIdx, int userIdx);	// 1:1 채팅방 만들고 멤버 추가
	int findPrivateChat(int loginUserIdx, int userIdx);	// 1:1 채팅방이 있는지 없는지 확인
}
