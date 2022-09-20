package com.bookshop.goods.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.common.base.BaseController;
import com.bookshop.goods.service.GoodsService;
import com.bookshop.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController implements GoodsController{
	@Autowired
	private GoodsService goodsService;
	
	@Override
	@RequestMapping(value="/goodsDetail.do", method = RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("goods_id")String goods_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		HttpSession session = request.getSession();
		Map goodsMap = goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap",goodsMap);
		GoodsVO goodsVO = (GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
	}
	
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	public @ResponseBody String keywordSearch(@RequestParam("keyword") String keyword,
												HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//키워드가 비어있으면 종료
		if(keyword == null || keyword.equals("")) {
			return null;
		}
		
		keyword = keyword.toUpperCase();
		List<String> keywordList = goodsService.keywordSearch(keyword);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("keyword", keywordList);
		
		
		String jsonInfo = jsonObject.toString();
		System.out.println(jsonInfo);
		return jsonInfo;
	}
	@Override
	@RequestMapping(value="/searchGoods.do",method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		List<GoodsVO> goodsList = goodsService.searchGoods(searchWord);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsList",goodsList);
		return mav;
	}
	//최근 본 상품을 세션에 저장
	private void addGoodsInQuick(String goods_id, GoodsVO goodsVO, HttpSession session) {
		boolean already_existed = false;//이미 존재하면 true 존재하지 않으면 false
		List<GoodsVO> quickGoodsList;
		quickGoodsList = (List<GoodsVO>) session.getAttribute("quickGoodsList");
		//매개변수로 최근 본 상품(goods_id)을 세션에 담긴 리스트로 비교하고
		//최근 본 상품이 리스트에 등록되어 있으면 true로 바꾸고 for문 종료
		//등록되어 있지 않다면 리스트에 추가 후 세션에 바인딩 후 종료
		//최근 본 상품이 처음이라면 바로 리스트에 등록함
		if(quickGoodsList!=null) {
			if(quickGoodsList.size()<4) {
				for(int i=0; i<quickGoodsList.size(); i++) {
					GoodsVO _goodsBean = (GoodsVO)quickGoodsList.get(i);
					//상품아이디가 같으면 이미 존재하니까 true로 바꾸고 for문 종료
					if(goods_id.equals(String.valueOf(_goodsBean.getGoods_id()))) {
						already_existed = true;
						break;
					}
				}
				//리스트에 순차적으로 쌓이기 때문에 처음과 마지막 상품은 무조건 false
				if(already_existed==false) {
					quickGoodsList.add(goodsVO);
				}
			}
		}else {//첫 상품은 무조건 등록
			quickGoodsList = new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
		}
		Collections.reverse(quickGoodsList);
		
		session.setAttribute("quickGoodsList", quickGoodsList);
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}
	
}
