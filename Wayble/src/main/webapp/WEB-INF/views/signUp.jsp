<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sign-up.css" />
	<style>
	
	</style>
</head>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		function onSubmitCheck() {
			let p1 = $("#password").val();
			let p2 = $("#password-check").val();
			if(p1.length==0 || p2.length==0) {
				alert("비밀번호를 입력하세요.");
				return false;
			}
			if(p1!=p2) {
				alert("비밀번호 확인을 정확하게 입력해 주세요.");
				return false;
			}
			return true;
		}
	
		$(function(){
			$("#email-check").click(function() {
				// ajax.
				const json_data = {
					email: $("#email").val()
				};
				const init01 = {
					method: "POST",
					headers: {
						/* "Content-Type": "application-json" */
					},
					body: JSON.stringify(json_data)
				};
				
				fetch('RegisterCode', init01)
				.then(function(response) {
					return response.json()
				})
				.then(function(data) {
					console.log(data);
					if(data.result == true) {
						// .then() ---> 1) alert(메일 보냈다) 2) "인증코드 입력" 숨겼던 거 보이게 (나중에) 3) "1,2번째 자식 숨김".
						alert("메일 보냈다");
						$("#content1 > form > div:first-child > input:nth-child(1)").hide();
						$("#content1 > form > div:first-child > div:nth-child(2)").hide();
						$("#content1 > form > div:first-child > input:nth-child(3)").show();
						$("#content1 > form > div:first-child > div:nth-child(4)").show();
					} else {
						alert('이미 가입된 이메일입니다.')
					}
				})
				.catch(function(error) {
					alert("에러! : " + error);
				});
			});
			$("#code-check").click(function() {
				// ajax.
				const json_data = {
					code: $("#code").val()
				};
				const init01 = {
					method: "POST",
					headers: {
						/* "Content-Type": "application-json" */
					},
					body: JSON.stringify(json_data)
				};
				
				fetch('RegisterCodecheck', init01)
				.then(function(response) {
					return response.json()
				})
				.then(function(data) {
					console.log(data);
					if(data.result == true) {
						alert("인증되었습니다");
/* 						$("#content1 > form > div:first-child > input:nth-child(1)").hide();
						$("#content1 > form > div:first-child > div:nth-child(2)").hide();
						$("#content1 > form > div:first-child > input:nth-child(3)").show();
						$("#content1 > form > div:first-child > div:nth-child(4)").show(); */
						$("#content1 > form > div:first-child > input:nth-child(3)").attr('disabled', 'true');
						$("#content1 > form > div:first-child > div:nth-child(4) > input").attr('disabled', 'true');
						$("#content1 > form > div:first-child > div:nth-child(4) > input").css('color', 'grey');
						$("#content1 > form > div:first-child > div:nth-child(4) > input").css('backgroundColor', '#f8f8f8');
						$("#content1 > form > div:first-child > div:nth-child(4) > input").text('인증번호 확인완료');
						$("#content1 > form .afterInput").css('visibility', 'initial');
					} else {
						alert("인증번호를 잘못 입력하셨습니다. 다시 입력해 주세요.");
					}
				})
				.catch(function(error) {
					alert("에러! : " + error);
				});
			});
			$("#nick-name").keyup(function(e) {
				// 회원가입 버튼을 비활성화! (중복화인 버튼 클릭을 유도)
				$("#registerComplete").prop("disabled", true);
			});
			$("#nick-name-check").click(function() {
				// ajax.
				const json_data = {
					nickname : $("#nick-name").val(),
				};
				const init01 = {
					method: "POST",
					headers: {
						/* "Content-Type": "application-json" */
					},
					body: JSON.stringify(json_data)
				};
				
				fetch('nicknameCheck', init01)
				.then(function(response) {
					return response.json()
				})
				.then(function(data) {
					console.log(data);
					if(data.result == true) {
						alert("사용할 수 있는 닉네임입니다");
						$("#registerComplete").prop("disabled", false);
					} else {
						alert("사용할 수 없는 닉네임입니다");
						$("#registerComplete").prop("disabled", true);
					}
				})
				.catch(function(error) {
					alert("에러! : " + error);
				});
				
			});
			/* $("#registerComplete").click(function() {
				alert('회원가입 눌림!');
			}); */
		});
	</script>
<body>
	<div id="header" class="center">
		<div>Wayble</div>
		<div><b>회원가입</b></div>
	</div>
	<div id="content1" class="center">
		<form action="signUpAction" method="post">
			<div>
				<input type="text" class="register" id="email" placeholder="이메일" name="email" />
				<div class='checkButton'><br/><input type="button" id="email-check" value="이메일 인증번호발송" name="email-check"><br/></div>
				
				<input type="text" class="register" id="code" placeholder="인증번호 입력" STYLE="DISPLAY:NONE;"/>
				<div STYLE="DISPLAY:NONE;" class='checkButton'><br/><input type="button" id="code-check" value="인증번호 확인" name="code-check"><br/></div>
				
				<input STYLE="VISIBILITY:HIDDEN;" type="text" class="register afterInput" id="nick-name" placeholder="닉네임" name="nickname"><br/>
				<div STYLE="VISIBILITY:HIDDEN;" class='checkButton afterInput'><input type="button" id="nick-name-check" value="중복확인" name="nick-name-check"><br/></div>
				<input STYLE="VISIBILITY:HIDDEN;" type="password" class="register afterInput" id="password" placeholder="비밀번호" name="pw"><br/>
				<input STYLE="VISIBILITY:HIDDEN;" type="password" class="register afterInput" id="password-check" placeholder="비밀번호 확인" name="pw2"><br/>
				<p STYLE="VISIBILITY:HIDDEN;" class="afterInput">8자 이상, 문자 · 숫자 · 특수문자 중 2가지 포함</p>
			</div>
			<div><input type='submit' disabled='true' value='회원가입' id='registerComplete' onclick="return onSubmitCheck();"></button></div>
		</form>
	</div>
	
</body>
</html>