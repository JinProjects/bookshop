package com.bookshop.admin.goods.service;

import java.util.List;
import java.util.Map;

import com.bookshop.goods.vo.GoodsVO;

public interface AdminGoodsService {
	public List<GoodsVO> listNewGoods(Map<String,Object> condMap) throws Exception;
	public int addNewGoods(Map addNewGoods) throws Exception;
	public Map goodsDetail(int goods_id) throws Exception;
	public void modifyGoodsInfo(Map goodsMap) throws Exception;
	public void removeGoodsImage(int image_id) throws Exception;
	public void addNewGoodsImage(List imageFileList) throws Exception;
	public void modifyGoodsImage(List imageFileList) throws Exception;
} 
