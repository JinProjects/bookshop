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
//		//ȸ���� ��ٱ��� ����Ʈ�� ��������
//		String member_id = memberVO.getMember_id();
//		cartVO.setMember_id(member_id);
//		//dao���� myCartList, myGoodsList�� ��Ƽ� ����
//		Map<String,List> cartMap = cartService.myCartList(cartVO);
//		session.setAttribute("cartMap", cartMap);
//		return mav;
//	}
	
	@Override
	@RequestMapping(value="/myCartList.do", method= RequestMethod.GET)
	public ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		//��α��� ���¿��� ������ null�̶� �Ƹ��� nullexception�� �߻��ҵ�?
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		Map<String,List> cartMap = null;
		Cookie[] cookies = request.getCookies();
		
		if(memberVO != null && memberVO.getMember_id() != null) {
			cartMap = cartService.myCartList(cartVO);
			//���ǿ� �����ؼ� jsp�̵�
			//session.setAttribute("cartMap", cartMap);
		}else {
			//��α��� ���¿��� ��ٱ��� Ŭ��
			//��Ű������ �ҷ��ͼ� ��������
			//DB�� ����ؼ� ������ ���°� �ƴ϶� ������� ��Ű���������� �о ����Ʈ�� �����´�.
			Cookie[] cookie = request.getCookies();
			cartMap = new HashMap<String,List>();
			List myCartList = new ArrayList();
			List goodsList = new ArrayList();
			//��Ű�� ����Ǿ��ִ� goods_id�� dao���� ��ȸ�ؼ� map���� ���ͼ� ���� 
			for(int i=0; i<cookie.length; i++) {
				goodsList.add(cookie[i]);//��Ű������ ����Ʈ ����
			}
			cartMap.put("myCartList", myCartList);
			
		}
		session.setAttribute("cartMap", cartMap);
		return mav;
	}
	//��Ű�� ���ǿ� ����
	//��α����� ��Ű�� �ҷ��� ���������
	//�α����� ��Ű�� ������ �ҷ��� ���
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
	
	//������ ��Ű�� ��ȸ��
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
		
		//�ߺ�üũ
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
