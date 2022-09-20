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
	//�α��� �� ��Ű�� ��ٱ��ϸ� ���̺� �ݿ��ϴ� �͵� �غ�����. �ߺ� ��ǰ�� �����ؾ� �մϴ�. 
	//��Ű������ �������� ȸ�� ��ٱ��� �����͸� ������ �ߺ�üũ�ϰ�
	//
	@Override
	@RequestMapping(value="/login.do",method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(loginMap);
		//memberVO�� �����Ѵٸ� true
		if(memberVO!=null && memberVO.getMember_id()!=null) {
			HttpSession session = request.getSession();
			session.setAttribute("isLogOn", true);
			session.setAttribute("memberInfo", memberVO);
			Map<String,List> myCartMap = null;
			//��Ű ���̰� üũ
			Cookie[] cookie = request.getCookies();
			
			//ȸ�����̵�� cart�� ����� ����� ��ȸ�ϰ�
			//��Ű���� �� ��
			//�ߺ��� �����͸� �Ÿ��� ������ �����Ѵ�.
			
			String memberId = memberVO.getMember_id();
			cartVO.setMember_id(memberId);
			
			//1.ȸ�����̵�� ��ٱ��� ��ȸ
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
			//2.����� ��ٱ��Ͽ� ��Ű������ ��
			//cart�� ��ǰ�� ����ִٸ� ��Ű�����Ϳ� ��ġ��
			//������� �ʴٸ� �״�� ����ϸ� ��
			if(myCartMap != null) {
				//��ٱ��� ���� Ű�� ��´�. 
				Set<String> keySet = myCartMap.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				//2.���� ��� ��ٱ��� goodsId�� ��ȸ���� �� ��ٱ��Ͽ� �ߺ�üũ
				//memberId�� ��ȸ�� �ʵ����͸� �������Ѵ�.
				//myCartMap Ű�� �޴´�.
				//Ű�� ���� ������ ���� �ڵ�
				while(keyIterator.hasNext()) {
					//Ű�� ��´�.
					String key = keyIterator.next();
					//myCartMap�� ����� list ���� ������.[myCartList, myGoodsList]
					myList.add(myCartMap.get(key)); 
				}
				//index�� 0��°�� myCartList�� ����ִ�.
				myCartList = (List) myList.get(0);
				//myCartList�� �����͸� ������ ��ٱ��� goodsId�� ���Ͽ� �ߺ� ����
				//�ߺ�üũ ���1 ����
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
				//īƮ�� ��ǰ �����͸� �������� CartVO�� ���� �� ����
				//���2
				//���� ��ٱ��Ͽ� ����ִ� ���
				for(int i=0; i<myCartList.size(); i++) {
					cartVO = (CartVO) myCartList.get(i);
					goodsId[i] = String.valueOf(cartVO.getGoods_id());
					//��Ű��ٱ��� ������ �����´�
					for(int j=0; j<cookie.length; j++) {
						if(cookie[j].getName().equals("goods_id")) {
							cookieData = cookie[j].getValue();
							//ȸ����ٱ��ϵ����Ϳ� ��Ű��ٱ��ϵ����� �ߺ�üũ
							//���ϰ� �ٷ� db�� �����͸� ������������
							//������ �� �� �÷��׸� �ٲٰ� �ٸ� �ڵ忡�� ������ ����
							if(!goodsId[i].equals(cookieData)) {
								cartVO.setGoods_id(Integer.parseInt(cookieData));
								flag = true;
								break;
							}
						}
					}
					if(flag == true) {
						//cartVO������ Ȯ��
						cartService.addGoodsInCart(cartVO);
						break;
					}
				}
			}
			//��Ű����
			for(int i=0; i<cookie.length; i++) {
				CookieGenerator cookieRemove = new CookieGenerator();
					cookieRemove.setCookieName(cookie[i].getName());
					cookieRemove.setCookieMaxAge(0);
					cookieRemove.addCookie(response, null);
			}
			
			String action = (String)session.getAttribute("action");
			//�α����� ��ǰ�ֹ��� ���� �ߴٸ� �ٽ� �ֹ�ȭ������ forward
			if(action!=null && action.equals("/order/orderEachGoods.do")) {
				mav.setViewName("forward:"+action);
			}else {
				mav.setViewName("redirect:/main/main.do");
			}
			
		}else {
			String message="���̵� ��й�ȣ�� Ʋ���ϴ�. �ٽ� �α������ּ���.";
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
			message += "alert('ȸ�� ������ ���ƽ��ϴ�. �α���â���� �̵��մϴ�.');";
			message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
			message += " </script>";
		} catch (Exception e) {
			message += "<script>";
			message += " alert('�۾� �� ������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');";
			message += " location.href='"+request.getContextPath()+"/member/memberForm.do';";
			message += " </script>";
		}
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	//ResponseEntity ���� ���ϵǴ���
	@Override
	@RequestMapping(value="/overlapped.do",method = RequestMethod.POST)
	public ResponseEntity overlapped(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseEntity resEntity = null;
		//true/false ����
		String result = memberService.overlapped(id);
		resEntity = new ResponseEntity(result,HttpStatus.OK);
		return resEntity;
	}
}
