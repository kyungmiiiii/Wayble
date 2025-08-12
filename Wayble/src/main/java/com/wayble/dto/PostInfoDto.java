package com.wayble.dto;

public class PostInfoDto {
	private String profile;			// 프로필
	private String nickname;		// 닉네임
	private String content;			// 게시물 내용
	private boolean checkHeart;		// 하트 누름 여부
	private int countHeart;			// 하트 수
	private int countReply;			// 댓글 수
	private int postIdx;			// 게시물 기본키
	private String img;				// 게시물 사진
	
	public PostInfoDto() {}

	public PostInfoDto(String profile, String nickname, String content, boolean checkHeart, int countHeart, int countReply, int postIdx, String img) {
		this.profile = profile;
		this.nickname = nickname;
		this.content = content;
		this.checkHeart = checkHeart;
		this.countHeart = countHeart;
		this.countReply = countReply;
		this.postIdx = postIdx;
		this.img = img;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public int getCountReply() {
		return countReply;
	}

	public void setCountReply(int countReply) {
		this.countReply = countReply;
	}

	public int getPostIdx() {
		return postIdx;
	}

	public void setPostIdx(int postIdx) {
		this.postIdx = postIdx;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	

	
	


}
