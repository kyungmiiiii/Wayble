package com.wayble.util;

public class GenerateInviteCode {
	
	// 인증코드 만들기
	// 파라미터 : -
	// 리턴값 : 인증코드(code)
	public static String main() {
		String code = "";
		while(code.length()<4) {
			char ch = 0;
			ch = (char)(Math.random()*'z');
			if(ifEngNum(ch))
				code += ch;
		}
		return code;
	}
	
	// 난수 발생한 아스키코드가 영어인지 아닌지 확인
	// 파라미터 : 글자(ch)
	// 리턴값 : 확인결과(boolean)
	public static boolean ifEngNum(char ch) {
		if(ch>='A' && ch<='Z') return true;
		if(ch>='a' && ch<='z') return true;
		if(ch>='0' && ch<='9') return true;
		return false;
	}
	
	
}
