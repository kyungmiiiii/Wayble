package com.wayble.dto;

public class ReplyDetailDto {
	private int replyIdx;			// 댓글 기본키
	private int postIdx;			// 게시물 기본키
	private int userIdx;			// 유저 기본키
	private String content;			// 댓글 내용
	private String profile;			// 프로필
	private String nickname;		// 닉네임
	private boolean checkHeart;		// 하트 누름 여부
	private int countHeart;			// 하트 수
	
	public ReplyDetailDto() {}
	public ReplyDetailDto(int replyIdx, int postIdx, int userIdx, String content, String profile, String nickname, boolean checkHeart, int countHeart) {
		this.replyIdx = replyIdx;
		this.postIdx = postIdx;
		this.userIdx = userIdx;
		this.content = content;
		this.profile = profile;
		this.nickname = nickname;
		this.checkHeart = checkHeart;
		this.countHeart = countHeart;
	}

	public int getReplyIdx() {
		return replyIdx;
	}

	public void setReplyIdx(int replyIdx) {
		this.replyIdx = replyIdx;
	}

	public int getPostIdx() {
		return postIdx;
	}

	public void setPostIdx(int postIdx) {
		this.postIdx = postIdx;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean isCheckHeart() {
		return checkHeart;
	}

	public void setCheckHeart(boolean checkHeart) {
		this.checkHeart = checkHeart;
	}

	public int getCountHeart() {
		return countHeart;
	}

	public void setCountHeart(int countHeart) {
		this.countHeart = countHeart;
	}
	
	
	
	

}
