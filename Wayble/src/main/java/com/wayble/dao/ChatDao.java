package com.wayble.dao;

import java.util.List;

import com.wayble.dto.ChatDetailDto;
import com.wayble.dto.MessageDto;
import com.wayble.dto.MessageInfoDto;
import com.wayble.dto.PrivateChatUserDto;


public interface ChatDao {
	// 작업자 : 서수련 ---------------------------------------------------------------------------------------------
	List<ChatDetailDto> getMyOpenChatList(int userIdx);	// 내가 참여한 오픈채팅 목록
	MessageDto getLastMessage(int chatRoomIdx);	// 마지막으로 보낸 메시지
	List<PrivateChatUserDto> getMyChatList(int userIdx); // 내가 참여한 1:1 채팅방 목록
	int countUnread(int userIdx, int chatRoomIdx); // 안 읽은 메시지 개수
	List<MessageInfoDto> getMessage(int chatRoomIdx); // 채팅방에 저장되어 있는 메시지들
	int saveMessage (int chatRoomIdx, int userIdx, String content);	// 메시지 저장하기
	void addUnread(int userIdx, int messageIdx); // 안 읽은 알림 추가하기
	List<Integer> getChatMemberByChatRoomIdx(int chatRoomIdx); // 채팅방 인덱스로 참여하고 있는 사람 불러오기
	void readAllMessage(int chatRoomIdx, int userIdx); // 안 읽었던 알림 읽기
	List<ChatDetailDto> loadOpenChatList();	// 전체 오픈채팅방 목록
	List<ChatDetailDto> loadOpenChatListBySearch(String search); // 검색으로 오픈채팅방 찾기
	ChatDetailDto getChatDetailDtoByChatRoomIdx(int chatRoomIdx); // 채팅방 인덱스로 채팅방 상세 얻기
	int makeOpenChat(ChatDetailDto dto); // 오픈채팅방 만들기
	void addChatMember(int chatRoomIdx, int userIdx); // 채팅방 참여하기
	void deleteChatMember(int chatRoomIdx, int userIdx); // 채팅방 나가기
	PrivateChatUserDto getPrivateChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx); // 채팅방 인덱스로 1:1 채팅방 상세
	ChatDetailDto getOpenChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx); // 채팅방 인덱스로 오픈채팅방 상세
	int makePrivateChat(int userIdx); // 1:1 채팅방 만들기
}
