<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="jspf/snsHome.jspf" %>

    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>

	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/sns-alarm.css'/> 
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/sns-home.css'/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/chat-list.css'/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/open-chat-log.css'/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/chat-log.css'/>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/open-chat-search.css"/>	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/open-chat-info.css"/> 
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sns-add-post.css"/>	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/sns-post-detail.css"/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/open-chat-make.css'/>
	<link rel='stylesheet' href='${pageContext.request.contextPath}/resources/css/open-chat-out.css'/>
</head>

<body>
	<div class="to-home"></div>
	<div id="header">WayTalk</div>
	<div id="notice">
		<input class='chat-list-popup' type='image' src='${pageContext.request.contextPath}/resources/img/icon_message.png'/>
		<input class='alarm-popup' type='image' src='${pageContext.request.contextPath}/resources/img/icon_bell.png'/>
	</div>

	<div id="bg-screen"></div>
	<div id="middle">
		<div class="post-list">
		<c:forEach var="item" items="${postList}">
			<div class="post" data-post-idx="${item.postIdx}"> 
				<div onclick="location.href='mypage?postIdx=${item.postIdx}'">
					<img src="${item.profile}"/> 
					<span>${item.nickname}</span>
				</div>
				<div class="image-list">
					<div>
						<img src="${pageContext.request.contextPath}/resources/upload/img/profile/${item.img}"/>
					</div>
				</div>
				<div>
					<h3>${item.nickname}</h3>
					<span>${item.content}</span>
				</div>
				<div>
					<label class='post-like'>
						<input type='checkbox' class="post-heart"style='display: none;' ${item.checkHeart ? "checked" : ""}/>
						<img src="${pageContext.request.contextPath}/resources/img/icon_empty_heart.png" class='unchecked'/>
						<img src="${pageContext.request.contextPath}/resources/img/icon_heart.png" class='checked'/>
					</label>
					<div class='post-like-count'>${item.countHeart}</div>
					<button class='reply-popup'>
						<img src="${pageContext.request.contextPath}/resources/img/icon_reply.png"/>
					</button>
					<div class="post-count-reply">${item.countReply}</div>
				</div>
				<div>
					<input type="text" placeholder="댓글 달기" class="reply-popup"/>
				</div>
			</div>
			</c:forEach>
		</div>
		<div id="right">
			<div id='add-post-popup'> 
				<input type='image' src='${pageContext.request.contextPath}/resources/img/icon_camera.png'/>
				<span>게시글 작성</span>
			</div>
			<div class="center">
				<div>
					<img src="${dto.profile}"/>
					<button id='to-my-page' onclick='location.href="${pageContext.request.contextPath}/sns/mypage?postIdx=0"'><b>${dto.nickname}</b></button>
				</div>
				<div id="follow">
					<div>
						<h3 class="center">팔로워</h3>
						<span>${myStats.follower}</span>
					</div>
					<div>
						<h3 class="center">게시글</h3>
						<span>${myStats.post}</span>
					</div>
					<div>
						<h3 class="center">팔로잉</h3>
						<span>${myStats.following}</span>
					</div>
				</div>
			</div>
			<div>
				<input class='open-chat-search-bar' type="text" placeholder="검색하여 채팅방 참여하기"/>
				<input class='open-chat-search-popup' type='image' src='${pageContext.request.contextPath}/resources/img/icon_magnifier.png'/>
				<button class='make-open-chat-popup' type='button'><img src='${pageContext.request.contextPath}/resources/img/icon_plus.png'/></button>
			</div>
			<div class="my-open-chat-list"></div>
		</div>
	</div>
	
	<!--ChatList--------------------------------------------------------->
	<div id="chat-list"> 
		<div id='chat-list-header'>
			<span><b>메시지</b></span>
			<button id='chat-list-x'><img src="${pageContext.request.contextPath}/resources/img/icon_x.png"/></button>
		</div>
		<div class="chat-list-content"></div>
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
	
	<!--OpenChatLog---------------------------------------------------------------------------------->
	<div id='open-chat-log' data-chat-room-idx=""> 
		<div id="open-chat-log-header">
			<div></div>
			<div><button id='open-chat-log-more'><img src="${pageContext.request.contextPath}/resources/img/icon_dots.png" class="head-pic"/></button></div>
			<div><button id='open-chat-log-x'><img src="${pageContext.request.contextPath}/resources/img/icon_x.png" class="head-pic"/></button></div>
		</div>
		<div class='open-chat-log-content'></div>
		<div class="send">
			<input type="text" placeholder='메세지 입력' name='text'/>
			<input type='image' src='${pageContext.request.contextPath}/resources/img/icon_send.png'/>
		</div>
	</div>

	<!--OpenChatSearch---------------------------------------------------------------------------------->
	<div id="open-chat-search">
		<div id="open-chat-one">
			<div id="open-chat-title">
				<div>오픈채팅 찾기</div>
				<input class="open-chat-search-x" type='image' src='${pageContext.request.contextPath}/resources/img/icon_x.png'>
			</div>	
			<div id="open-chat-search-bar">
				<input type="text" placeholder="검색어를 입력해주세요"/>
				<button class="open-chat-search-icon">
					<svg class="w-6 h-6 md:w-8 md:h-8" xmlns="http://www.w3.org/2000/svg"version="1.1" id="Capa_1" viewBox="0 0 56.966 56.966"><path d="M55.146,51.887L41.588,37.786c3.486-4.144,5.396-9.358,5.396-14.786c0-12.682-10.318-23-23-23s-23,10.318-23,23 s10.318,23,23,23c4.761,0,9.298-1.436,13.177-4.162l13.661,14.208c0.571,0.593,1.339,0.92,2.162,0.92 c0.779,0,1.518-0.297,2.079-0.837C56.255,54.982,56.293,53.08,55.146,51.887z M23.984,6c9.374,0,17,7.626,17,17s-7.626,17-17,17 s-17-7.626-17-17S14.61,6,23.984,6z"></path></svg>
				</button>
				<div class="open-chat-search-title"></div>
			</div>
		</div>
		<div id="open-chat-search-two"></div>
	</div>
	
	<!--OpenChatInfo------------------------------------------------------------------------------------->
	<div id="open-chat-info" data-chat-room-idx="">
		<div id="open-chat-info-one">
			<div class='flex'>
				<div class="open-chat-info-font">오픈채팅</div>
				<input class='open-chat-info-x' type='image' src='${pageContext.request.contextPath}/resources/img/icon_x.png'/>
			</div>
		</div>
		<div id="open-chat-info-two">
			<div>채팅방 설명</div>
			<div class="open-chat-info-text"></div>
			<div class="warning">※참여자는 카테고리를 준수하여 채팅방 규율을 잘 지켜주세요!</div>
		</div>
		<div id="open-chat-info-three">
			<div class="member">참여 맴버</div>
			<div class="open-chat-info-profile"></div>
		</div>
		<div id="open-chat-info-four">
			<button class="chat-room-join"></button>
		</div>
	</div>
	
