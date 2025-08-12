package com.wayble.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wayble.dto.PostDto;
import com.wayble.dto.PostInfoDto;
import com.wayble.dto.ReplyDetailDto;
import com.wayble.dto.UsersDto;

@Repository
public class SnsDaoImpl implements SnsDao {
	@Autowired
	SqlSession sqlSession;

//	작업자 : 서수련 ---------------------------------------------------------------
	// 팔로워 세기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 팔로워수
	@Override
	public int countFollower(int userIdx) {
		return sqlSession.selectOne("SnsMapper.countFollower", userIdx);
	}

	// 팔로잉 세기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 팔로잉수 
	@Override
	public int countFollowing(int userIdx) {
		return sqlSession.selectOne("SnsMapper.countFollowing", userIdx);
	}

	// 게시물 세기 
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 게시물 수 
	@Override
	public int countPost(int userIdx) {
		return sqlSession.selectOne("SnsMapper.countPost", userIdx);
	}

	// 전체보기로 게시된 게시물
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [게시물 정보](PostInfoDto)
	@Override
	public List<PostInfoDto> getOpenPostList(int userIdx) {
		return sqlSession.selectList("SnsMapper.getOpenPostList", userIdx);
	}

	// 내가 팔로우한 사람들의 게시물
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [게시물 정보](PostInfoDto)
	@Override
	public List<PostInfoDto> getFollowingPostList(int userIdx) {
		return sqlSession.selectList("SnsMapper.getFollowingPostList", userIdx);
	}

	// 게시물에 하트 누르기
	// 파라미터 : 게시물 기본키(postIdx), 누른 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void heartToPost(int postIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("postIdx", postIdx);
		map.put("userIdx", userIdx);
		sqlSession.selectList("SnsMapper.heartToPost", map);
	}

	// 게시물에 하트 취소
	// 파라미터 : 게시물 기본키(postIdx), 취소한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deletePostHeart(int postIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("postIdx", postIdx);
		map.put("userIdx", userIdx);
		sqlSession.selectList("SnsMapper.deletePostHeart", map);
	}

	// 게시물 기본키로 게시물 정보
	// 파라미터 : 게시물 기본키(postIdx), 로그인한 유저 기본키(userIdx)
	// 리턴값 : 게시물 정보(PostInfoDto)
	@Override
	public PostInfoDto getPostInfoDtoByPostIdx(int postIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("postIdx", postIdx);
		map.put("userIdx", userIdx);
		return sqlSession.selectOne("SnsMapper.getPostInfoDtoByPostIdx", map);
	}

	// 게시물에 달린 댓글
	// 파라미터 : 게시물 기본키(postIdx), 로그인한 유저 기본키(userIdx)
	// 리턴값 : [댓글 상세 정보](ReplyDetailDto)
	@Override
	public List<ReplyDetailDto> getReplyList(int postIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("postIdx", postIdx);
		map.put("userIdx", userIdx);
		return sqlSession.selectList("SnsMapper.getReplyList", map);
	}
	
	// 게시물에 댓글 추가하기
	// 파라미터 : 게시물 기본키(postIdx), 댓글 단 유저 기본키(userIdx), 댓글내용(content)
	// 리턴값 : 댓글 기본키
	@Override
	public int addReply(int postIdx, int userIdx, String content) {
		ReplyDetailDto dto = new ReplyDetailDto();
		dto.setPostIdx(postIdx);
		dto.setUserIdx(userIdx);
		dto.setContent(content);
		sqlSession.insert("SnsMapper.addReply", dto);
		return dto.getReplyIdx();
	}

	// 게시물 댓글에 하트 누르기
	// 파라미터 : 게시물 기본키(postIdx), 누른 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void heartToReply(int replyIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("replyIdx", replyIdx);
		map.put("userIdx", userIdx);
		sqlSession.insert("SnsMapper.heartToReply", map);
	}

	// 게시물 댓글에 하트 취소
	// 파라미터 : 게시물 기본키(postIdx), 취소한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deleteReplyHeart(int replyIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("replyIdx", replyIdx);
		map.put("userIdx", userIdx);
		sqlSession.delete("SnsMapper.deleteReplyHeart", map);
	}

	// 게시물 기본키로 게시물 얻기
	// 파라미터 : 게시물 기본키(postIdx)
	// 리턴값 : 게시물정보(PostDto)
	@Override
	public PostDto getPostDtoByPostIdx(int postIdx) {
		return sqlSession.selectOne("SnsMapper.getPostDtoByPostIdx", postIdx);
	}

	// 댓글 삭제하기
	// 파라미터 : 댓글 기본키(replyIdx), 작성한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deleteReply(int replyIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("replyIdx", replyIdx);
		map.put("userIdx", userIdx);
		sqlSession.selectOne("SnsMapper.deleteReply", map);
	}

	// 게시물 추가하기
	// 파라미터 : 게시한 유저 기본키(userIdx), 공개범위(openRange), 내용(content), 사진(img)
	// 리턴값 : -
	@Override
	public void addPost(int userIdx, String openRange, String content, String img) {
		PostDto dto = new PostDto();
		dto.setUserIdx(userIdx);
		dto.setOpenRange(openRange);
		dto.setContent(content);
		dto.setImg(img);
		sqlSession.insert("SnsMapper.addPost", dto);
	}
	
	// 특정 유저가 올린 게시물
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : [게시물 정보](PostInfoDto)
	@Override
	public List<PostInfoDto> getUsersPostList(int loginUserIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("loginUserIdx", loginUserIdx);
		map.put("userIdx", userIdx);
		return sqlSession.selectList("SnsMapper.getMyPostList", map);
	}
	
	// 팔로우 하기
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 내가 팔로우 할 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void addFollower(int loginUserIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("loginUserIdx", loginUserIdx);
		map.put("userIdx", userIdx);
		sqlSession.selectList("SnsMapper.addFollower", map);
	}
	
	// 팔로우하고 있는지 아닌지 확인
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 확인할 유저 기본키(userIdx)
	// 리턴값 : 팔로우중이면 1, 아니면 0
	@Override
	public int checkFollowing(int loginUserIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("loginUserIdx", loginUserIdx);
		map.put("userIdx", userIdx);
		return sqlSession.selectOne("SnsMapper.checkFollowing", map);
	}
	
	// 게시물 삭제
	// 파라미터 : 게시물 기본키(postIdx)
	// 리턴값 : -
	@Override
	public void deletePost(int postIdx) {
		sqlSession.delete("SnsMapper.deletePost", postIdx);
	}

//	작업자 : 이경미 ---------------------------------------------------------------
	// 팔로잉 팔로워 리스트 보기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [유저 정보](UsersDto)
	@Override
	   public List<UsersDto> getFollowerList(int followedUserIdx) {
	      return sqlSession.selectList("getFollowerList", followedUserIdx);
	   }

	// 언팔로우
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void unfollow(int loginUserIdx, int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("loginUserIdx", loginUserIdx);
		map.put("userIdx", userIdx);
		sqlSession.delete("SnsMapper.unfollow", map);
	}

	
	








}
