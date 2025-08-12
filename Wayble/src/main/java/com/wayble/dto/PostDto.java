package com.wayble.dto;

public class PostDto {
	private int postIdx;			// 게시물 기본키
	private int userIdx;			// 게시물 올린 유저 기본키
	private String openRange;		// 공개범위
	private String content;			// 게시물 내용
	private String img;				// 게시물 사진
	
	public PostDto() {}

	public PostDto(int postIdx, int userIdx, String openRange, String content, String img) {
		super();
		this.postIdx = postIdx;
		this.userIdx = userIdx;
		this.openRange = openRange;
		this.content = content;
		this.img = img;
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

	public String getOpenRange() {
		return openRange;
	}

	public void setOpenRange(String openRange) {
		this.openRange = openRange;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	
	
	
}
