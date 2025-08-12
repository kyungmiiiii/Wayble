<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>마이페이지</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/resign.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/change-pw.css" />
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.1/index.global.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@fullcalendar/core@6.1.1/locales/ko.global.js"></script>
	<script>
		const contextPath = '${pageContext.request.contextPath}';
		function send_ajax_new_month(year, month) {
			const json_data = {
				year: year,
				month: month
			};
			const init01 = {
				method: "POST",
				/* headers: {
					"Content-Type": "application-json"
				}, */
				body: JSON.stringify(json_data)
			};
			
			fetch('getPlannerByMonth', init01)
			.then(function(response) {
				if(response.ok) {
					return response.json();
				}
				return response;
			})
			.then(function(arr) {
				console.log(arr);
				// 새롭게 받은 일정으로, 캘린더에 표시된 일정 지운 후 다시 채워넣기!
				full_calendar.removeAllEvents();
				for(let i=0; i<=arr.length-1; i++) {
					obj = getNextDay(arr[i].year2, arr[i].month2, arr[i].day2);
					arr[i].year2 = obj.year;
					arr[i].month2 = obj.month <= 9 ? "0" + obj.month : obj.month;
					arr[i].day2 = obj.day <= 9 ? "0" + obj.day : obj.day;
					
					//console.log(arr[i]);
					//arr[i].title
					//arr[i].year1 arr[i].month1 arr[i].day1
					full_calendar.addEvent({
						title: arr[i].title,
						start: `\${arr[i].year1}-\${arr[i].month1}-\${arr[i].day1}`,
						end: `\${arr[i].year2}-\${arr[i].month2}-\${arr[i].day2}`,
						allDay: true
					});
				}
				full_calendar.render();
				
			})
			.catch(function(error) {
				alert("에러! : " + error);
			});
		}

		function getNextDay(year, month, day) {
		  const currentDate = new Date(year, month - 1, day);

		  currentDate.setDate(currentDate.getDate() + 1);

		  const nextYear = currentDate.getFullYear();
		  const nextMonth = currentDate.getMonth() + 1;
		  const nextDay = currentDate.getDate();

		  return {
		    year: nextYear,
		    month: nextMonth,
		    day: nextDay,
		  };
		}
	
		document.addEventListener('DOMContentLoaded', function() {
			const nicknameDiv = document.querySelector('.nickname');
			const button = document.querySelector('.update-nickname');
			
			let isEditing = false;
			
			nicknameDiv.addEventListener('click', () => {
				if(!isEditing) {
					const currentNickname = nicknameDiv.textContent;
					const input = document.createElement('input');
					input.type = 'text';
					input.value = currentNickname;
					
					nicknameDiv.textContent = '';
					nicknameDiv.appendChild(input);
					
					input.focus();
					isEditing = true;
				}
			});
			button.addEventListener('click', (e) => {
				if(isEditing) {
					const input = nicknameDiv.querySelector('input');
					const newNickname = input.value;
					nicknameDiv.textContent = newNickname;
					isEditing = false;
				}
			});
			
			// Full Calendar 초기화
			let calendar_element = document.getElementById("calendar");
			full_calendar = new FullCalendar.Calendar(calendar_element, {
				initialView: "dayGridMonth",
				locale: "ko",
				headerToolbar: {
					start: 'prev',
					center: 'title',
					end: 'next'
				},
				events: [
/* 					{
						title: 'my event',
						start: '2025-07-22',
						end: '2025-07-25'
					} */
				],
				dayCellContent: function (arg) {
			    	//const { date } = arg;
			    	return arg.dayNumberText.replace("일","");
			    },

				/*
				,eventClick: function(info) {
					alert('Event: ' + info.event.title);
				}*/
			});
			let y = new Date().getFullYear();
			let m = new Date().getMonth()+1;
			send_ajax_new_month(y, m);
			full_calendar.render();
		});
		
		$(function() {
			// FullCalendar
			$(".fc-icon-chevron-left").click(function() {
				let year = full_calendar.getDate().getFullYear();
				let month = full_calendar.getDate().getMonth()+1;	// 이동 직전 월.
				month = month - 1;
				if(month==0) {
					year--;
					month += 12;
				}
				send_ajax_new_month(year, month);
			});
			$(".fc-icon-chevron-right").click(function() {
				let year = full_calendar.getDate().getFullYear();
				let month = full_calendar.getDate().getMonth()+1;	// 이동 직전 월.
				month = month + 1;
				if(month==13) {
					year++;
					month -= 12;
				}
				send_ajax_new_month(year, month);
			});

			$("#plus").change(function() {
				$("#form-img-upload").submit();
			});
			
			$('.option').click(function() {
				$('#optionList').fadeToggle();
			});
			$('#change-pw-btn').click(function(){
				$('.change-pw').css('display', 'block');
			});
			$('#Resign-btn').click(function(){
				$('#bg-screen').css('display','block');
				$('#Resign-main').css('display','block');
			})
			
			$('.btn-cancel').click(function() {
				$('.change-pw').css('display', 'none');
				$('#Resign-main').css('display','none');
				$('#bg-screen').css('display','none');
				$('.input-bar > input').val('');
			});
			$('.btn-update').click(function() {
				$('.change-pw').css('display', 'none');
				$('#Resign-main').css('display','none');
				$('#bg-screen').css('display','none');
				let currentPw = $('.change-pw > .input-bar:nth-child(2) > input').val();
				let newPw1 = $('.change-pw > .input-bar:nth-child(3) > input').val();
				let newPw2 = $('.change-pw > .input-bar:nth-child(4) > input').val();
				//alert(current_pw + " / " + new_pw1 + " / " + new_pw2);
				if(newPw1 != newPw2) {
					alert("새 비밀번호 확인을 잘못 입력하셨습니다!");
					return;
				}
				
				// ajax.
				const json_data = {
					currentPw: current_pw,
					new_pw: new_pw1,
					user_idx: "${loginUserIdx}"
				};
				const init = {
					method: "POST",
					headers: {
						/* "Content-Type": "application/json" */
					},
					body: JSON.stringify(json_data),
				};
				
				fetch("updatePw", init)
				.then(response => response.json())
				.then(data => {
					console.log(data);
					if(data.result==true)
						alert("변경 성공!");
					else
						alert("현재 비밀번호를 잘못 입력하셨습니다.");
				})
				.catch(error => {
					alert("에러! : " + error);
				});
				
				// .then(), result:true인지 result:false인지?
				//		    ---> 현재 비밀번호를 제대로 입력했는지 확인 가능.
				
			});
			
			$('#bg-none').click(function() {
				$('.change-pw').css('display', 'none');
			});
			
			$('.update-nickname').click(function(){
				let new_nickname = $('input.nickname').val();
				const json_data = {
					nickname: new_nickname,
					user_idx: "${loginUserIdx}"
				};
				const init01 = {
					method: "POST",
					headers: {
						/* "Content-Type": "application-json" */
					},
					body: JSON.stringify(json_data)
				};
				
				fetch('updateNickname', init01)
				.then(function(response) {
					return response.json()
				})
				.then(function(data) {
					console.log(data);
					if(data.result == true)
						alert("변경 성공.");
					else
						alert("이미 존재하는 닉네임입니다.");
				})
				.catch(function(error) {
					alert("에러! : " + error);
				});
			});
			
			$('.plus-button').click(function(){	
				let new_nickname = $('input.nickname').val();
				const json_data = {
					nickname: new_nickname,
					user_idx: "${loginUserIdx}"
				};
				const init01 = {
					method: "POST",
					headers: {
						/* "Content-Type": "application-json" */
					},
					body: JSON.stringify(json_data)
				};
				
				fetch('updateNickname', init01)
				.then(function(response) {
					return response.json()
				})
				.then(function(data) {
					console.log(data);
					if(data.result == true)
						alert("변경 성공.");
					else
						alert("이미 존재하는 닉네임입니다.");
				})
				.catch(function(error) {
					alert("에러! : " + error);
				});
			})
			
		});
		
	</script>
	<script>
		
	</script>
