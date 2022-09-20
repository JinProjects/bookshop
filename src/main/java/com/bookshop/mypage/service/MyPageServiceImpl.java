package com.bookshop.mypage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.member.vo.MemberVO;
import com.bookshop.mypage.dao.MyPageDAO;
import com.bookshop.order.vo.OrderVO;
@Service("myPageService")
public class MyPageServiceImpl implements MyPageService{
	@Autowired
	MyPageDAO myPageDAO;
	
	@Override
	public List<OrderVO> listMyOrderGoods(String member_id) throws Exception {
		
		return myPageDAO.selectMyOrderGoodsList(member_id);
	}
	
	@Override
	public void cancelOrder(String order_id) throws Exception {
		myPageDAO.updateMyOrderCancel(order_id);
	}
	@Override
	public MemberVO modifyMyInfo(Map<String, String> memberMap) throws Exception {
		String member_id=(String)memberMap.get("member_id");
		myPageDAO.updateMyInfo(memberMap);
		return myPageDAO.selectMyDetailInfo(member_id);
	}
	
	@Override
	public List<OrderVO> listMyOrderHistory(Map<String, String> dateMap) throws Exception {
		return myPageDAO.selectMyOrderHistoryList(dateMap);
	}
	
	@Override
	public List<OrderVO> findMyOrderInfo(String order_id) throws Exception {
		return myPageDAO.selectMyOrderInfo(order_id);
	}
}
