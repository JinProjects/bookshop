package com.bookshop.admin.goods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.goods.vo.ImageFileVO;
@Repository("adminGoodsDAO")
public class AdminGoodsDAOImpl implements AdminGoodsDAO{
	@Autowired
	private SqlSession sqlSession;
	@Override
	public List<GoodsVO> selectNewGoodsList(Map<String, Object> condMap) throws DataAccessException {
		List<GoodsVO> goodsList = sqlSession.selectList("mapper.admin.goods.selectNewGoodsList",condMap);
		return goodsList;
	}
	@Override
	public int insertNewGoods(Map newGoodsMap) throws DataAccessException {
		sqlSession.insert("mapper.admin.goods.insertNewGoods",newGoodsMap);
		return Integer.parseInt((String)newGoodsMap.get("goods_id"));
	}
	@Override
	public void insertGoodsImageFile(List fileList) throws DataAccessException {
		for(int i=0; i<fileList.size(); i++) {
			ImageFileVO imageFileVO = (ImageFileVO)fileList.get(i);
			sqlSession.insert("mapper.admin.goods.insertGoodsImageFile",imageFileVO);
		}
	}
	@Override
	public GoodsVO selectGoodsDetail(int goods_id) throws DataAccessException {
		GoodsVO goodsBean = new GoodsVO();
		goodsBean = sqlSession.selectOne("mapper.admin.goods.selectGoodsDetail",goods_id);
		return goodsBean;
	}
	@Override
	public List<ImageFileVO> selectGoodsImageFileList(int goods_id) throws DataAccessException {
		List imageList = new ArrayList();
		imageList = sqlSession.selectList("mapper.admin.goods.selectGoodsImageFileList",goods_id);
		return imageList;
	}
	@Override
	public void updateGoodsInfo(Map goodsMap) throws DataAccessException {
		sqlSession.update("mapper.admin.goods.updateGoodsInfo",goodsMap);
	}
	
	@Override
	public void deleteGoodsImage(int image_id) throws DataAccessException {
		sqlSession.delete("mapper.admin.goods.deleteGoodsImage",image_id);
	}
	@Override
	public void updateGoodsImage(List imageFileList) throws DataAccessException {
		for(int i=0; i<imageFileList.size(); i++) {
			ImageFileVO imageFileVO = (ImageFileVO)imageFileList.get(i);
			sqlSession.update("mapper.admin.goods.updateGoodsImage",imageFileVO);
		}
	}
}
