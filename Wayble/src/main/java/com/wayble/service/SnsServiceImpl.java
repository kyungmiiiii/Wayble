package com.wayble.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wayble.dao.CommonDao;
import com.wayble.dao.SnsDao;
import com.wayble.dto.PostDto;
import com.wayble.dto.PostInfoDto;
import com.wayble.dto.UsersDto;

@Service 
public class SnsServiceImpl implements SnsService {
	@Autowired
	SnsDao sDao;
	@Autowired
	CommonDao coDao;

//	작업자 : 서수련 ---------------------------------------------------------------
	// 팔로워수, 팔로잉수, 게시글수 가져오기 
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : 팔로워수, 팔로잉수, 게시글수
	@Override
	public HashMap<String, Integer> getUserStats(int userIdx) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("follower", sDao.countFollower(userIdx));
		map.put("post", sDao.countPost(userIdx));
		map.put("following", sDao.countFollowing(userIdx));
		return map;
	}

	// 전체공개와 팔로잉 게시물 모아서 정렬
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [게시물 정보](PostInfoDto)
	@Override
	public List<PostInfoDto> getTotalPostList(int userIdx) {
		List<PostInfoDto> list = new ArrayList<>();
		List<PostInfoDto> tmp = new ArrayList<>();
		List<PostInfoDto> open = sDao.getOpenPostList(userIdx);
		List<PostInfoDto> following = sDao.getFollowingPostList(userIdx);
		int max = open.get(0).getPostIdx();
		int min = open.get(0).getPostIdx();
		
		for(PostInfoDto p : open) {
			int postIdx = p.getPostIdx();
			max = max < postIdx ? postIdx : max;
			min = min > postIdx ? postIdx : min;
			tmp.add(p);
		}
		for(PostInfoDto p : following) {
			int postIdx = p.getPostIdx();
			max = max < postIdx ? postIdx : max;
			min = min > postIdx ? postIdx : min;
			tmp.add(p);
		}
		for(int i=max; i>=min; i--) {
			for(int j=0; j<tmp.size(); j++) {
				PostInfoDto p = tmp.get(j);
				if(p.getPostIdx() == i) {
					list.add(p);
					tmp.remove(j);
				}
			}
		}
		return list;
	}

	// 게시물에 하트 누르기
	// 파라미터 : 게시물 기본키(postIdx), 누른 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void heartToPost(int postIdx, int userIdx) {
		sDao.heartToPost(postIdx, userIdx);
	}

	// 게시물에 하트 취소
	// 파라미터 : 게시물 기본키(postIdx), 취소한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deletePostHeart(int postIdx, int userIdx) {
		sDao.deletePostHeart(postIdx, userIdx);
	}

	// 게시물 상세와 댓글 가져오기 
	// 파라미터 : 게시물 기본키(postIdx), 로그인한 유저 기본키(userIdx)
	// 리턴값 : 게시물 정보(PostInfoDto)
	@Override
	public HashMap<String, Object> loadPostDetail(int postIdx, int userIdx) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("post", sDao.getPostInfoDtoByPostIdx(postIdx, userIdx));
		map.put("reply", sDao.getReplyList(postIdx, userIdx));
		return map;
	}

	// 게시물에 댓글 추가하기
	// 파라미터 : 게시물 기본키(postIdx), 댓글 단 유저 기본키(userIdx), 댓글내용(content)
	// 리턴값 : 댓글 기본키
	@Override
	public HashMap<String, Object> addReply(int postIdx, int userIdx, String content) {
		HashMap<String, Object> map = new HashMap<>();
		String sendUserNickname = coDao.getUserInfo(userIdx).getNickname();
		map.put("replyIdx", sDao.addReply(postIdx, userIdx, content));
		map.put("profile", coDao.getUserInfo(userIdx).getProfile());
		map.put("nickname", sendUserNickname);
		return map;
	}
	
	// 게시물 댓글에 하트 누르기
	// 파라미터 : 게시물 기본키(postIdx), 누른 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void heartToReply(int replyIdx, int userIdx) {
		sDao.heartToReply(replyIdx, userIdx);
	}

	// 게시물 댓글에 하트 취소
	// 파라미터 : 게시물 기본키(postIdx), 취소한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deleteReplyHeart(int replyIdx, int userIdx) {
		sDao.deleteReplyHeart(replyIdx, userIdx);
	}

	// 댓글 삭제하기
	// 파라미터 : 댓글 기본키(replyIdx), 작성한 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void deleteReply(int replyIdx, int userIdx) {
		sDao.deleteReply(replyIdx, userIdx);
	}

	// 게시물 추가하기
	// 파라미터 : 게시한 유저 기본키(userIdx), 공개범위(openRange), 내용(content), 사진(img)
	// 리턴값 : -
	@Override
	public void addPost(int userIdx, String openRange, String content, String img) {
		sDao.addPost(userIdx, openRange, content, img);
	}
	
	// 게시물 기본키로 게시물 얻기
	// 파라미터 : 게시물 기본키(postIdx)
	// 리턴값 : 게시물정보(PostDto)
	@Override
	public PostDto getPostDtoByPostIdx(int postIdx) {
		return sDao.getPostDtoByPostIdx(postIdx);
	}
	
	// 특정 유저가 올린 게시물
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : [게시물 정보](PostInfoDto)
	@Override
	public List<PostInfoDto> getUsersPostList(int loginUserIdx, int userIdx) {
		return sDao.getUsersPostList(loginUserIdx, userIdx);
	}
	
	// 팔로우 하기
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 내가 팔로우 할 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void addFollower(int loginUserIdx, int userIdx) {
		sDao.addFollower(loginUserIdx, userIdx);
	}
	
	// 팔로우하고 있는지 아닌지 확인
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 확인할 유저 기본키(userIdx)
	// 리턴값 : 팔로우중이면 1, 아니면 0
	@Override
	public boolean checkFollowing(int loginUserIdx, int userIdx) {
		if(sDao.checkFollowing(loginUserIdx, userIdx) == 0) return false;
		else return true;
	}
	
	// 게시물 삭제
	// 파라미터 : 게시물 기본키(postIdx)
	// 리턴값 : -
	@Override
	public void deletePost(int postIdx) {
		sDao.deletePost(postIdx);
	}

//	작업자 : 이경미 ---------------------------------------------------------------
	
	// 팔로잉 팔로워 리스트 보기
	// 파라미터 : 로그인한 유저 기본키(userIdx)
	// 리턴값 : [유저 정보](UsersDto)
	@Override
    public List<UsersDto> getFollowerList(int followedUserIdx) {
       return sDao.getFollowerList(followedUserIdx);
    }
	
	// 언팔로우
	// 파라미터 : 로그인한 유저 기본키(loginUserIdx), 상대 유저 기본키(userIdx)
	// 리턴값 : -
	@Override
	public void unfollow(int loginUserIdx, int userIdx) {
		sDao.unfollow(loginUserIdx, userIdx);
	}










	

}
