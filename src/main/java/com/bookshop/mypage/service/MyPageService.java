package com.bookshop.mypage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.bookshop.member.vo.MemberVO;
import com.bookshop.order.vo.OrderVO;

public interface MyPageService {
	public List<OrderVO> listMyOrderGoods(String member_id) throws Exception;
	public void cancelOrder(String order_id) throws Exception;
	public MemberVO	modifyMyInfo(Map<String,String> memberMap) throws Exception;
	public List<OrderVO> listMyOrderHistory(Map<String,String> dateMap) throws Exception;
	public List<OrderVO> findMyOrderInfo(String order_id) throws Exception;
}
