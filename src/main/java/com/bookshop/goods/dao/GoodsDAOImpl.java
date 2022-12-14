package com.bookshop.goods.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.goods.vo.ImageFileVO;
@Repository("goodsDAO")
public class GoodsDAOImpl implements GoodsDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<GoodsVO> selectGoodsList(String goodsStatus) throws DataAccessException {
		List<GoodsVO> list = null; 
		list = sqlSession.selectList("mapper.goods.selectGoodsList",goodsStatus); 
		
		return list;
	}
	@Override
	public GoodsVO selectGoodsDetail(String goods_id) throws DataAccessException {
		GoodsVO goodsVO = sqlSession.selectOne("mapper.goods.selectGoodsDetail",goods_id);
		return goodsVO;
	}
	@Override
	public List<ImageFileVO> selectGoodsDetailImage(String goods_id) throws DataAccessException {
		List<ImageFileVO> imagesList = sqlSession.selectList("mapper.goods.selectGoodsDetailImage",goods_id); 
		return imagesList;
	}
	@Override
	public List<String> selectKeywordSsearch(String keyword) throws DataAccessException {
		List<String> list = sqlSession.selectList("mapper.goods.selectKeywordSearch",keyword);
		return list;
	}
	@Override
	public List<GoodsVO> selectGoodsBySearchWord(String searchWord) throws DataAccessException {
		List<GoodsVO> goodsList = sqlSession.selectList("mapper.goods.selectGoodsBySearchWord",searchWord);
		return goodsList;
	}
}
