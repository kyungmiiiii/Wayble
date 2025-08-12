package com.wayble.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wayble.dto.CityDto;
import com.wayble.dto.DayDto;
import com.wayble.dto.PartnerDto;
import com.wayble.dto.PlannerDetailDto;
import com.wayble.dto.PlannerInfoDto;
import com.wayble.dto.PostDto;
import com.wayble.dto.ReviewDetailDto;
import com.wayble.dto.UserPlaceNameDto;
import com.wayble.dto.UsersDto;
import com.wayble.service.ChatService;
import com.wayble.service.CommonService;
import com.wayble.service.PlannerService;
import com.wayble.service.SnsService;
import com.wayble.util.MailSender;

@Controller
public class HomeController {
	@Autowired
	CommonService coSvc;
	@Autowired
	SnsService sSvc;
	@Autowired
	PlannerService pSvc;
	@Autowired
	ChatService chSvc;
	@Autowired
	ServletContext servletContext;

// LOGIN ---------------------------------------------------------------------------------------------------
	
	// 네이버 로그인 테스트
	// 파라미터 : -
	@RequestMapping("/naverLoginTest")
	public String n1() {
		return "naverlogin";
	}

	// 네이버 로그인
	// 파라미터 : -
	@RequestMapping("/naverLoginCallback")
	public String n2() {
		return "callback";
	}
	
	// 로그인 화면으로 이동 (작성자 : 최호준)
	// 파라미터 : -
	@GetMapping("/loginForm") 
	public String login() { 
		return "login";
	}
	
