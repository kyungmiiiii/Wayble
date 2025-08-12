package com.wayble.dao;

import java.util.List;
import java.util.Map;

import com.wayble.dto.PostDto;
import com.wayble.dto.PostInfoDto;
import com.wayble.dto.ReplyDetailDto;
import com.wayble.dto.UsersDto;

public interface SnsDao {
//	작업자 : 서수련
	int countFollower(int followedUserIdx); // 팔로워 세기
	int countFollowing(int followingUserIdx); // 팔로잉 세기
	int countPost(int userIdx); // 게시물 세기 
	List<PostInfoDto> getOpenPostList(int userIdx); // 전체보기로 게시된 게시물
	List<PostInfoDto> getFollowingPostList(int userIdx); // 내가 팔로우한 사람들의 게시물
	void heartToPost(int postIdx, int userIdx); // 게시물에 하트 누르기
	void deletePostHeart(int postIdx, int userIdx); // 게시물에 하트 취소
	PostInfoDto getPostInfoDtoByPostIdx(int postIdx, int userIdx); // 게시물 기본키로 게시물 정보
	List<ReplyDetailDto> getReplyList(int postIdx, int userIdx); // 게시물에 달린 댓글
	int addReply(int postIdx, int userIdx, String content); // 게시물에 댓글 추가하기
	void heartToReply(int replyIdx, int userIdx); // 게시물 댓글에 하트 누르기
	void deleteReplyHeart(int replyIdx, int userIdx); // 게시물 댓글에 하트 취소
	PostDto getPostDtoByPostIdx(int postIdx); // 게시물 기본키로 게시물 얻기
	void deleteReply(int replyIdx, int userIdx); // 댓글 삭제하기
	void addPost(int userIdx, String openRange, String content, String img); // 게시물 추가하기
	List<PostInfoDto> getUsersPostList(int loginUserIdx, int userIdx); // 특정 유저가 올린 게시물
	void addFollower(int loginUserIdx, int userIdx); // 팔로우 하기
	int checkFollowing(int loginUserIdx, int userIdx); // 팔로우하고 있는지 아닌지 확인
	void deletePost(int postIdx); // 게시물 삭제
	
//	작업자 : 이경미 ---------------------------------------------------------------------------
	List<UsersDto> getFollowerList(int followedUserIdx); // 팔로잉 팔로워 리스트 보기
	void unfollow(int loginUserIdx, int userIdx); // 언팔로우
}
