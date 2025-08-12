<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/FindPw.css" />
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<style>
	
	</style>
</head>
<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		let result;
		$(function(){
			$("input[id='email']").on('input',function(){
				let email = $("input[id='email']").val().trim();
				if(email.length>0){
					$('#send-email-code').css({background: 'black',color:'white'});
				}else {
					$('#send-email-code').css({background: '#f8f8f8',color:'black'});
				}
			});
			
			$('#send-email-code').click(function(){
				const json_data = {
						email : $("#email").val()
					};
					const init01 = {
						method: "POST",
						headers: {
							/* "Content-Type": "application-json" */
						},
						body: JSON.stringify(json_data)
					};
					
					fetch('ResetPwcode', init01)
					.then(function(response) {
						return response.json()
					})
					.then(function(data) {
						console.log(data);
						if(data.result == true) {
							alert("메일 보냈다");
							$("#send-email-code").css({"color": "grey", "backgroundColor" : "#f8f8f8"});
							$("#send-email-code").prop("disabled", true);
							$('.code-check').show();
						}
					})
					.catch(function(error) {
						alert("에러! : " + error);
					});
				});
			
			 $('input[name=authCode]').click(function(){
				
				const json_data = {
						code: $("#write-email-code").val(),
						email : $('#email').val()
					};
					const init01 = {
						method: "POST",
						headers: {
							/* "Content-Type": "application-json" */
						},
						body: JSON.stringify(json_data)
					};
					
					fetch('ResetPwcodecheck', init01)
					.then(function(response) {
						return response.json()
					})
					.then(function(data) {
						console.log(data);
						if(data.result == true) {
							alert("인증되었습니다.\n이메일로 임시비밀번호가 발송되었습니다.");
							//바로 발송하게
							location.href = 'ResetComplete';
						} else {
							alert("인증번호를 잘못 입력하셨습니다. 다시 입력해 주세요.");
						}
					})
					.catch(function(error) {
						alert("에러! : " + error);
					});
			});
			
			
			$('#write-email-code').on('input',function(){
				let code = $("input[id='write-email-code']").val().trim();
				if(code.length>0) {
					$("input[name='authCode']").css({background: 'black',color:'white'});
				} else {
					$("input[name='authCode']").css({background: '#f8f8f8',color:'black'});
				}
			});
		});	
	</script>
<body>
	<div id="header" class="center">
		<div>Wayble</div>
		<div><b>비밀번호 초기화</b></div>
	</div>
	<div id="content1" class="center">
		<p>비밀번호를 찾고자하는 계정의 </br>이메일을 입력해 주세요</p></br>
		<input type="text" id='email' class="register" placeholder="이메일" name='email'>
		<div><button type='button' id="send-email-code" class="center email-check2">이메일 인증번호발송</button></div>
		<p>※인증번호가 안온다면?<br/>스팸으로 분류되어있거나, 일시적인 오류 일 수 있습니다. <br/>확인 후 재시도해주세요</p>
		<input type="text" id="write-email-code" class="code-check" name='code' placeholder="인증번호 입력">
		<%-- <input type="hidden" id='result' vaule='${result}'/> --%>
		<input type="button" style="margin-top:90px;" class="code-check" value="다음" name="authCode"/>
	</div>
</body>
</html>



