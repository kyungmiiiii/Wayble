package com.wayble.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

class SMTPAuthenticator extends javax.mail.Authenticator {
    private String userid = null;
    private String pw = null;

    public SMTPAuthenticator(String id, String pw){
        this.userid = id;
        this.pw = pw;
    }
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userid, pw); 
    }
}

public class MailSender {
	public static boolean ifEngNum(char ch) {
		if(ch>='A' && ch<='Z') return true;
		if(ch>='a' && ch<='z') return true;
		if(ch>='0' && ch<='9') return true;
		return false;
	}
	
	// 인증코드 메일 보내기
	// 파라미터 : 보내는 이메일(emailTo), 인증코드(code)
	// 리턴값 : -
	public static void sendCodeMail(String emailTo, String code){
	    Properties prop = new Properties();
	    prop.put("mail.smtp.host", "smtp.naver.com");
	    prop.put("mail.smtp.port", "465");

	    // SMTP 인증 기능 사용시
	    prop.put("mail.smtp.auth", "true");

	    prop.put("mail.smtp.ssl.enable", "true");
	    prop.put("mail.transport.protocol", "smtp");
	    prop.put("mail.smtp.ssl.trust", "smtp.naver.com");

	    String emailId = "wbtest2025@naver.com";
	    String emailPw = "NXDZVER3UD6E";
	    
	    try {
	        // 1) SMTP 인증방식을 이용하는 경우: 보내는 사람 메일 아이디, 비밀번호
	        Authenticator auth = new SMTPAuthenticator(emailId, emailPw);
	        Session session = Session.getInstance(prop, auth);
	        //session.setDebug(true);  // 디버그 용도로만 사용(성능저하)
	        //

	        /* 2) SMTP 릴레이 방식으로 보내는 경우
	        Session session = Session.getInstance(prop);
	        // session.setDebug(true);  // 디버그 용도로만 사용(성능저하)
	        */

	        // 메일 언어셋
	        String charset = "UTF-8";

	        MimeMessage msg = new MimeMessage(session);

	        // 메일제목
	        String subject = "[Wayble] 회원가입 인증코드입니다.";
	        msg.setSubject(MimeUtility.encodeText(subject, charset, "B"));

	        // 메일 발신 날짜
	        msg.setSentDate(new Date());

	        // 보내는 사람의 메일주소(위에서 인증시 사용한 메일아이디와 동일한 메일주소 권장)
	        Address fromAddr = new InternetAddress(emailId, "발신자이름을여기에", charset);    // 이메일, 이름, charset
	        msg.setFrom(fromAddr);
	       
	        // 수신자
	        Address[] arrAddresses = new Address[1];
	        arrAddresses[0] = new InternetAddress(emailTo,"수신자이름을여기에", charset);

	        // 하나의 동일한 메일을 수신자 수만큼 지정하여 발송(동보메일)
	        msg.setRecipients(Message.RecipientType.TO, arrAddresses);

	        MimeMultipart multipart = new MimeMultipart();
	        MimeBodyPart msgPart = new MimeBodyPart();

	        // 메일 본문 html
	        String htmlMsg = "<h1>Wayble</h1><div>인증코드 : " + code + "</div>";
	        msgPart.setContent(htmlMsg, "text/html;charset=" + charset);
	        multipart.addBodyPart(msgPart);

	        // 첨부파일
	        /*
	        MimeBodyPart attachPart2 = new MimeBodyPart();
	        DataSource fds = new FileDataSource("C:\\Temp\\테스트.pptx");    // 첨부파일 경로
	        attachPart2.setDataHandler(new DataHandler(fds));
	        String filename = "테스트.pptx";    // 첨부파일 이름 지정
	        attachPart2.setFileName(MimeUtility.encodeText(filename, charset, "B"));

	        multipart.addBodyPart(attachPart2);
	        //*/
	        msg.setContent(multipart);

	        // 메일 전송
	        Transport.send(msg);
	        System.out.println("메일발송 성공");
	    } catch (MessagingException ex) {
	        System.out.println("메일전송 오류 - " + ex.toString());
	    } catch(UnsupportedEncodingException ex) {
	        System.out.println("인코딩 오류 - " + ex.toString());
	    } catch (Exception ex) {
	        System.out.println("기타 오류 - " + ex.toString());
	    }
	}

	public static void main(String[] args) {
		String code = "";
		while(code.length()<4) {
			char ch = 0;
			ch = (char)(Math.random()*'z');
			if(ifEngNum(ch))
				code += ch;
		}
		//code = code.toUpperCase();
		System.out.println("인증code : " + code);
		
		// send mail (testing)
		sendCodeMail("chg4723@naver.com", code);
		
	}
}
