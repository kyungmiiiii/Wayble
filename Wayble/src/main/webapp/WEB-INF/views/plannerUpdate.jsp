<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  <!-- 선택 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@	include file="jspf/plannerUpdate.jspf" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>플래너 수정</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/planner-update.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/planner-memo.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/planner-partner-list.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/planner-email.css"/>
</head>
<body>
	<div id="planner-main">
		<div id="box-one">
			<div class="planner-header">
				<div class="planner-title">
					<div class="title-wrapper" data-planner-idx="${pDto.plannerIdx}">
						<input type="text" class="title" placeholder="" value="<c:out value='${pDto.title}' default='제목 없음'/>"/>
						<button class="update-title">수정</button>
					</div>
					    <fmt:formatDate value="${pDto.startDate}" pattern="yyyy-MM-dd" var="startDateStr" />
						<fmt:formatDate value="${pDto.endDate}" pattern="yyyy-MM-dd" var="endDateStr" />
						
						<span style="font-size: 25px; font-weight: bold; margin-left: 10px;">
						    ${startDateStr} ~ ${endDateStr}
						</span>
				</div>
			</div>
			<div class="room-manager">
				<img src="${pageContext.request.contextPath}/resources/img/${pDto.profile}" class="profile"/>
				<button id="planner-nickname">${pDto.nickname}</button>
				<button id="add-partner-popup" class="invite-button">초대하기</button>
			</div>
		</div>
		
		<div id="box-two">
			<div id="two-left">
	    		<c:forEach var="date" items="${userPlaceList.entrySet()}" varStatus="dateBox">
	    			<div class="day-column" data-day-idx="${date.key}">
		    			<div class="day-box">
	    					<div class="day-title">${dateBox.index + 1}일차</div>
	    					<div class="date">${dayDateMap[date.key]}</div>
	    				</div>	
						<div class="selete">
						    <label>
						    	<input type="radio" name="daySelect" value="${dateBox.index + 1}" hidden>
						    	<span class="select-btn" data-day-idx="${date.key}">선택</span>
						    </label>
						</div>
					    <div class="place-list">
				            <c:forEach var="place" items="${date.value}">
				            	<div class="place" data-user-place-idx="${place.userPlaceIdx}">
					            	<div class="place-box">
				                		<div class="place-start">
				                			<div class="place-number"></div>
				                		</div>
				                		<div class="place-center">	
				                    		<div class="place-item">${place.name}</div>
				                    	</div>
				                    </div>
				                    	<div class="place-end">
				                    		<img src="${pageContext.request.contextPath}/resources/img/icon_dots.png" class="menu-list">
				                    	</div>
				                    	<div class="selete-option" style="display: none;">
				                    		<button class="show-memo-popup">작성 메모보기</button>
				                    		<button class="delete-place-btn">일정 삭제하기</button>
				                    	</div>
				                </div>
				            </c:forEach>
					    </div>
		                <div class="place-move"></div>
		                <div class="move-button" style="display: none;">
		                	<input type="radio" id="transportation" name="move${dateBox.index}" hidden>
		                	<label for="transportation" class="btn-submit3">자가<span class="checkmark"></span></label>
		                	<input type="radio" id="walk" name="move" hidden>
		                	<label for="walk" class="btn-submit3">도보<span class="checkmark"></span></label>
		                </div>
					</div>
				</c:forEach>
			</div>
			<div id="two-right">
				<div id="map"></div>
				<h3 style="color: blue;">총 소요시간 및 경로 안내</h3>
				<div id="summary" style="font-size:16px;"></div>
				<button class="out-btn">나가기</button>
			</div>
		</div>
	</div>
	
	<div id="bg-screen"></div>
	
	<!-- PlannerMemo.jsp --------------------------------------------------------------- -->
	<div id="memo" style="display: none;" data-user-place-idx="">
		<div id="memo-title">
			<div class="memo">메모</div>
		</div> 
			<button class="window-close"><img src="${pageContext.request.contextPath}/resources/img/icon_x.png" class="close"></button>
		<div id="memo-content"><textarea></textarea></div>
		<div class="memo-last-user border"></div>
		<div id="memo-end">
			<div class="button-group">
			 	<button class="all-memo-del-btn">삭제</button>
			 	<button class="memo-finish-btn">완료</button>
			</div>
		</div>	
	</div>
	
	<!-- PlannerEmail.jsp (동행인 초대하기) ------------------------------------------------- -->
	<div id="email-main"> <!-- style="DISPLAY:BLOCK !IMPORTANT;" -->
	 	<div id="email-one">
	 		<div class="email-title">
	 			<div class="email-address">이메일 주소</div>
	 			<button class="email-window-close"><img src="${pageContext.request.contextPath}/resources/img/icon_x.png" class="close"></button>
	 		</div>
	 		<div id="email-search-bar" class="search">
				<input type="text" id="inviteEmail" placeholder="이메일 입력"/>
				<button class="search-icon">
					<input type='image' src="${pageContext.request.contextPath}/resources/img/icon_magnifier.png"/>
				</button>
			</div>
	 	</div>
	 	<div id="email-two">
	 		<div>검색한 유저의 이메일 목록</div>
	 	</div>
	 </div>
	
	<!-- PlannerPartnerList.jsp (동행인 리스트) -------------------------------------------- -->
	<div id="plannerPartner-main">
	<button class="partner-window-close"><img src="${pageContext.request.contextPath}/resources/img/icon_x.png" class="close"></button>
		<div id="partner-Profile-list">
		  <c:forEach var="partner" items="${partners}">
		    <div class="profileList" planner-idx="${partner.plannerIdx}" user-idx="${partner.userIdx}">
		      <img src="${pageContext.request.contextPath}/resources/img/${partner.profile}" class="partner-profile" />
		      <span class="nickname">${partner.nickname}</span>
		      <span class="delete">
		        <button class="partner-delete-btn" onclick="deletePartner(this)">삭제</button>
		      </span>
		    </div>
		  </c:forEach>
		</div>
	</div>
</body>
</html>