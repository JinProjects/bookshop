<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<c:set var="myCartList" value="${cartMap.myCartList }" />
<c:set var="myGoodsList" value="${cartMap.myGoodsList }" />

<c:set var="totalGoodsNum" value="0" />
<c:set var="totalDeliveryPrice" value="0" />
<c:set var="totalDiscountedPrice" value="0" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript">
	//장바구니의 상품을 체크박스를 체크하면 총 가격을 합산
	function calcGoodsPrice(bookPrice,obj){
			//총 상품 금액, 최종 결제금액 ,총 상품 수
		var totalPrice, final_total_price,totalGoodsNum;
			//선택 상품 수량 
		var goods_qty=document.getElementById("cart_goods_qty");
			//총 상품 수
		var p_totalGoodsNum=document.getElementById("p_totalGoodsNum");
			//총 상품금액
		var p_total_goods_price=document.getElementById("p_total_goods_price");
			//최종 결제금액
		var p_final_totalPrice=document.getElementById("p_final_totalPrice");
		var h_totalGoodsNum=document.getElementById("h_totalGoodsNum");
		//
		var h_total_goods_price=document.getElementById("h_total_goods_price");
			//배송비
		var h_totalDeliveryPrice=document.getElementById("h_totalDeliveryPrice");
		var h_final_total_price=document.getElementById("h_final_totalPrice");
		//이벤트가 발생한 태그에 check되어있으면 true 아니면 false
		if(obj.checked==true){
			//총 상품수 = 총 상품수 + 상품 개수
			totalGoodsNum=Number(h_totalGoodsNum.value)+Number(goods_qty.value);
			//총 상품금액 = 총 상품금액 + (상품 개수*판매가)
			totalPrice=Number(h_totalGoodsPrice.value)+Number(goods_qty.value*bookPrice);
			//최종 결제금액 = 총 상품금액 + 배송비 
			final_total_price=totalPrice+Number(h_totalDeliveryPrice.value);
		}else{
			alert(bookPrice);
			totalGoodsNum=Number(h_totalGoodsNum.value)-Number(goods_qty.value);
			totalPrice=Number(h_total_goods_price.value)-Number(goods_qty.value)*bookPrice*0.9;
			final_total_price=totalPrice-Number(h_totalDeliveryPrice.value);
		}
		p_totalGoodsNum.value=totalGoodsNum;
		
		h_total_goods_price.value=totalPrice;
		h_final_total_price.value=final_total_price;
		
		p_totalGoodsNum.innerHTML=totalGoodsNum;
		p_total_goods_price.innerHTML=totalPrice;
		p_final_totalPrice.innerHTML=final_total_price;
	}
	//상품 아이디, 상품 할인 가격, 
	function modify_cart_qty(goods_id,bookPrice,index){
		//어떤 값이 들어오는지 체크
		var length = document.frm_order_all_cart.cart_goods_qty.length;
		alert(length);
		var _cart_goods_qty=0;
		if(length>1){
			_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty[index].value;
		}else{
			_cart_goods_qty=document.frm_order_all_cart.cart_goods_qty.value;
		}
		var cart_goods_qty = Number(_cart_goods_qty);
		
		$.ajax({
			type: "post",
			async: false,
			url: "${contextPath}/cart/modifyCartQty.do",
			data :{//서버로 보내는 데이터
				goods_id:goods_id,
				cart_goods_qty:cart_goods_qty
			},
			success: function(data,textStatus){//서버로 부터 받은 데이터
				if(data.trim()=='modify_success'){
					alert('수량을 변경했습니다.');
				}else{
					alert('다시 시도해 주세요!');
				}
			},
			error : function(data, textStatus){
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus){
				//alert("작업을 완료 했습니다.");
			}
			
		})
	}
	function delete_cart_goods(cart_id){
		var cart_id = Number(cart_id);
		var formObj = document.createElement("form");
		var i_cart = document.createElement("input");
		i_cart.name = "cart_id";
		i_cart.value = cart_id;
		
		formObj.appendChild(i_cart);
		document.body.appendChild(formObj);
		formObj.method = "post";
		formObj.action = "${contextPath}/cart/removeCartGoods.do";
		formObj.submit();
	}
	function fn_order_each_goods(goods_id,goods_title,goods_sales_price,fileName){
		var total_price,final_total_price,_goods_qty;
		var cart_goods_qty=document.getElementById("cart_goods_qty");
		
		_order_goods_qty=cart_goods_qty.value;
		
		var formObj = document.createElement("form");
		var i_goods_id = document.createElement("input");
		var i_goods_title =document.createElement("input");
		var i_goods_sales_price = document.createElement("input");
		var i_fileName = document.createElement("input");
		var i_order_goods_qty = document.createElement("input");
		
		i_goods_id.name = "goods_id";
		i_goods_title.name = "goods_title";
		i_goods_sales_price.name = "goods_sales_price";
		i_fileName.name = "goods_fileName";
		i_order_goods_qty.name = "order_goods_qty";
		
		i_goods_id.value = goods_id;
		i_goods_title.value = goods_title;
		i_goods_sales_price.value = goods_sales_price;
		i_fileName.value = fileName;
		i_order_goods_qty.value = _order_goods_qty; 
		
		formObj.appendChild(i_goods_id);
		formObj.appendChild(i_goods_title);
		formObj.appendChild(i_goods_sales_price);
		formObj.appendChild(i_fileName);
		formObj.appendChild(i_order_goods_qty);
		
		document.body.appendChild(form);
		formObj.method = "post";
		formObj.action = "${contextPath}/order/orderEachGoods.do";
		formObj.submit();
	}
	
	function fn_order_all_cart_goods(){
		var order_goods_qty;
		var order_goods_id;
		var objForm = document.frm_order_all_cart;
		var cart_goods_qty = objForm.cart_goods_qty;
		var h_order_each_goods_qty = objForm.h_order_each_goods_qty;
		var checked_goods = objForm.checked_goods;
		var length = checked_goods.length;
		
		if(length>1){
			for(var i=0; i<length; i++){
				if(checked_goods[i].checked==true){
					order_goods_id=checked_goods[i].value;
					order_goods_qty=cart_goods_qty[i].value;
					cart_goods_qty[i].value="";
					cart_goods_qty[i].value = order_goods_id+":"+order_goods_qty;
				}
			}
		}else{
			order_goods_id=checked_goods.value;
			order_goods_qty=cart_goods_qty.value;
			cart_goods_qty.value=order_goods_id+":"+order_goods_qty;
		}
		objForm.method="post";
		objForm.action="${contextPath}/order/orderAllCartGoods.do";
		objForm.submit();
	}
