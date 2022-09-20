package com.bookshop.mypage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bookshop.member.vo.MemberVO;
import com.bookshop.order.vo.OrderVO;

public interface MyPageDAO {
	public List<OrderVO> selectMyOrderGoodsList(String member_id) throws DataAccessException;
	public void updateMyOrderCancel(String order_id) throws DataAccessException;
	public MemberVO selectMyDetailInfo(String member_id) throws DataAccessException;
	public void updateMyInfo(Map<String, String> memberMap) throws DataAccessException;
	public List<OrderVO> selectMyOrderHistoryList(Map<String,String> dateMap) throws DataAccessException;
	public List<OrderVO> selectMyOrderInfo(String order_id) throws DataAccessException;
}
