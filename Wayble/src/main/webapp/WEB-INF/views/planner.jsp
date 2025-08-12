<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="jspf/planner.jspf" %>
<%@ page import="com.wayble.util.CommonKey" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>플래너 시작</title>
	
	<script>
	</script>

	<style>
	</style>
</head>
<body>
	<div id='bg-screen'></div>
	<div id="first">
		<div id="title">
			<div>${setting.country} ${setting.city}</div>
			<div>${setting.title}</div>
			<div>${setting.startDate} ~ ${setting.endDate}</div>
		</div>
		<div id="content">
			<div id="con1">
				<div class="sub-title">여행 일차</div>
				<div class="date">
				<c:forEach var="i" begin="1" end="${setting.travelDate}">
					<input type="radio" id="day${i}" name="day" value="day${i}"/>
					<label for="day${i}" class='day-btn'>Day ${i}</label>
				</c:forEach>
				</div>
			</div>
			<div id="con2">
				<input type="text" placeholder="장소명을 입력하세요" name='search'/>
				<input type='image' src='${pageContext.request.contextPath}/resources/img/icon_search.png'/>
			</div>
			<div id="con3">
				<input type="radio" id="리뷰순" name="order" style="display:none;"/>
				<label for="리뷰순" class="order-btn">리뷰많은순</label>
				<input type="radio" id="별점순" name="order" style="display:none;"/>
				<label for="별점순" class="order-btn">별점순</label>
			</div>
			<div id="con4" data-token=""></div>
		</div>
	</div>
	<div class='flex'>
		<div id="second">
			<div class="flex">
				<div class="sub-title">내가 선택한 일정</div>
				<div><button id='hotel'>호텔입력</button></div>
			</div>
			<div class="selected-store-list">
			<c:forEach var="i" begin="1" end="${setting.travelDate}">
				<div class='day-block' data-day-idx="${dayList[i-1]}">
					<div class="day-title">day${i}</div>
				</div>
			</c:forEach>
			</div>
		</div>
		<div id="third">
			<div id="map" data-lat="${setting.cityLat}" data-lng="${setting.cityLng}"></div>
			<input id="make-planner-next" type="submit" value="다음"/>
			
			<form id="placeForm" method="post" action="${pageContext.request.contextPath}/planner/update">
				<input type="hidden" name="placeList" id="placeList">
				<input type="hidden" name="travelDate" value="${setting.travelDate}">
				<%-- <input type="text" name="plannerIdx" value="${setting.plannerIdx}"> --%>
			</form>
		</div>
	</div>
	
	
	<!--PlannerHotel------------------------------------------------------------------------------------------------------------>
	<div id="planner-hotel" data-token="">
		<div id="hotel-search-bar">
			<input type="text" id="hotel-input" placeholder="호텔 이름을 입력하세요"/>
			<img src='${pageContext.request.contextPath}/resources/img/icon_magnifier.png' alt="검색" class="search-icon"/>
		</div>
		<div class="search-result"></div>
	</div>
</body>
</html>