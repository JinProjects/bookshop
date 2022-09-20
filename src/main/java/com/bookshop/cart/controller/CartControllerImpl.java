package com.bookshop.cart.controller;

import java.net.HttpCookie;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;

import com.bookshop.cart.service.CartService;
import com.bookshop.cart.vo.CartVO;
import com.bookshop.common.base.BaseController;
import com.bookshop.member.vo.MemberVO;
@Controller("cartController")
@RequestMapping(value="/cart")
public class CartControllerImpl extends BaseController implements CartController{
	@Autowired
	private CartService cartService;
	@Autowired
	private CartVO cartVO;
	@Autowired
	private MemberVO memberVO;
	
//	@Override
//	@RequestMapping(value="/myCartList.do", method= RequestMethod.GET)
//	public ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String viewName=(String)request.getAttribute("viewName");
//		ModelAndView mav = new ModelAndView(viewName);
//		HttpSession session = request.getSession();
//		MemberVO memberVO = (MemberVO)session.getAttribute("memberInfo");
//		//회원의 장바구니 리스트를 가져오기
//		String member_id = memberVO.getMember_id();
//		cartVO.setMember_id(member_id);
//		//dao에서 myCartList, myGoodsList을 담아서 리턴
//		Map<String,List> cartMap = cartService.myCartList(cartVO);
//		session.setAttribute("cartMap", cartMap);
//		return mav;
//	}
	
	@Override
	@RequestMapping(value="/myCartList.do", method= RequestMethod.GET)
	public ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		//비로그인 상태에서 세션은 null이라 아마도 nullexception이 발생할듯?
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		Map<String,List> cartMap = null;
		Cookie[] cookies = request.getCookies();
		
		if(memberVO != null && memberVO.getMember_id() != null) {
			cartMap = cartService.myCartList(cartVO);
			//세션에 저장해서 jsp이동
			//session.setAttribute("cartMap", cartMap);
		}else {
			//비로그인 상태에서 장바구니 클릭
			//쿠키데이터 불러와서 보내야함
			//DB를 사용해서 가지고 오는게 아니라 사용자의 쿠키파일정보를 읽어서 리스트를 가져온다.
			Cookie[] cookie = request.getCookies();
			cartMap = new HashMap<String,List>();
			List myCartList = new ArrayList();
			List goodsList = new ArrayList();
			//쿠키에 저장되어있는 goods_id를 dao에서 조회해서 map으로 얻어와서 리턴 
			for(int i=0; i<cookie.length; i++) {
				goodsList.add(cookie[i]);//쿠키정보를 리스트 저장
			}
			cartMap.put("myCartList", myCartList);
			
		}
		session.setAttribute("cartMap", cartMap);
		return mav;
	}
	//쿠키와 세션에 저장
	//비로그인은 쿠키만 불러와 데이터출력
	//로그인은 쿠키와 세션을 불러와 출력
	@Override
	@RequestMapping(value="/addGoodsInCart.do", method = RequestMethod.POST,produces = "application/text; charset=utf8")
	public @ResponseBody String addGoodsInCart(@RequestParam("goods_id") int goods_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		memberVO = (MemberVO)session.getAttribute("memberInfo");
		
		String member_id = memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		cartVO.setGoods_id(goods_id);
		boolean isAreadyExisted=cartService.findCartGoods(cartVO);
		if(isAreadyExisted == true) {
			return "already_existed";
		}else {
			cartService.addGoodsInCart(cartVO);
			return "add_success";
		}
	}
	
	//생성된 쿠키를 조회하
	@Override
	@RequestMapping(value="/addCookieGoodsInCart.do",method = RequestMethod.POST,produces = "application/text; charset=utf8")
	public @ResponseBody String addCookieGoodsInCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Enumeration e =  request.getParameterNames();
		boolean isAreadyExisted= false;
		Cookie[] cookie = request.getCookies();
		for(int i=0; i<cookie.length; i++) {
			System.out.println("cookie="+cookie[i].getValue());
		}
		String goodsId = request.getParameter("goods_id");
		
		//중복체크
		for(int i=0; i<cookie.length; i++) {
			if(cookie[i].getValue().equals(goodsId)) {
				isAreadyExisted = true;
				break;
			}
		}
		
		if(isAreadyExisted == true) {
			return "already_existed";
		}else {
			while(e.hasMoreElements()) {
				String goodsKey = (String) e.nextElement();
				String goodsValue = request.getParameter(goodsKey);
				CookieGenerator newCookie = new CookieGenerator();
				newCookie.setCookieMaxAge(60*10);
				newCookie.setCookieName(goodsKey+goodsValue);
				newCookie.addCookie(response, goodsValue);
			}
			return "add_success";
		}
	}
	@Override
	@RequestMapping(value="/modifyCartQty.do",method=RequestMethod.POST)
	public String modifyCartQty(@RequestParam("goods_id") int goods_id, @RequestParam("cart_goods_qty") int cart_goods_qty, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		memberVO=(MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		cartVO.setGoods_id(goods_id);
		cartVO.setMember_id(member_id);
		cartVO.setCart_goods_qty(cart_goods_qty);
		boolean result = cartService.modifyCartQty(cartVO);
		if(result == true) {
			return "modify_success";
		}else {
			return "modify_failed";
		}
	}
	@Override
	@RequestMapping(value="/removeCartGoods.do",method = RequestMethod.POST)
	public ModelAndView removeCartGoods(@RequestParam("cart_id") int cart_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		cartService.removeCartGoods(cart_id);
		mav.setViewName("redirect:/cart/myCartList.do");
		return mav;
	}
}
