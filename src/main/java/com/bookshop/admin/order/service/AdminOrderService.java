package com.bookshop.admin.order.service;

import java.util.List;
import java.util.Map;

import com.bookshop.order.vo.OrderVO;

public interface AdminOrderService {
	public List<OrderVO> listNewOrder(Map condMap) throws Exception;
	public void modifyDeliveryState(Map<String, String> deliveryMap) throws Exception;
	public Map orderDetail(int order_id) throws Exception;
}