<!--SNSAddPost------------------------------------------------------------------------------------->
	<form id="add-post-main" action="add-post" method="post" enctype="multipart/form-data">
		<div id="add-post-one">   
			<div>게시글 작성하기</div>
			<img class='add-post-x' src='${pageContext.request.contextPath}/resources/img/icon_x.png'/>
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

	<!--SNSPostDetial------------------------------------------------------------------------------------->
	<div id="post-detail" data-post-idx="">
		<div class="post-detail-left"></div>
		<div class="post-detail-right">
			<div>
				<input class='post-detail-x' type='image' src="${pageContext.request.contextPath}/resources/img/icon_x.png"/>
			</div>
			<div class="post-detail-comment-list"></div>
		</div>
	</div>
	
	<!--OpenChatMake------------------------------------------------------------------------------------->
		<div id="make-open-chat"> 
		<div id="make-open-chat-header">
			<span><b>오픈채팅 만들기</b></span>
			<button class='make-open-chat-x'><img src="${pageContext.request.contextPath}/resources/img/icon_x.png"/></button>
		</div>
		<div id="make-open-chat-content">
			<div id="make-open-chat-name">
				<div class="make-open-chat-title">채팅방 이름 (필수)</div>
				<input type="text" placeholder="채팅방 이름" name='chat-room-name'/>
			</div>
			<div id="make-open-chat-category">
				<div class="make-open-chat-title">카테고리 선택 (필수)</div>
				<input type="radio" id="opt1" name="group" value='red'/>
				<label for="opt1">
					<div class="choice-chat-room">
						<div>
							<div style="background-color:rgb(255, 73, 159);"></div>
							<div>동행인 구하기</div>
						</div>
						<div>동행인을 구할 수 있습니다.</div>
					</div>
				</label>
				<input type="radio" id="opt2" name="group" value='yellow'/>
				<label for="opt2">
					<div class="choice-chat-room">
						<div>
							<div style="background-color:rgb(255, 200, 77);"></div>
							<div>플래너 공유</div>
						</div>
						<div>플래너를 공유할 수 있습니다.</div>
					</div>
				</label>
				<input type="radio" id="opt3" name="group" value='green'/>
				<label for="opt3">
					<div class="choice-chat-room">
						<div>
							<div style="background-color:rgb(0, 154, 50);"></div>
							<div>나라 / 지역별 채팅방</div>
						</div>
						<div>나라 / 지역에 대한 자유로운 대화가 가능합니다.</div>
					</div>
				</label>
				<div>※ 방장은 카테고리를 준수하여 채팅방 규율을 잘 지켜주세요!</div>
			</div>
			<div id="make-open-chat-desc">
				<div class="make-open-chat-title">채팅방 설명 (선택)</div>
				<input type="text" placeholder="채팅방 설명 (간략히)" class="chat-room-description"/>
				<div>0 / 30</div>
			</div>
			<div id="make-open-chat-limit">
				<div class="make-open-chat-title">최대 참가 인원 (필수): 최대 </div>
				<input type="number" value='0' name='limit'>
				<div class="make-open-chat-title">명</div>
			</div>
		</div>
		<button id="make-open-chat-footer" disabled>오픈채팅 만들기</button>
	</div>
	
<!--OpenChatOut------------------------------------------------------------------------------------->
	<div id="open-chat-out" data-chat-room-idx="">
		<div id="chat-out-one">
			<div class="font"></div>
			<div class="chat-out-x">
				<button class="open-chat-out-x"><img src="${pageContext.request.contextPath}/resources/img/icon_x.png" class="close"></button>
			</div>
		</div>
		<div id="chat-out-two">
			<div class="chat-content">채팅방 설명</div>
			<div class="chat-out-text-box"></div>
		</div>
		<div id="chat-out-three">
			<div class="member">참여 맴버</div>
			<div class="chat-out-profile-list"></div>
		</div>
		<div id="chat-out-four">
			<button class="chat-out-button">오픈채팅 나가기</button>
		</div>
	</div>
	
<!--Alarm------------------------------------------------------------------------------------->
	<div id="sns-alarm-box" class="center">
      <div id="sns-alarm-header" class="center">
         <h3>알림</h3>
      </div>
   </div>
   
   
</body>
</html>