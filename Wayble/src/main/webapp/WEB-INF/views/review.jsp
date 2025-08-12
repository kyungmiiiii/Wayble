<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="jspf/review.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>	<!-- 맨 마지막 플래너리뷰 페이지 -->
<head>
	<meta charset="UTF-8">
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<title>1조 (리뷰 등 상세)</title>
</head>
<body>
	<div id="map_schedule_div">
		<div>	<!-- 오사카 여행 / 현지인 맛집 머시기 / day1 / 일정항목 -->
			<h6 class="city-name" data-city-lat="" data-city-lng=""></h6>
			<h1 class="title"></h1>
			<div>
				<select name="day" style="border:none;"></select>
			</div>
			<div class="timeline">
			</div>
		</div>
		<div>
			<span id="map"></span>
			<div id="float_div">
				<div>
					<ul>
					</ul>
				</div>
				<div class="place-detail"></div>
			</div>	
		</div>
	</div>	
					
	<div id="review_div">
		<div>
			<span>4.4</span>
			<span>/5</span>
			<span class="outer_star">
				<span class="inner_star"></span>
			</span>
		</div>
		<div>
			<div>
				총 <span>62</span>건
			</div>
			<div>
				<span>평점순 최신순</span>
				<input type="submit" style="margin-right:48px;" class="click_array" onclick="alert('평점순 눌림!')">
				<input type="submit" class="click_array" onclick="alert('최신순 눌림!')">
			</div>
				
		</div>
	</div>	
		<!-- article 시작.. 리뷰디테일dto -->
		<div id="review-list">
			<c:forEach var="plan" items="${plannerReview}">
					<div class="article">
						<c:if test="${not empty plannerReview}">
							<div class="article_header">
								<img src="https://image.hanatour.com/usr/static/img2/pc/com/rating_star_on.png"/>
								<span>${plan.rate}</span>
								<c:if test="${not empty plan.profile}">
									<img src="${pageContext.request.contextPath}/wayble_upload/${plan.profile}"/>
								</c:if>
								<c:if test="${empty plan.profile}">
									<img src="${pageContext.request.contextPath}/resources/img/icon_user.png"/>
								</c:if>
								<span>${plan.nickname}</span>
							</div>
							<div class="article_content">
								<p>${plan.content}</p>
							</div>
						</c:if>
					</div>
			</c:forEach>
		</div>	
				<c:if test="${empty plannerReview}">
					<div style="display: flex; justify-content: center; align-items: center; font-size:30px; padding:100px 0px;">아직 작성된 댓글이 없습니다. 첫번째로 댓글을 남겨보세요!</div>
				</c:if>	
			
		
		
		<!-- <div class="article">
			<div class="article_header">
				<img src="https://image.hanatour.com/usr/static/img2/pc/com/rating_star_on.png"/>
				<span>5</span>
				<img src="https://image.hanatour.com/usr/static/img2/mobile/icon/profile-64@2x.png"/>
				<span>ghk*****</span>
			</div>
			<div class="article_content">
				<p>남편 칠순기념으로 처음가보는 유럽여행이라서 걱정을 많이 하고 출발 했는데 비행기를 오래타야 하는것은 힘들었지만 현지에서 멋진경치와 웅장한 건물에 환호하며 10일 어톃게 지나갔는지 같은차에 타신분들과도 친하게 지내서 너무나 즐거웠던 잊을수 없는여행이였습니다 스위스에 호텔은 너무분워기가 있고경치도 매우좋은곳에 자리잡은곳이라 더욱 기억에 남을거여요~ 나이만 적다면 스위스는 다시 가보고 싶은고이고 베네치아도 TV여서 보던것과는 다르게 너무도 멋진곳입니다~ 이제는 추억속에 한페이지로 도어버린것이 아쉽네요</p>
			</div>
		</div>	
			article 끝
		article 시작
		<div class="article">
			<div class="article_header">
				<img src="https://image.hanatour.com/usr/static/img2/pc/com/rating_star_on.png"/>
				<span>5</span>
				<img src="https://image.hanatour.com/usr/static/img2/mobile/icon/profile-64@2x.png"/>
				<span>cho****</span>
			</div>
			<div class="article_content">
				<p>역시 서유럽 볼거리다양 핵심관광으로 알차고식사도좋앟구요 현지음식도맛있고 숙소도 깨끗 중간자유시간도 많아서 사진찍고 쇼핑하고 커피도한잔 넘 즐거운 시간이었어요 날씨까지 넘나 좋았어요 😍</p>
			</div>
		</div>	
			article 끝
		article 시작
		<div class="article">
			<div class="article_header">
				<img src="https://image.hanatour.com/usr/static/img2/pc/com/rating_star_on.png"/>
				<span>5</span>
				<img src="https://image.hanatour.com/usr/static/img2/mobile/icon/profile-64@2x.png"/>
				<span>yjh****</span>
			</div>
			<div class="article_content">
				<p>가족과 함께하는 첫 유럽여행 일정이 알차고 가이드 및 팀장님의 전문적인 해설과 배려 덕분에 즐거운 서유럽 여행을 잘 마쳤습니다. 특히 임현자 팀장님의 유쾌하고 안정적인 인솔로 여행기간내내 편안하고 즐거웠습니다. 다음 여행도 꼭 같이 하겠습니다. 감사합니다^^</p>
			</div>
		</div> -->
		<!-- article 끝 -->
	<p style="padding: 30px 250px 0px 278px; font-size:18px;">플래너가 도움이 됐다면 평점을 남겨보세요!</p>
	<div id="comment_star">
		<input type="radio" class="star clicked" name="rating" id="star1" value="1"/>
		<input type="radio" class="star clicked" name="rating" id="star2" value="2"/>
		<input type="radio" class="star clicked" name="rating" id="star3" value="3"/>
		<input type="radio" class="star clicked" name="rating" id="star4" value="4"/>
		<input type="radio" class="star clicked" name="rating" id="star5" value="5"/>
	</div>
	<div id="comment">
		<div>
			<textarea placeholder="리뷰를 남겨주세요."></textarea>
		</div>
	</div>
	<div id="regist">
		<input type="submit" class="click-regist" value="등록하기">
	</div>
</body>
</html>