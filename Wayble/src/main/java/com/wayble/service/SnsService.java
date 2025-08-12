package com.wayble.service;

import java.util.HashMap;
import java.util.List;

import com.wayble.dto.PostDto;
import com.wayble.dto.PostInfoDto;
import com.wayble.dto.UsersDto;

public interface SnsService {
//  작업자 : 서수련 ----------------------------------------------------------------------------------	
	HashMap<String, Integer> getUserStats(int userIdx); // 팔로워수, 팔로잉수, 게시글수 가져오기
	List<PostInfoDto> getTotalPostList(int userIdx); // 전체공개와 팔로잉 게시물 모아서 정렬
	void heartToPost(int postIdx, int userIdx); // 게시물에 하트 누르기
	void deletePostHeart(int postIdx, int userIdx); // 게시물에 하트 취소
	HashMap<String, Object> loadPostDetail(int postIdx, int userIdx); // 게시물 상세와 댓글 가져오기 
	HashMap<String, Object> addReply(int postIdx, int userIdx, String content); // 게시물에 댓글 추가하기
	void heartToReply(int replyIdx, int userIdx); // 게시물 댓글에 하트 누르기
	void deleteReplyHeart(int replyIdx, int userIdx); // 게시물 댓글에 하트 취소
	void deleteReply(int replyIdx, int userIdx); // 댓글 삭제하기
	void addPost(int userIdx, String openRange, String content, String image); // 게시물 추가하기
	PostDto getPostDtoByPostIdx(int postIdx); // 게시물 기본키로 게시물 얻기
	List<PostInfoDto> getUsersPostList(int loginUserIdx, int userIdx); // 특정 유저가 올린 게시물
	void addFollower(int loginUserIdx, int userIdx); // 팔로우 하기
	boolean checkFollowing(int loginUserIdx, int userIdx); // 팔로우하고 있는지 아닌지 확인
	void deletePost(int postIdx);	//게시물 삭제
	
//	작업자 : 이경미 -----------------------------------------------------------------------------------
	List<UsersDto> getFollowerList(int followedUserIdx); // 팔로잉 팔로워 리스트 보기
	void unfollow(int loginUserIdx, int userIdx); // 언팔로우
}