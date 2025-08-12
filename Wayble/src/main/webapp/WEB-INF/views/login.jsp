<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>로그인</title>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/login.css'/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>

	</script>
</head>
<body>

	<div id="header">
		<div>Wayble</div>
		<div><b>로그인</b></div>
	</div>
	<div id="main" class="border">
		<button id="kakao" class="login">
			<div class="flex">
				<div><img src="https://fe-web-assets.wow.wrtn.ai/icons/social-icons/kakao_fill.svg" class="cover pic"/></div>
				<div>카카오 계정으로 로그인</div>
			</div>
		</button>
		<button id="naver" class="login">
			<div class="flex">
				<div><img src="	https://fe-web-assets.wow.wrtn.ai/icons/social-icons/naver_fill.svg" class="cover pic"/></div>
				<div>네이버 계정으로 로그인</div>
			</div>
		</button>
		<div>또는 직접 입력</div>
		<form action='loginAction' method='post'>
			<div class="register">
				<input type="text" placeholder="이메일" name='email' 
				VALUE=""/> <!-- TODO : 나중에 수정 -->
			</div>
			<div class="register">
				<input type="password" placeholder="비밀번호" name='pw' 
				VALUE=""/>  <!-- TODO : 나중에 수정 -->
			</div>
			<input type='submit' id="final" class="login" value='로그인'>
		</form>
		<div class="flex">
			<button onclick="location.href='signUpForm'">회원가입</button>
			<div></div> 
			<button onclick="location.href='passwordResetForm'">비밀번호 초기화</button>
		</div>
	</div>
</body>
</html>