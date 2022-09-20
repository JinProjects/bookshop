package com.bookshop.admin.order.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.order.vo.OrderVO;

@Repository("adminOrderDAO")
public class AdminOrderDAOImpl implements AdminOrderDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public ArrayList<OrderVO> selectNewOrderList(Map condMap) throws DataAccessException {
		ArrayList<OrderVO> orderList = (ArrayList)sqlSession.selectList("mapper.admin.order.selectNewOrderList",condMap);
		return orderList;
	}
	@Override
	public void updateDeliveryState(Map<String, String> deliveryMap) throws DataAccessException {
		sqlSession.update("mapper.admin.order.updateDeliveryState", deliveryMap);
	}
	
	@Override
	public ArrayList<OrderVO> selectOrderDetail(int order_id) throws DataAccessException {
		ArrayList<OrderVO> orderList = (ArrayList)sqlSession.selectList("mapper.admin.order.selectOrderDetail",order_id);
		return orderList;
	}
}
