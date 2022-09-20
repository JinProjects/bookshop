package com.bookshop.admin.goods.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.goods.vo.ImageFileVO;

public interface AdminGoodsDAO {
	public List<GoodsVO> selectNewGoodsList(Map<String,Object> condMap) throws DataAccessException;
	public int insertNewGoods(Map newGoodsMap) throws DataAccessException;
	public void insertGoodsImageFile(List fileList) throws DataAccessException;
	public GoodsVO selectGoodsDetail(int goods_id) throws DataAccessException;
	public List<ImageFileVO> selectGoodsImageFileList(int goods_id) throws DataAccessException;
	public void updateGoodsInfo(Map goodsMap) throws DataAccessException;
	public void deleteGoodsImage(int image_id) throws DataAccessException;
	public void updateGoodsImage(List imageFileList) throws DataAccessException;
 }