</head>
<body>
	<div id='bg-none'></div>
	<!-- <form action="Home.jsp"> -->
		<div id="header">							
			<button class="logo" onclick="location.href = '${pageContext.request.contextPath}/';"><div>wayble</div></button>
		</div>
	<!-- </form> -->
	
	<div id="main">
		<div id="profile">
			<div id="profile-img-wrrapper">
				<c:if test="${not empty loginUserIdx and loginUserIdx != -1}">	<!-- 로그인했을때 -->
<%-- 					<img src="${pageContext.request.contextPath}/resources/img/icon_user.png" class="profile_img"/> --%>
					<%-- <img id="profileImage" src="${pageContext.request.contextPath}/upload/img/profile/${profileImg}?v=${sessionScope.profileVersion}" class="profile-img"/> --%>
					<img src="${pageContext.request.contextPath}/resources/upload/img/profile/${loginUserDto.profile}?v=${sessionScope.profileVersion}" class="profile-img" />
				</c:if>
				<c:if test="${empty loginUserIdx or loginUserIdx == -1}">	<!-- 로그인 안했을때 -->
					<img src="${pageContext.request.contextPath}/resources/img/icon_user.png" class="profile-img"/>
				</c:if>
				
				<form id="form-img-upload" action='${pageContext.request.contextPath}/uploadFile' method='post' enctype='multipart/form-data'>
					<input type='hidden' name='desc'/> <br/>
					<label for='plus'></label>
					<input type='file' id='plus' name='file'/> <br/>
					<!-- <input type='submit' value="" id="plus1"/> -->
				</form>
				<%-- <img src="${pageContext.request.contextPath}/resources/img/icon_plus.png" class="plus_button"/> --%>
			</div>
			<div class="nickname-wrapper">
				<div class="name">	
					<input type="text" class="nickname" value="${loginUserDto.nickname}"/>
				</div>
				<button class="update-nickname">수정</button>
			</div>	
		</div>
		<form action="PlannerUpdate.html">
			<div id="current-planner">
				<div><strong>작성 중인 여행 계획</strong></div>
				
				<div class="container">
					<c:forEach var="i" begin="0" end="3">
						<button data-currentPlanner-idx="${currentPlanner[i].plannerIdx}" class="planner-button" onclick="location.href='update';">
			        		<div class="flex-item">
								<div><strong>${currentPlanner[i].country}-${currentPlanner[i].name }</strong></div>
								<div>${currentPlanner[i].startDate}<br>~${currentPlanner[i].endDate}</div>          		
			        		</div>
		        		</button>
					</c:forEach>
	   			</div>
			</div>
		</form>
		<form action="Review.jsp"> <!-- 완료된 플래너로 연결하기 -->
			<div id="past-planner">
				<div><strong>내 과거 여행 계획</strong></div>
				
	        	<div class="container">
	        		<c:forEach var="i" begin="0" end="3">
		        		<button data-pastPlan-idx="${pastPlanner[i].plannerIdx}" class="planner-button">
		        			<div class="flex-item">
		        				<div><strong>${pastPlanner[i].country}-${pastPlanner[i].name }</strong></div>
								<div>${pastPlanner[i].startDate}<br>~${pastPlanner[i].endDate}</div> 
		        			</div>
		        		</button>
	        		</c:forEach>
	   			</div>
	        </div>
		</form>
		<button class='option'>
			<span>설정</span>
			<img src="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA3MDVfMTUy%2FMDAxNTkzOTEyOTE1MDE0.JFb30PTa8Ku4snKVmugT9Y70YLuomR5VwFC1xmaW4ogg.J9gQI0GJ9jTX9QUNahIZ-t2umg7E5zX6gF8L2q0IHnwg.PNG.aimode0821%2Fsetting.png&type=sc960_832">
		</button>
		<div id="calendar">
