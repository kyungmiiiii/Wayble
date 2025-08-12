package com.wayble.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wayble.dto.ChatDetailDto;
import com.wayble.dto.MessageDto;
import com.wayble.dto.MessageInfoDto;
import com.wayble.dto.PrivateChatUserDto;

// 작업자 : 서수련 ----------------------------------------------------------------

@Repository
public class ChatDaoImpl implements ChatDao {
	@Autowired
	SqlSession sqlSession;

	// 내가 참여한 오픈채팅 목록
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : ChatDetailDto
	@Override
	public List<ChatDetailDto> getMyOpenChatList(int userIdx) {
		return sqlSession.selectList("ChatMapper.getMyOpenChatList", userIdx);
	}
	
	// 마지막으로 보낸 메시지
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : MessageDto
	@Override
	public MessageDto getLastMessage(int chatRoomIdx) {
		return sqlSession.selectOne("ChatMapper.getLastMessage", chatRoomIdx);
	}

	// 내가 참여한 1:1 채팅방 목록
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : PrivateChatUserDto
	@Override
	public List<PrivateChatUserDto> getMyChatList(int userIdx) {
		return sqlSession.selectList("ChatMapper.getMyChatList", userIdx);
	}
	
	// 안 읽은 메시지 개수
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : 안 읽은 메시지 수
	@Override
	public int countUnread(int userIdx, int chatRoomIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		return sqlSession.selectOne("ChatMapper.countUnread", map);
	}

	// 채팅방에 저장되어 있는 메시지들
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : MessageInfoDto
	@Override
	public List<MessageInfoDto> getMessage(int chatRoomIdx) {
		return sqlSession.selectList("ChatMapper.getMessage", chatRoomIdx);
	}

	// 메시지 저장하기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 보낸 유저 기본키(userIdx), 메시지(content)
	// 리턴값 : 메시지 기본키(messageIdx)
	@Override
	public int saveMessage(int chatRoomIdx, int userIdx, String content) {
		MessageDto dto = new MessageDto();
		dto.setChatRoomIdx(chatRoomIdx);
		dto.setUserIdx(userIdx);
		dto.setContent(content);
		sqlSession.insert("ChatMapper.saveMessage", dto);
		return dto.getMessageIdx();
	}

	// 안 읽은 알림 추가하기
	// 파라미터 : 알림 받은 유저 기본키(userIdx), 메시지 기본키(messageIdx)
	// 리턴값 : -
	@Override
	public void addUnread(int userIdx, int messageIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("messageIdx", messageIdx);
		sqlSession.insert("ChatMapper.addUnread", map);
	}

	// 채팅방 인덱스로 참여하고 있는 사람 불러오기
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
	// 리턴값 : 채팅 멤버 유저 기본키
	@Override
	public List<Integer> getChatMemberByChatRoomIdx(int chatRoomIdx) {
		return sqlSession.selectList("ChatMapper.getChatMemberByChatRoomIdx", chatRoomIdx);
	}

	// 안 읽었던 알림 읽기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 로그인한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void readAllMessage(int chatRoomIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		sqlSession.delete("ChatMapper.readAllMessage", map);
	}

	// 전체 오픈채팅방 목록
	// 파라미터 : -
	// 리턴값 : ChatDetailDto
	@Override
	public List<ChatDetailDto> loadOpenChatList() {
		return sqlSession.selectList("ChatMapper.loadOpenChatList");
	}

	// 검색으로 오픈채팅방 찾기
	// 파라미터 : 검색어(search)
    // 리턴값 : ChatDetailDto
	@Override
	public List<ChatDetailDto> loadOpenChatListBySearch(String search) {
		search = "%" + search + "%";
		return sqlSession.selectList("ChatMapper.loadOpenChatListBySearch", search);
	}

	// 채팅방 인덱스로 채팅방 상세 얻기
	// 파라미터 : 채팅방 기본키(chatRoomIdx)
    // 리턴값 : ChatDetailDto
	@Override
	public ChatDetailDto getChatDetailDtoByChatRoomIdx(int chatRoomIdx) {
		return sqlSession.selectOne("ChatMapper.getChatDetailDtoByChatRoomIdx", chatRoomIdx);
	}

	// 오픈채팅방 만들기
	// 파라미터 : 채팅방 기본 정보(dto)
    // 리턴값 : 채팅방 기본키(chatRoomIdx)
	@Override
	public int makeOpenChat(ChatDetailDto dto) {
		sqlSession.insert("ChatMapper.makeOpenChat", dto);
		return dto.getChatRoomIdx();
	}

	// 채팅방 참여하기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 참여하는 유저 기본키(userIdx)
    // 리턴값 : -
	@Override
	public void addChatMember(int chatRoomIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		sqlSession.insert("ChatMapper.addChatMember", map);
	}

	// 채팅방 나가기
	// 파라미터 : 채팅방 기본키(chatRoomIdx), 나가는 유저 기본키(userIdx)
    // 리턴값 : -
	@Override
	public void deleteChatMember(int chatRoomIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		sqlSession.delete("ChatMapper.deleteChatMember", map);
	}

	// 채팅방 인덱스로 1:1 채팅방 상세
	// 파라미터 : 로그인한 유저 기본키(userIdx), 1:1 채팅방 기본키(chatRoomIdx)
    // 리턴값 : PrivateChatUserDto
	@Override
	public PrivateChatUserDto getPrivateChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		return sqlSession.selectOne("ChatMapper.getPrivateChatRoomByChatRoomIdx", map);
	}

	// 채팅방 인덱스로 오픈채팅방 상세
	// 파라미터 : 로그인한 유저 기본키(userIdx), 오픈채팅방 기본키(chatRoomIdx)
    // 리턴값 : ChatDetailDto
	@Override
	public ChatDetailDto getOpenChatRoomByChatRoomIdx(int userIdx, int chatRoomIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("userIdx", userIdx);
		map.put("chatRoomIdx", chatRoomIdx);
		return sqlSession.selectOne("ChatMapper.getOpenChatRoomByChatRoomIdx", map);
	}

	// 1:1 채팅방 만들기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
    // 리턴값 : 채팅방 기본키(chatRoomIdx)
	@Override
	public int makePrivateChat(int userIdx) {
		System.out.println("userIdx!!!" + userIdx);
		ChatDetailDto dto = new ChatDetailDto();
		dto.setCaptainUserIdx(userIdx);
		sqlSession.insert("ChatMapper.makePrivateChat", dto);
		return dto.getChatRoomIdx();
	}

}