	// 로그인 여부 확인 (작성자 : 최호준)
	// 파라미터 : 이메일(email), 비밀번호(pw)
	@PostMapping("/loginAction") //post 타입을 여기서 실행하고
	public ModelAndView login(String email, String pw, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		int userIdx = -1;
		UsersDto loginUserDto = null;
		try {
			userIdx = coSvc.loginUserCheck(email, pw);
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(userIdx != -1) {	//로그인 성공
			loginUserDto = coSvc.getNicknameAndProfileFromIdx(userIdx);
			HashMap<String,Integer> myStats = sSvc.getUserStats(userIdx);
			for(String key : myStats.keySet()) {
				System.out.println(key + " : " + myStats.get(key) );
			}
			
			session.setAttribute("myStats", myStats);	// 팔로워,팔로잉,게시글 
			session.setAttribute("loginUserDto", loginUserDto);	// 닉네임,프로필사진
			session.setAttribute("loginUserIdx", userIdx);	// 로그인체크 
			mav.setViewName("redirect:/");
			
		}else {
			mav.addObject("login_msg", "로그인정보를 다시 확인해주세요.");
			mav.setViewName("login");
		}
		return mav;
	}
	
	// 로그아웃 (작성자 : 최호준)
	// 파라미터 : -
	@GetMapping("/logout")
	public String logoutAction(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	// 회원가입 창으로 이동(작성자 : 최호준)
	// 파라미터 : -
	@GetMapping("/signUpForm")
	public String signupForm() {
		return "signUp";
	}
	
	// 회원가입 (작성자 : 최호준)
	// 파라미터 : 이메일(email), 비밀번호(pw), 닉네임(nickname), 새 비밀번호(pw2)
	@PostMapping("/signUpAction")
	public ModelAndView singupAction(String email, String pw, String nickname, String pw2) {
		ModelAndView mav = new ModelAndView();
		
		if(pw.equals(pw2)) {
			try {
				coSvc.addSignUp(email,pw,nickname);
			}catch(Exception e) {
				e.printStackTrace();
			}
			mav.addObject("SignUp_msg", "회원가입 완료! 다시 로그인해주세요 로그인화면으로 이동합니다.");
			mav.setViewName("login");
		}
		
		return mav;
	}
	
	// 비밀번호 변경 화면으로 이동 (작성자 : 최호준)
	// 파라미터 : -
	@GetMapping("/passwordResetForm")
	public String ResetPw() {
		return "resetPw"; 
	}
	
	// 새 비밀번호 메일 발송 (작성자 : 최호준)
	// 파라미터 : 이메일(email)
	@GetMapping("/ResetComplete")
	public String changeRandomPw(HttpSession session)  {
		String email = (String) session.getAttribute("find_pw_email");
		UsersDto userinfo = coSvc.getUserByEmail(email);
		
		MailSender ms = new MailSender();
		
		String code = "";
		while(code.length()<6) {
			char ch = 0;
			ch = (char)(Math.random()*'z');
			if(ms.ifEngNum(ch))
				code += ch;
		}
		code = code.toUpperCase();
		ms.sendCodeMail(email, code);   // 변경된 비밀번호 메일 발송.
		
		//비번 새비번 유저인덱스
		try{
			coSvc.updatePw(userinfo.getPassword(), code, userinfo.getUserIdx());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/loginForm";
	}
// HOME -------------------------------------------------------------------------------------------------
	
	// 플래너 수정 성공 후 홈화면으로 이동 (작업자 : 이경미)
	// 파라미터 : -
	@GetMapping("/home")
    public String home() {
        return "home";
    }

	// 네이버 로그인 확인
	// 파라미터 : -
	@RequestMapping("/naverLoginCheck")
	public String n3() {
		return "/";
	}
	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}
	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}
	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

	// 네이버로 로그인하기
	// 파라미터 : 아이디(id), 프로필(profileImage), 이메일(email), 이름(name)
	@RequestMapping("/naverLoginProfile")
	public String n4(@RequestParam("access_token") String accessToken) {
		String token = accessToken; // 네이버 로그인 접근 토큰;
		String header = "Bearer " + token; // Bearer 다음에 공백 추가

		String apiURL = "https://openapi.naver.com/v1/nid/me";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);

		String responseBody = get(apiURL, requestHeaders);

		JSONParser parsing = new JSONParser();
		Object obj = "";
		try {
			obj = parsing.parse(responseBody);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		jsonObj = (JSONObject) jsonObj.get("response");
		String id = (String) jsonObj.get("id");
		String profileImage = (String) jsonObj.get("profile_image");
		String email = (String) jsonObj.get("email");
		String name = (String) jsonObj.get("name");

		return "forward:/";
	}
	
	// 홈 화면으로 이동
	// 파라미터 : -
	@RequestMapping("/")
	public String home(Model model, HttpSession session) {
		List<CityDto> defaultCity = null;
		try {
			defaultCity = coSvc.getcityInfo();
		}catch(Exception e) { e.printStackTrace(); }
		List<Map<String,Object>> plannerReview = pSvc.getplannerList();
		
		model.addAttribute("plannerReview", plannerReview);	//플래너 리뷰 뿌리기
		model.addAttribute("defaultCity", defaultCity);	// 전체 
		
		if(session.getAttribute("loginUserIdx")==null) {
			// 로그인x시,
			model.addAttribute("loginUserIdx", "");
			model.addAttribute("loginNull_msg", "로그인 후 이용가능합니다.\n로그인화면으로 이동합니다.");
		}
		return "home";
	}
	
// MYPAGE --------------------------------------------------------------------------------------------------------
	
	// 마이페이지 홈 화면으로 이동(작성자 : 최호준)
	// 파라미터 : 
	@GetMapping("/MypageForm")
    public String mypage(Model model,HttpSession session) {
		int userIdx = (Integer)session.getAttribute("loginUserIdx");
      
		UsersDto loginUserDto = coSvc.getUserInfo(userIdx);
		session.setAttribute("loginUserDto", loginUserDto);
		List<Map<String, Object>> currentPlanner = pSvc.currentPlanner(userIdx);
		List<Map<String, Object>> pastPlanner = pSvc.pastPlanner(userIdx);
		session.setAttribute("profileVersion", System.currentTimeMillis());
      
		model.addAttribute("currentPlanner",currentPlanner);
		model.addAttribute("pastPlanner",pastPlanner);
      
		return "mypage";
    }
	
	// 파일 업로드
	// 파라미터 : 파일(file)
	@PostMapping("/uploadFile")
	public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
	    int userIdx = (Integer)session.getAttribute("loginUserIdx");
	    if(file == null || file.isEmpty()) {
	    	result.put("result", false);
	        result.put("error", "업로드된 파일이 없습니다.");
	        return "redirect:/MypageForm";
	    } 
	    // 1. upload폴더를 만들어서, 여기에 저장한다고 알려줘야 함.
	    // (upload폴더의 절대경로 얻기)
	    String path = servletContext.getRealPath("/resources/upload/img/profile"); // "upload/profile/01" 이런식으로도 가능
	    // 파일이 저장될 폴더("upload")의 절대경로(C:\ 로 시작)
	      
	    // "upload" 폴더가 없으면, 생성: 
	    File f = new File(path).getAbsoluteFile();
	    if(!f.exists()) { // f.exists()가 false이면, 폴더가 없는 것
	    	// path폴더가 없으면 생성
	        f.mkdirs(); // 해당 폴더 (=디렉터리) 필요시 상위 폴더들까지 한번에 생성함. 폴더생성임. 
	    }
	    // 파일원본이름 받아서 저장된 이름으로 바꾸기.
	    String originName = file.getOriginalFilename();
	    String savedName = UUID.randomUUID() + "_" + originName;
	    String cleanedName = savedName.replaceAll(" ", "_");
	    try {
	    	file.transferTo(new File(path, cleanedName));
	    } catch (IllegalStateException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	      
	    String profilePath = "/resources/upload/img/profile/" + cleanedName;
	      
	    UsersDto updatedUserDto = coSvc.getUserInfo(userIdx);
	    session.setAttribute("loginUserDto", updatedUserDto);
	      
	    try {
	    	coSvc.updateProfile(cleanedName, userIdx);
	    } catch(Exception e) { e.printStackTrace(); }
	      session.setAttribute("profileVersion", System.currentTimeMillis());
	      return "redirect:/MypageForm";
	    }
	
// SNS ------------------------------------------------------------------------------------------------------
	
	// SNS 홈 화면으로 이동 (작성자 : 서수련)
	// 파라미터 : -
	@RequestMapping("/sns/home")
	public String snsHome(Model model, HttpSession session) {
		int userIdx = Integer.parseInt(session.getAttribute("loginUserIdx") + "");
		model.addAttribute("dto", coSvc.getUserInfo(userIdx));
		model.addAttribute("myStats", sSvc.getUserStats(userIdx));
		model.addAttribute("postList", sSvc.getTotalPostList(userIdx));
		model.addAttribute("openChat", chSvc.loadMyOpenChatList(userIdx));
		return "snsHome";
	}

	// SNS 마이페이지 화면으로 이동 (작성자 : 서수련)
	// 파라미터 : -
	@RequestMapping("/sns/mypage")
	public String snsMyPage(HttpSession session, Model model, @RequestParam int postIdx) {
		Integer loginUserIdx = (Integer)session.getAttribute("loginUserIdx");
		if(loginUserIdx == null) { return "redirect:/login"; }
		int userIdx = 0;
		if(postIdx == 0) {
			userIdx = loginUserIdx;
		} else {
			PostDto dto = sSvc.getPostDtoByPostIdx(postIdx);
			userIdx = dto.getUserIdx();
		}
		model.addAttribute("info", coSvc.getUserInfo(userIdx));
		model.addAttribute("myStats", sSvc.getUserStats(userIdx));
		model.addAttribute("postList", sSvc.getUsersPostList(loginUserIdx, userIdx));
		return "snsMypage";
	}
	
	// 게시물 작성하기 (작성자 : 서수련)
	// 파라미터 : 파일(file), 게시물 내용(content)
	@PostMapping("/sns/add-post")
	public String addPost(@RequestParam("file") MultipartFile file, @RequestParam("content") String content,
			@RequestParam("visibility") String visibility, HttpSession session) {
		String fileName = file.getOriginalFilename();
		String uploadFolder = "wayble_upload";
		
		// 웹 애플리케이션의 실제 경로를 가져옴 (예: Tomcat이 압축 해제한 war 파일의 경로)
	    String realPath = servletContext.getRealPath("/"); 
	    // 이 realPath 아래에 wayble_upload/upload 폴더를 만듦
	    Path dirPath = Paths.get(realPath, uploadFolder); 
		try {
			Files.createDirectories(dirPath);
		} catch(Exception e) { e.printStackTrace(); }

		File fileToSave = new File(dirPath.toFile(), fileName);
		
		try {
			file.transferTo(fileToSave);
		} catch (IOException e) { e.printStackTrace(); }
		int userIdx = Integer.parseInt(session.getAttribute("loginUserIdx") + "");
		sSvc.addPost(userIdx, visibility, content, fileName);
		return "redirect:/sns/home";
	}
	
// PLANNER ---------------------------------------------------------------------------------------------------------
	
	// 플래너 시작 (작성자 : 서수련)
	// 파라미터 : 도시 기본키(cityIdx)
	@RequestMapping("/planner/start")
	public String maekPlanner(int cityIdx, String travelDate, HttpSession session) {
		int userIdx = Integer.parseInt(session.getAttribute("loginUserIdx") + "");
		int plannerIdx = pSvc.startPlanner(userIdx, cityIdx, travelDate);
		return "redirect:/planner?plannerIdx=" + plannerIdx + "&cityIdx=" + cityIdx;
	}

	// 플래너 명소 선택 화면 (작성자 : 이경미)
	// 파라미터 : 플래너 기본키(plannerIdx), 도시 기본키(cityIdx)
	@GetMapping("/planner")
	public String planner(int plannerIdx, int cityIdx, Model model) {
		HashMap<String, Object> map = pSvc.loadPlannerBasicInfo(plannerIdx, cityIdx);
		List<Integer> dayList = pSvc.getDayIdxList(plannerIdx, Integer.parseInt(map.get("travelDate") + ""));
		model.addAttribute("setting", map);
		model.addAttribute("dayList", dayList);
		return "planner";
	}

	// 플래너 화면 (작업자 : 이경미)
	// 파라미터 : 플래너 기본키(plannerIdx)
	@GetMapping("/planner/update")
	public String showPlannerUpdatePage(@RequestParam("plannerIdx") String sPIdx, HttpSession session, Model model) {
		int pIdx = -1;
		try {
			pIdx = Integer.parseInt(sPIdx);
		} catch(NumberFormatException e) { System.out.println("(예외) pIdx 파라미터 = " + sPIdx); e.printStackTrace(); }
		
		Integer loginUserIdx = (Integer)session.getAttribute("loginUserIdx");
		if(loginUserIdx == null) {
			return "redirect:/loginForm";
		}
		UsersDto loginUser = (UsersDto) coSvc.getUserInfo(loginUserIdx);
		
		session.setAttribute("loginUser", loginUser);
		
		PlannerDetailDto pDto = pSvc.getPlannerDetailByIdx(pIdx);
		CityDto cDto = pSvc.getCityDetail(pDto.getCityIdx());
		List<DayDto> dDto = pSvc.getDaysByPlannerIdx(pIdx);
		
		Map<Integer, List<UserPlaceNameDto>> userPlaceList = new LinkedHashMap<>();
		for(DayDto day : dDto) {
			List<UserPlaceNameDto> places = pSvc.getPlacesByDayIdx(day.getDayIdx());
			userPlaceList.put(day.getDayIdx(), places);
		}
		
		// 날짜 포맷
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 일차 리스트 정렬
		dDto.sort(Comparator.comparingInt(DayDto::getDayIdx));

		// dayIdx의 최소값 찾기 (보정용)
		int minDayIdx = dDto.stream().mapToInt(DayDto::getDayIdx).min().orElse(1);

		// 날짜 계산
		Map<Integer, String> dayDateMap = new HashMap<>();
		if (pDto.getStartDate() != null) {
		    Calendar base = Calendar.getInstance();
		    base.setTime(pDto.getStartDate());

		    for (DayDto day : dDto) {
		        Calendar temp = (Calendar) base.clone();
		        temp.add(Calendar.DATE, day.getDayIdx() - minDayIdx); // 보정값 적용
		        dayDateMap.put(day.getDayIdx(), sdf.format(temp.getTime()));
		    }
		}
	    Set<Integer> keyset = userPlaceList.keySet();
	    Iterator<Integer> itr = keyset.iterator();
	    while(itr.hasNext()) {
	    	Integer key = itr.next();
	    	List<UserPlaceNameDto> valueList = (List<UserPlaceNameDto>)userPlaceList.get(key);
	    	for(UserPlaceNameDto dto : valueList) {
	    		System.out.println(key + " : " + dto.getUserPlaceIdx() + " : " + dto.getName());
	    	}
	    }
		model.addAttribute("pDto", pDto);
		model.addAttribute("cDto", cDto);
		model.addAttribute("dDto", dDto);
		model.addAttribute("userPlaceList", userPlaceList);
		model.addAttribute("dayDateMap", dayDateMap);
		model.addAttribute("startDateStr", sdf.format(pDto.getStartDate()));
		model.addAttribute("endDateStr", sdf.format(pDto.getEndDate()));
		
		return "plannerUpdate";
	}
	
	// 이메일로 사용자 찾기 및 동행인 등록하기 (작업자 : 이경미)
	// 파라미터 : 인증코드(code), 유저 이메일(userEmail)
	@GetMapping("/accept-invite")
	public String acceptInvite(@RequestParam("code") String code,
							   @RequestParam("userEmail") String userEmail,
							   HttpSession session, Model model,
							   RedirectAttributes redirectAttributes) {
		// 이메일로 사용자 찾기
		UsersDto user = coSvc.getUserByEmail(userEmail);
		if(user == null) {
			model.addAttribute("error", "존재하지 않는 사용자입니다.");
			return "error";
		}
		// 동행자 등록
		PartnerDto partner = new PartnerDto();
		partner.setCode(code);
		pSvc.acceptPartner(partner);

		Integer plannerIdx = pSvc.getPlannerIdxByCode(code);

		if (plannerIdx == null || plannerIdx <= 0) {
		    model.addAttribute("error", "유효하지 않은 플래너 정보");
		    return "error";
		}
		// 세션 로그인 처리
		session.setAttribute("login", user);
		
		// 플래너 업데이트 화면으로 이동
		return "home";
	}
	
	// REVIEW ---------------------------------------------------------------------------------------------------------
	
	// 플래너 리뷰 (작성자 : 최호준)
	// 파라미터 : 플래너 기본키(plannerIdx)
	@GetMapping("/plannerReview")
	public String plannerReview(int plannerIdx, Model model) {
		
		List<ReviewDetailDto> plannerReview = pSvc.getReviewList(plannerIdx); //ReviewDetailDto+UserDto 가져옴
		PlannerInfoDto pIDto = pSvc.getPlannerInfoDto(plannerIdx);		// 플래너Idx로 'PlannerInfoDto' 가져옴
		CityDto cDto = coSvc.cityinfoFromPlannerIdx(plannerIdx);	// 플래너Idx로, '시티DTO' 가져옴
		//CityDto cDto = coSvc.getCityInfoSortFromIdx(cDto.getCityIdx());
		
		model.addAttribute("plannerIdx", plannerIdx);
		model.addAttribute("pIDto", pIDto);
		model.addAttribute("cDto", cDto);
		model.addAttribute("plannerReview",plannerReview);
		return "review";
	}

}
