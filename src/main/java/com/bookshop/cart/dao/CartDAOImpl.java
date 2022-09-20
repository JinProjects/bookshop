package com.bookshop.cart.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.cart.vo.CartVO;
import com.bookshop.goods.vo.GoodsVO;
@Repository("cartDAO")
public class CartDAOImpl implements CartDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<CartVO> selectCartList(CartVO cartVO) throws DataAccessException {
		List<CartVO> cartList = sqlSession.selectList("mapper.cart.selectCartList",cartVO);
		return cartList;
	}
	@Override
	public List<GoodsVO> selectGoodsList(List<CartVO> cartList) throws DataAccessException {
		List<GoodsVO> myGoodsList = sqlSession.selectList("mapper.cart.selectGoodsList",cartList);
		return myGoodsList;
	}
	@Override
	public boolean selectCountInCart(CartVO cartVO) throws DataAccessException {
		String result = sqlSession.selectOne("mapper.cart.selectCountInCart",cartVO);
		return Boolean.parseBoolean(result);
	}
	@Override
	public List selectCookieGoodsList(List cartList) throws DataAccessException {
		List myGoodsList = sqlSession.selectList("mapper.cart.selectCookieGoodsList",cartList);
		return myGoodsList;
	}
	@Override
	public void insertGoodsInCart(CartVO cartVO) throws DataAccessException {
		int cart_id=selectMaxCartId();
		cartVO.setCart_id(cart_id);
		sqlSession.insert("mapper.cart.insertGoodsInCart",cartVO);
	}
	private int selectMaxCartId() throws DataAccessException {
		int cart_id = sqlSession.selectOne("mapper.cart.selectMaxCartId");
		return cart_id;
	}
	
	@Override
	public int updateCartGoodsQty(CartVO cartVO) throws DataAccessException {
		int result = sqlSession.insert("mapper.cart.updateCartGoodsQty",cartVO);
		return result;
	}
	@Override
	public void deleteCartGoods(int cart_id) throws DataAccessException {
		sqlSession.delete("mapper.cart.deleteCartGoods",cart_id);
	}
}