<%-- 			<img src="${pageContext.request.contextPath}/resources/img/calender.png"/> --%>
		</div>
	</div>
	
	<!--ChangePassWord----------------------------------------------------------------->
	<div class='change-pw'>
		 <h2>비밀번호 변경</h2>
		 <div class="input-bar">
			<input type="password" placeholder="현재 비밀번호"/>
		 </div>
		 <div class="input-bar">
			<input type="password" placeholder="새 비밀번호"/>
		 </div>
		 <div class="input-bar">
			<input type="password" placeholder="새 비밀번호 확인"/>
		 </div>
		 
		 <div class="button-group">
		 	<button class="btn-cancel btn-submit">취소</button>
		 	<button class="btn-update btn-submit">수정</button>
		 </div>	
	</div>
	
	<!--------------------------------------------------------------------------------->
	<div id="optionList">
		<button id="change-pw-btn">비밀번호 변경</button></br>
		<div id="divLine"></div> 
		<button id="Resign-btn">회원 탈퇴</button>
	</div>
	
	<!---회원탈퇴 ------------------------------------------------------------------------>
	<div id="Resign-main" class="center">
		<h2>정말로 Wayble을 탈퇴하겠습니까?<br/>모든 정보를 잃어버리며 복구 할 수 없습니다.</h2>
		<div><input type='checkbox' name='Resign-agreed'/> 예</div>
		</br>
		</br>
		<div><input type='text' id='Resign-text'  placeholder='내용을 이해했습니다.'/>
		<input type='password' id='Resign-pw' placeholder='현재 비밀번호입력.'/></div>
		<div>
			<button class="아이디정해야됨(체크하고 딜리트하기)">확인</button>
			<button class="btn-cancel btn-submit">취소</button>
		</div>
	</div>
	<!-- 백그라운드 색 ---------------------------------------------------------------------->
	<div id='bg-screen'></div>
</body>
</html>