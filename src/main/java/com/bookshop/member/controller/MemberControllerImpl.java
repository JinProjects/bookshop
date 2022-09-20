package com.bookshop.member.controller;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.bookshop.cart.service.CartService;
import com.bookshop.cart.vo.CartVO;
import com.bookshop.common.base.BaseController;
import com.bookshop.goods.service.GoodsService;
import com.bookshop.member.service.MemberService;
import com.bookshop.member.vo.MemberVO;
@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl extends BaseController implements MemberController{
	@Autowired
	private MemberService memberService;
	@Autowired
	private CartService cartService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private MemberVO memberVO;
	@Autowired
	private CartVO cartVO;
	//로그인 시 쿠키의 장바구니를 테이블에 반영하는 것도 해보세요. 중복 상품은 제외해야 합니다. 
	//쿠키데이터 가져오고 회원 장바구니 데이터를 가져와 중복체크하고
	//
	@Override
	@RequestMapping(value="/login.do",method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(loginMap);
		//memberVO가 존재한다면 true
		if(memberVO!=null && memberVO.getMember_id()!=null) {
			HttpSession session = request.getSession();
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberVO);
			Map<String,List> myCartMap = null;
			//쿠키 길이가 체크
			Cookie[] cookie = request.getCookies();
			
			//회원아이디로 cart에 저장된 목록을 조회하고
			//쿠키값과 비교 후
			//중복된 데이터를 거르고 데이터 삽입한다.
			
			String memberId = memberVO.getMember_id();
			cartVO.setMember_id(memberId);
			
			//1.회원아이디로 장바구니 조회
			if(cookie.length != 0) {
				for(int i=0; i<cookie.length; i++) {
					if(!cookie[i].getName().equals("JSESSIONID")) {
						cartVO.setGoods_id(Integer.parseInt(URLDecoder.decode(cookie[i].getValue(),"utf-8")));
						cartService.addGoodsInCart(cartVO);
					}
				}
			}
			myCartMap = cartService.myCartList(cartVO);
			List myCartList = null;
			List myList = new ArrayList();
			String[] goodsId = null;
			//2.저장된 장바구니와 쿠키데이터 비교
			//cart에 상품이 담겨있다면 쿠키데이터와 합치고
			//담겨있지 않다면 그대로 출력하면 됨
			if(myCartMap != null) {
				//장바구니 맵의 키를 얻는다. 
				Set<String> keySet = myCartMap.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				//2.기존 멤버 장바구니 goodsId를 비회원일 때 장바구니와 중복체크
				//memberId로 조회한 맵데이터를 꺼내야한다.
				//myCartMap 키를 받는다.
				//키로 값을 얻어오기 위한 코드
				while(keyIterator.hasNext()) {
					//키를 얻는다.
					String key = keyIterator.next();
					//myCartMap에 저장된 list 값을 꺼낸다.[myCartList, myGoodsList]
					myList.add(myCartMap.get(key)); 
				}
				//index가 0번째는 myCartList가 담겨있다.
				myCartList = (List) myList.get(0);
				//myCartList의 데이터를 꺼내서 장바구니 goodsId와 비교하여 중복 제거
				//중복체크 방법1 보류
//				for(Object cartList : myCartList) {
//					//String data = (String)name;
//					cartVO = (CartVO) cartList;
//					if(cartVO.getGoods_id() != ) {
//						
//					}
//				}
				goodsId = new String[myCartList.size()];
				String cookieData = null;
				boolean flag = false;
				//카트의 상품 데이터를 가져오고 CartVO에 세팅 후 삽입
				//방법2
				//현재 장바구니에 들어있는 목록
				for(int i=0; i<myCartList.size(); i++) {
					cartVO = (CartVO) myCartList.get(i);
					goodsId[i] = String.valueOf(cartVO.getGoods_id());
					//쿠키장바구니 데이터 가져온다
					for(int j=0; j<cookie.length; j++) {
						if(cookie[j].getName().equals("goods_id")) {
							cookieData = cookie[j].getValue();
							//회원장바구니데이터와 쿠키장바구니데이터 중복체크
							//비교하고 바로 db에 데이터를 삽입하지말고
							//데이터 비교 후 플래그를 바꾸고 다른 코드에서 데이터 삽입
							if(!goodsId[i].equals(cookieData)) {
								cartVO.setGoods_id(Integer.parseInt(cookieData));
								flag = true;
								break;
							}
						}
					}
					if(flag == true) {
						//cartVO데이터 확인
						cartService.addGoodsInCart(cartVO);
						break;
					}
				}
			}
			//쿠키삭제
			for(int i=0; i<cookie.length; i++) {
				CookieGenerator cookieRemove = new CookieGenerator();
					cookieRemove.setCookieName(cookie[i].getName());
					cookieRemove.setCookieMaxAge(0);
					cookieRemove.addCookie(response, null);
			}
			
			String action = (String)session.getAttribute("action");
			//로그인을 상품주문을 통해 했다면 다시 주문화면으로 forward
			if(action!=null && action.equals("/order/orderEachGoods.do")) {
				mav.setViewName("forward:"+action);
			}else {
				mav.setViewName("redirect:/main/main.do");
			}
			
		}else {
			String message="아이디나 비밀번호가 틀립니다. 다시 로그인해주세요.";
			mav.addObject("message",message);
			mav.setViewName("/member/loginForm");
		}
		return mav;
	}
	@Override
	@RequestMapping(value="/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session=request.getSession();
		session.setAttribute("isLogOn", false);
		session.removeAttribute("memberInfo");
		mav.setViewName("redirect:/main/main.do");
		return mav;
	}
	@Override
	@RequestMapping(value="/addMember.do",method = RequestMethod.POST)
	public ResponseEntity addMember(@ModelAttribute("memberVO") MemberVO _memberVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			memberService.addMember(_memberVO);
			message = "<script>";
			message += "alert('회원 가입을 마쳤습니다. 로그인창으로 이동합니다.');";
			message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
			message += " </script>";
		} catch (Exception e) {
			message += "<script>";
			message += " alert('작업 중 오류가 발생했습니다. 다시 시도해 주세요');";
			message += " location.href='"+request.getContextPath()+"/member/memberForm.do';";
			message += " </script>";
		}
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	//ResponseEntity 뭐가 리턴되는지
	@Override
	@RequestMapping(value="/overlapped.do",method = RequestMethod.POST)
	public ResponseEntity overlapped(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseEntity resEntity = null;
		//true/false 리턴
		String result = memberService.overlapped(id);
		resEntity = new ResponseEntity(result,HttpStatus.OK);
		return resEntity;
	}
}
