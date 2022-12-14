package com.bookshop.order.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.order.vo.OrderVO;
@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO{
	@Autowired
	private SqlSession sqlSession;
	//데이터가 거의 null
	@Override
	public void insertNewOrder(List<OrderVO> myOrderList) throws DataAccessException {
		int order_id = selectOrderID();
		for(int i=0; i<myOrderList.size(); i++) {
			OrderVO orderVO = (OrderVO)myOrderList.get(i);
			orderVO.setOrder_id(order_id);
			sqlSession.insert("mapper.order.insertNewOrder",orderVO);
		}
	}
	@Override
	public void removeGoodsFromCart(List<OrderVO> myOrderList) throws DataAccessException {
		for(int i=0; i<myOrderList.size(); i++) {
			OrderVO orderVO = (OrderVO)myOrderList.get(i);
			sqlSession.delete("mapper.order.deleteGoodsFromCart",orderVO);
		}
	}
	private int selectOrderID() throws DataAccessException{
		return sqlSession.selectOne("mapper.order.selectOrderID");
	}
}
