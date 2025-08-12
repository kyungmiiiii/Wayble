<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/home.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/alarm.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/city-detail.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/select-date.css" />
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
	<link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap" rel="stylesheet">
	
	<%@ include file="jspf/home.jspf" %>
</head>
<body>
	<div id='bg-screen'></div>
	<div id="header" class="content" >
		<div><input type="submit" id="logo" class="click-header"/>Wayble</div>
		<h1>어디로 여행을 떠나시나요?</h1>
			<c:if test="${not empty loginUserIdx and loginUserIdx != -1}">
				<div>
					<div class="alarm-bell">
						<input type="submit" id="open-alarm" class="click-header" ><img src="${pageContext.request.contextPath}/resources/img/icon_bell.png">
						<div class="new-alarm" style="display:none">N</div>
					</div>
					<div>
						<button class="click-header" onclick="location.href = 'MypageForm';"></button>	<!-- 로그인했을때 프로필사진 있으면 프로필사진. 없으면 기본이미지.-->
							<c:if test="${empty loginUserDto || empty loginUserDto.profile}">
								<img src="${pageContext.request.contextPath}/resources/img/icon_user.png"/>
							</c:if>
							<c:if test="${not empty loginUserDto && not empty loginUserDto.profile}">
								<img src="${pageContext.request.contextPath}/resources/img/${loginUserDto.profile}?v=${sessionScope.profileVersion}" class="profile-img" />
							</c:if>
					</div>
					<div><span>${loginUserDto.nickname}</span></div>
					<div>
						<button class="click-header" style="margin-left: -53px; width: 50px; height: 40px;" onclick="logout()"></button>
					</div>
				</div>
			</c:if>
			<c:if test="${empty loginUserIdx or loginUserIdx == -1}">
				<div>
					<div><img src="${pageContext.request.contextPath}/resources/img/icon_user.png"/></div>
					<div>
						<input type="submit" id='login' class="click-header" onclick="location.href = 'loginForm';">
						<span>로그인</span>
					</div>
				</div>
			</c:if>
				
	</div>
	<div id=search-bar class="center">
		<input type="text" placeholder="국가명이나 도시명으로 검색해보세요." id='search'>
	</div>
	<input type="submit" class="click-search">
	
	<div id="content1">	
	<c:forEach var="i" begin="0" end="7">
		<div data-city-idx ="${defaultCity[i].cityIdx}" class="content1-item">
			<img src="${defaultCity[i].pic}"/>
			<div>${defaultCity[i].nameEng}</div>
			<div>${defaultCity[i].name}</div>
			<input type="submit" class="click-content1">
		</div>
	</c:forEach>
	
	</div>
	
	<c:if test="${not empty loginUserIdx and loginUserIdx != -1}">
		<div id="content2">
			<div class="waytalk">
				<div>WayTalk
					<input type="submit" class="click-waytalk" onclick="location.href='sns/home'">
				</div>
				<div>
					<div>
						<c:if test="${loginUserDto.profile==null}">
							<img src="${pageContext.request.contextPath}/resources/img/icon_user.png"/>
						</c:if>
							
						<c:if test="${loginUserDto.profile!=null}">
							<img src="${pageContext.request.contextPath}/resources/upload/img/profile/${loginUserDto.profile}?v=${sessionScope.profileVersion}" class="profile-img" />
						</c:if>
					</div>
					<div>${loginUserDto.nickname}</div>
				</div>
				<div class="my-fr">
					<div>팔로워
						<h4>${myStats.follower}</h4>
					</div>
					<div>게시글
						<h4>${myStats.post}</h4>
					</div>
					<div>팔로잉
						<h4>${myStats.following}</h4>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	
	<c:if test="${empty loginUserIdx or loginUserIdx == -1}">
		<div id="content2">
			<div class="waytalk">
				<div>WayTalk
				<input type="submit" class="click-waytalk" onclick="alert('로그인 후 이용가능합니다\n로그인 페이지로 이동합니다.'); location.href = 'loginForm';">
				</div>
				<div class="logout-my-fr">
					<div>
						<i style="font-family: 'Noto Sans KR'; font-weight:300;">"가벼운 짐 속에 설렘을 담아, 길 위로 나섰다"</i>
						<p>WayTalk속 사람들과 다양한 소통을 나눠보세요!</p>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	<div id="content3-title">다녀온 여행자들의 플래너</div>
	<div id="content3">
	
	<c:forEach var="i" begin="0" end="9">
		<div data-planner-idx="${plannerReview[i].plannerIdx}" class="content3-item">
			<img src="${plannerReview[i].pic}">
			<div>${plannerReview[i].title}</div>
			<input type="button" class="click-content3" onclick="location.href='plannerReview'">
		</div>
	</c:forEach>
	</div> 
	
<!--city-detail------------------------------------------------------------------------------------------------------>
	
	<div id="city-detail" data-city-idx="">
		<div><button id='city-detail-x'><img src="${pageContext.request.contextPath}/resources/img/icon_x.png"/></button></div>
		<div id="city-detail-content">
			<div id="city-detail-text">
				<div>TOKYO</div>
				<div><b>일본 도쿄</b></div>
				<div>세계의 여러 도지 중 어디에서도 경험할 수 없는 매력을 지닌 도쿄는 일본의 수도로서 도시의 중심에서 미래의 기술과 과거의 전통이 공존하는 독특한 장소입니다. 도쿄의 번화한 거리와 조용한 사찰, 최첨단의 빌딩과 고요한 정원 사이에서 당신은 진정한 도쿄의 매력을 발견할 수 있습니다.</div>
			</div>
			<div>
				<img src="https://cdn.myro.co.kr/prod/image/city/Tokyo.jpg"/>
			</div>
		</div>
		<div>
			<button id="city-detail-next">일정만들기 ></button>
		</div>
	</div>
	
<!--alarm------------------------------------------------------------------------------------------------------------>
	<div id="alarm-box" class="center">
		<div id="alarm-header" class="center">
			<h3>알림</h3>
		</div>
	</div>
	
<!--selectDate------------------------------------>
	<div id='select-date' action="planner" method="post">
		<h1 class="center">여행 기간이 어떻게 되시나요?</h1>
		<div class="center"><u>* 여행 일자는 <b>최대 10일</b>까지 설정 가능합니다.</u></div>
		<div class="center"><u>현지 여행 기간<b>(여행지 도착 날짜, 여행지 출발 날짜)</b>으로 입력해 주세요.</u></div>
		<div id="div_date_range_picker" class="center">
			<input type="text" style="opacity:0;" />
		</div>
      <button id='select-date-next'>선택</button>
	</div>
	
	
	
	
	
	
</body>
</html>