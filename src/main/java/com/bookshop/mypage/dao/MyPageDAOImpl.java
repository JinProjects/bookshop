package com.bookshop.mypage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.member.vo.MemberVO;
import com.bookshop.order.vo.OrderVO;

@Repository("myPageDAO")
public class MyPageDAOImpl implements MyPageDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<OrderVO> selectMyOrderGoodsList(String member_id) throws DataAccessException {
		List<OrderVO> orderGoodsList = (List)sqlSession.selectList("mapper.mypage.selectMyOrderGoodsList",member_id);
		return orderGoodsList;
	}
	
	@Override
	public void updateMyOrderCancel(String order_id) throws DataAccessException {
		sqlSession.update("mapper.mypage.updateMyOrderCancel",order_id);
	}
	@Override
	public MemberVO selectMyDetailInfo(String member_id) throws DataAccessException {
		return sqlSession.selectOne("mapper.mypage.selectMyDetailInfo",member_id);
	}
	@Override
	public void updateMyInfo(Map<String, String> memberMap) throws DataAccessException {
		sqlSession.update("mapper.mypage.updateMyInfo",memberMap);
	}
	@Override
	public List<OrderVO> selectMyOrderHistoryList(Map<String, String> dateMap) throws DataAccessException {
		List<OrderVO> myOrderHistList=(List)sqlSession.selectList("mapper.mypage.selectMyOrderHistoryList",dateMap);
		return myOrderHistList;
	}
	@Override
	public List<OrderVO> selectMyOrderInfo(String order_id) throws DataAccessException {
		List<OrderVO> myOrderList = (List)sqlSession.selectList("mapper.mypage.selectMyOrderInfo",order_id);
		return myOrderList;
	}
}
