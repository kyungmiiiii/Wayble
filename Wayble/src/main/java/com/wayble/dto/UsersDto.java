package com.wayble.dto;

public class UsersDto {
	private int userIdx;		// 유저 기본키
	private String email;		// 이메일
	private String nickname;	// 닉네임
	private String password;	// 비밀번호
	private String profile;		// 프로필
	
	public UsersDto() {}
	public UsersDto(int userIdx, String email, String nickname, String password, String profile) {
		this.userIdx = userIdx;
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.profile = profile;
	}

	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	

}