</script>
</head>
<body>
	<table class="list_view">
		<tbody align="center">
			<tr style="background:#33ff00">
				<td class="fixed">구분</td>
				<td colspan=2 class="fixed">상품명</td>
				<td>정가</td>
				<td>판매가</td>
				<td>수량</td>
				<td>합계</td>
				<td>주문</td>
			</tr>
			<c:choose>
				<c:when test="${empty myCartList }">
					<tr>
						<td colspan="8" class="fixed">
							<strong>장바구니에 상품이 없습니다.</strong>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<form name="frm_order_all_cart" >
					<c:forEach var="item" items="${myGoodsList }" varStatus="cnt">
						<tr>
						<!-- 정상적으론 form태그 선언이 안되지만 
							javascript에서 데이터로 사용하기 위해서 강제로 선언해서 사용
						-->
						<!-- cart_goods_qty DB에 저장된 상품 개수와 아이디 -->
							<c:set var="cart_goods_qty" value="${myCartList[cnt.count-1].cart_goods_qty }" />
							<c:set var="cart_id" value="${myCartList[cnt.count-1].cart_id }" />
							<td><input type="checkbox" name="checked_goods" value="${item.goods_id }" onclick="calcGoodsPrice(${item.goods_sales_price},this)" checked="checked"></td>
							<td class="goods_image">
								<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
									<img width="75" src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
								</a>
							</td>
							<td>
								<h2>
									<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">${item.goods_title}</a>
								</h2>
							</td>
							<td class="price"><span>${item.goods_price }원</span></td>
							<td>
								<strong>
									<fmt:formatNumber value="${item.goods_sales_price*0.9 }" type="number" var="discounted_price" />
									${discounted_price}원(10%할인)
								</strong>
							</td>
							<td>
								<input type="text" id="cart_goods_qty" name="cart_goods_qty" size=3 value="${cart_goods_qty }"><br>
								<a href="javascript:modify_cart_qty(${item.goods_id },${item.goods_sales_price*0.9 },${cnt.count-1 });">
									<img width="25" alt="" src="${contextPath }/resources/image/btn_modify_qty.jpg">
								</a>							
							</td>
							<td>
								<strong>
									<fmt:formatNumber value="${item.goods_sales_price*0.9*cart_goods_qty }" type="number" var="total_sales_price" />
									${total_sales_price }원
								</strong>
							</td>
							<td>
								<a href="javascript:fn_order_each_goods('${item.goods_id }','${item.goods_title }','${item.goods_sales_price }','${item.goods_fileName }')">
									<img width="75" alt="" src="${contextPath }/resources/image/btn_order.jpg">
								</a><br>
								<a href="#">
									<img width="75" alt="" src="${contextPath }/resources/image/btn_order_later.jpg">
								</a><br>
								<a href="#">
									<img width="75" src="${contextPath }/resources/image/btn_add_list.jpg"> 
								</a><br>
								<a href="javascript:delete_cart_goods('${cart_id }');">
									<img width="75" src="${contextPath}/resources/image/btn_delete.jpg">
								</a>
							</td>
							<!-- 총 상품 금액 -->
							<c:set var="totalGoodsPrice" value="${totalGoodsPrice+item.goods_sales_price*0.9*cart_goods_qty }" />
							<c:set var="totalGoodsNum" value="${totalGoodsNum+1 }"/><!-- foreach 반복수 = 총 상품수 -->
					</tr>
						</c:forEach>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<br>
	<br>
	<div class="clear"></div>
	
	<table width="80%" class="list_view" style="background:#cacaff">
	<tbody>
		<tr align="center" class="fixed">
			<td class="fixed">총 상품수</td>
			<td>총 상품금액</td>
			<td></td>
			<td>총 배송비</td>
			<td></td>
			<td>총 할인 금액</td>
			<td></td>
			<td>최종 결제금액</td>
		</tr>
		<tr cellpadding=40 align='center'>
			<td id=""><!-- 총 상품수 -->
				<p id="p_totalGoodsNum">${totalGoodsNum}개</p>
				<input id="h_totalGoodsNum" type="hidden" value="${totalGoodsNum }">
			</td>
			<td><!-- 총 상품금액  -->
				<p id="p_total_goods_price">
					<fmt:formatNumber value="${totalGoodsPrice }" type="number" var="total_goods_price" />
					${total_goods_price}원
				</p>
				<input id="h_total_goods_price" type="hidden" value="${totalGoodsPrice }">
			</td>
			<td>
				<img width="25" src="${contextPath }/resources/image/plus.jpg">
			</td>
			<td>
				<p id="p_totalDeliveryPrice">${totalDeliveryPrice }원</p>
				<input id="h_totalDeliveryPrice" type="hidden" value="${totalDeliveryPrice }">
			</td>
			<td>
				<img width="25" src="${contextPath }/resources/image/minus.jpg">
			</td>
			<td>
				<p id="p_totalSalesPrice">${totalDiscountedPrice }원</p>
				<input id="h_totalSalesPrice" type="hidden" value="${totalSalesPrice }">
			</td>
			<td>
				<img width="25" src="${contextPath }/resources/image/equal.jpg">
			</td>
			<td>
				<p id="p_final_totalPrice">
					<fmt:formatNumber value="${totalGoodsPrice+totalDeliveryPrice-totalDiscountedPrice }" type="number" var="total_price" />
					${total_price}원
				</p>
				<input id="h_final_totalPrice" value="${totalGoodsPrice+totalDeliveryPrice-totalDiscountedPrice }" type="hidden">
			</td>
		</tr>
	</tbody>
	</table>
	<center>
	<br><br>
		<a href="javascript:fn_order_all_cart_goods()">
			<img width="75" src="${contextPath }/resources/image/btn_order_final.jpg">
		</a>
		<a href="#">
			<img width="75" src="${contextPath }/resources/image/btn_shoping_continue.jpg">
		</a>
	</center>
</body>
</html>