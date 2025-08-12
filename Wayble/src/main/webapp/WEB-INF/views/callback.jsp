<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
  <head>
    <title>네이버로그인</title>
  </head>
  <body>
  <script>alert("여기가 콜백이다");</script>
  <%
    String clientId = "sIvyntAUE5uC91EOOqUG";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "wyQIfRTRK1";//애플리케이션 클라이언트 시크릿값";
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    String redirectURI = URLEncoder.encode("http://localhost:9090/www/naverLoginCheck", "UTF-8");
    String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
        + "&client_id=" + clientId
        + "&client_secret=" + clientSecret
        + "&redirect_uri=" + redirectURI
        + "&code=" + code
        + "&state=" + state;
    String accessToken = "";
    String refreshToken = "";
   
 	String str = "";
    try {
      URL url = new URL(apiURL);
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      con.setRequestMethod("GET");
      int responseCode = con.getResponseCode();
      BufferedReader br;
      if (responseCode == 200) { // 정상 호출
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {  // 에러 발생
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      String inputLine;
      StringBuilder res = new StringBuilder();
      while ((inputLine = br.readLine()) != null) {
        res.append(inputLine);
      }
      br.close();
      if (responseCode == 200) {
        //out.println(res.toString());
		JSONParser parsing = new JSONParser();
    	Object obj = parsing.parse(res.toString());
    	JSONObject jsonObj = (JSONObject)obj;
    			        
    	accessToken = (String)jsonObj.get("access_token");
    	refreshToken = (String)jsonObj.get("refresh_token");
   	  }
        str = res.toString();
    } catch (Exception e) {
      // Exception 로깅
    }
  %>
	<script>
		let str = <%=str%>;
		alert("액세스토큰 : " + str.access_token);
		location.href="naverLoginProfile?access_token=" + str.access_token;		
	</script>

  </body>
</html>