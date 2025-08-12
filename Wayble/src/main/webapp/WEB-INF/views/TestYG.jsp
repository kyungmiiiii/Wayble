<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel='stylesheet' href='css/Login.css'/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>	
	<script>
		function login() {
			let email = 'email....';
			let pw = 'pw......';
			const json_data = {
					email : email,
					pw : pw
			};
			const init01 = {
					method: "POST",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify(json_data)
			};
			
			fetch("yg", init01)
			.then(function(response) {
				return response.json();
			})
			.then(function(data) {
				alert("!");
				console.log(data);
			})
			.catch(function(error) {
				alert("에러! : " + error);
			})
		}
	</script>
</head>
<body>
	<div id="header">
		<div>Wayble</div>
		<div><b>로그인</b></div>
	</div>
	<div id="main" class="border">
		<div>또는 직접 입력</div>
		<div>
			<div class="register">
				<input type="text" placeholder="이메일" id='email'/>
			</div>
			<div class="register">
				<input type="password" placeholder="비밀번호" id='pw'/>
			</div>
			<div class="error">* 아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.</div> 
			<input type='button' onclick="login()" id="final" class="login" value='로그인'>
		</div>	
	</div>
</body>
</html>