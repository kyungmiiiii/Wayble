<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
	<script>
	// SockJS 클라이언트 생성
	const socket = new SockJS('/www/ws');
	const stompClient = Stomp.over(socket);

	// 웹소켓 연결
	stompClient.connect({}, function (frame) {
	    console.log('Connected: ',frame);
	});

	    // 구독 설정
	  /*   stompClient.subscribe('/topic/public', function (greeting) {
	        // 메시지 수신 처리
	        console.log(greeting.body);
	    }); */

	   /*  // 메시지 전송
	    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
	        'sender': 'user1',
	        'content': 'Hello'
	    }));  */

	
	function subscribe(invoke) {
		let idx = $(invoke).attr("data-chat-room-idx");
		
		// 구독 설정
	    stompClient.subscribe(`/topic/\${idx}`, function (greeting) {
	    	console.log("인덱스 들어옴!!");
	        console.log(greeting.body);
	        console.log(greeting.body.content);
	    });
	    // 메시지 전송
	    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
	        sender : 'user1',
	        content : 'Hello',
	        chatRoomIdx : idx
	    })); 
		
	}
	
	 
	</script>
	
</head>
<body>
	<h1 data-chat-room-idx="20" onclick="subscribe(this)">20번 채널 구독</h1>
	<h1 data-chat-room-idx="30" onclick="subscribe(this)">30번 채널 구독</h1>
	<h1 data-chat-room-idx="40" onclick="subscribe(this)">40번 채널 구독</h1>
	<input type="text" id="message"/>
	<input type="submit" value="보내기"/>
	<div id="chat-log"></div>

</body>
</html>