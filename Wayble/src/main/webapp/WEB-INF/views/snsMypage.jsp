<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="jspf/snsMypage.jspf" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>SNS 마이페이지</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sns-post-detail.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sns-add-post.css"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sns-mypage.css"/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/chat-log.css'/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/sns-post-modify.css'/>
</head>
<body>
	<header id="header1">
  		<a href="${pageContext.request.contextPath}/home" class="logo">Waytalk</a>
	</header>
	<div id="header2"></div>
	<div id="main">
		<div id="left-column">
			<div id="profile-container" data-user-idx="${info.userIdx}">
				<div>
					<img src="${info.profile}" class="profile-img"/>
					<div class="profile-box">
						<div class="nickname-follow">
							<span class="nickname">${info.nickname}</span>
						    <input type="submit" value="팔로우" class="follow-btn" style="display:${loginUserIdx == info.userIdx ? 'none' : 'block'}">
						</div>
						<div class="info-row">
							<div class="info-box">
								<div class="label">팔로워</div>
								<div class="number follower-cnt">${myStats.follower}</div>
							</div>
							<div class="info-box">	
								<div class="label">팔로잉</div>
								<div class="number">${myStats.following}</div>
							</div>
							<div class="info-box">
								<div class="label">게시글</div>
								<div class="number post-count">${myStats.post}</div>
							</div>	
						</div>
					</div>
				</div>
			</div>
			<div id="post-scroll">
				<div id="post-container">
				<c:forEach var="post" items="${postList}">
					 <div class="post-click">
                        <div data-post-idx="${post.postIdx}">
                            <img src="${pageContext.request.contextPath}/wayble_upload/${post.img}" class="post-img"/>
                            <div style="font-size:18px;">${post.content}</div>
                            <div class="sns-box">
                            	<img src="${post.profile}" class="post-profile"/>
                                <span class="post-nickname">${post.nickname}</span>
                                <label class='post-like'>
									<input type='checkbox' style='display: none;' ${post.checkHeart ? "checked" : ""}/>
									<img src="${pageContext.request.contextPath}/resources/img/icon_empty_heart.png" class='unchecked'/>
									<img src="${pageContext.request.contextPath}/resources/img/icon_heart.png" class='checked'/>
								</label>
                                <span class="post-count-heart">${post.countHeart}</span>
                                <img src="${pageContext.request.contextPath}/resources/img/icon_reply.png" class="talk"/>
                                <span class="post-count-reply">${post.countReply}</span>
                                <img src="${pageContext.request.contextPath}/resources/img/icon_dots.png" class="menu-list"/>
                                <div class="select-option">
                                    <button class="delete-post">삭제하기</button> <br/>
                                    <button class="modify-post">수정하기</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
				</div>
			</div>
		</div>
		<div id="header3"></div>
		<div id="right-column">
			<div id="followers-following">
				<label><input type="radio" name="list" class="following"/>팔로잉</label>
				<label><input type="radio" name="list" class="follower"/>팔로워</label>
			</div>
			<div id="search-bar" class="center">
				<input type="text" placeholder="이름 검색"/>
				<button class="search-icon">
					<svg class="w-6 h-6 md:w-8 md:h-8" xmlns="http://www.w3.org/2000/svg"version="1.1" id="Capa_1" viewBox="0 0 56.966 56.966"><path d="M55.146,51.887L41.588,37.786c3.486-4.144,5.396-9.358,5.396-14.786c0-12.682-10.318-23-23-23s-23,10.318-23,23 s10.318,23,23,23c4.761,0,9.298-1.436,13.177-4.162l13.661,14.208c0.571,0.593,1.339,0.92,2.162,0.92 c0.779,0,1.518-0.297,2.079-0.837C56.255,54.982,56.293,53.08,55.146,51.887z M23.984,6c9.374,0,17,7.626,17,17s-7.626,17-17,17 s-17-7.626-17-17S14.61,6,23.984,6z"></path></svg>
				</button>
			</div>
			<div class="ff-list"></div>
		</div>
	</div>
	<div id="bg-screen"></div>
	
<!--sns-post-detail------------------------------------------------------------------------------------->
	<div id="post-detail" data-post-idx="">
		<div class="post-detail-left"></div>
		<div class="post-detail-right">
			<div>
				<input class='post-detail-x' type='image' src="${pageContext.request.contextPath}/resources/img/icon_x.png"/>
			</div>
			<div class="post-detail-comment-list"></div>
		</div>
	</div>
	
<!--ChatLog--------------------------------------------------------------------->
	<div id="chat-log"> 
		<div id="chat-log-header" data-chat-room-idx="" data-chat-user-idx="">
			<span></span>
			<button id='chat-log-x'><img src="${pageContext.request.contextPath}/resources/img/icon_x.png"/></button>
		</div>
		<div id='chat-log-content'></div>
		<div id="chat-log-send" class="center">
			<input type="text" placeholder='메시지 입력' name='text'/>
			<input type='image' src='${pageContext.request.contextPath}/resources/img/icon_send.png'/>
		</div>
	</div>
	
<!--sns-post-modify------------------------------------------------------------------------------------->
	<form id="post-modify" action="modify-post" method="post" enctype="multipart/form-data">
		<div id="modify-one">   
			<div>게시글 작성하기</div>
			<img class='post-modify-x' src='${pageContext.request.contextPath}/resources/img/icon_x.png'/>
		</div>
		<div id="add-post-two">
			<div>사진 업로드</div>
			<div class="add-post-box">
				<div class="add-post-empty-box">
					<div>
  						<img src="${pageContext.request.contextPath}/resources/img/icon_camera.png" style="width: 100%; height: 100%;" />
					  	<input type="file" name="file"/>
					</div>
				</div>
				<div class="post-images"></div>
			</div>
		</div>
		<div id="add-post-three">
			<div class="add-post-title">
				<span>문구 추가</span>
				<span>(선택)</span>
			</div>
			<div class="add-post-text-box">
				<textarea placeholder="" class="add-post-text-detail" maxlength="500" name="content"/></textarea>
			</div>	
			<div class="add-post-count"><span id="add-post-count-chat">0</span>/500</div>
		</div>
		<div id="add-post-four">
			<div>공개범위</div>
			<div class="add-post-btn-box">
				<div class="add-post-open-btn">
					<input type="radio" id="public" value="public" name="visibility" hidden>
    				<label for="public" class="open-range">전체 공개</label>

				    <input type="radio" id="private" value="private" name="visibility" hidden>
				    <label for="private" class="open-range">나만 보기</label>
				
				    <input type="radio" id="followers" value="followers" name="visibility" hidden>
				    <label for="followers" class="open-range">내 팔로워</label>
				</div>
		 		<div class="add-post-close-btn">
		 			<input type="submit" class="add-post-submit" value="글 등록하기"/>
		 		</div>	
		 	</div>
		</div>
	</form>
	
</body>
</html>