<%@page import="org.apache.commons.collections.bag.SynchronizedSortedBag"%>
<%@page import="com.bookshop.member.vo.MemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="goods" value="${goodsMap.goodsVO }" />
<c:set var="imageList" value="${goodsMap.imageList }" />
<%
	pageContext.setAttribute("crcn", "\n");
	pageContext.setAttribute("br", "<br>");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
#layer{
	z-index:2;
	position: absolute;
	top: 0px;
	left: 0px;
	width: 100%;
}

#popup{
	z-index:3;
	position: fixed;
	text-align: center;
	left: 50%;
	top: 45%;
	width: 300px;
	height: 200px;
	
}
#close{
	z-index:4;
	float: right;
}

</style>
<script type="text/javascript">
	//장바구니 구현
	function add_cart(goods_id, goods_title,goods_sales_price){
		alert("goods_id:"+goods_id+",goods_title:"+",goods_sales_price:"+goods_sales_price);
		$.ajax({
			type:"post",
			async:false,
			<% 
				MemberVO memberVO =  (MemberVO)session.getAttribute("memberInfo");
				if(memberVO != null && memberVO.getMember_id() != null){
			%>
				url:"${contextPath}/cart/addGoodsInCart.do",
				data:{
					goods_id:goods_id
				},
			<%		
				}else{
			%>	
				url:"${contextPath}/cart/addCookieGoodsInCart.do",
				data:{
					//상품아이디
					goods_id:goods_id
				},
			<%
				}
			%>
			success : function(data,textStatus){
				if(data.trim()=="add_success"){
					imagePopup('open','.layer01');
				}else if(data.trim()=="already_existed"){
					alert("이미 카트에 등록된 상품입니다.");
				}
			},
			error : function(data,textStatus){
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus){
				//alert("작업을 완료 했습니다.");
			}
		});
	}
	//자바스크립트는 매개변수 개수가 상관이 없나?
	//답: 상관없음 호출하는 곳에서 두개의 인수가 있다면 첫번째 인수만 사용	
	function imagePopup(type){
		if(type == 'open'){
		alert(type);
			//팝업창을 연다.
			jQuery('#layer').attr('style','visibility:visible');
			//페이지를 가리기위한 레이어 영역의 높이를 페이지 전체의 높이와 같게 한다.
			jQuery('#layer').height(jQuery(document).height());
		}else if(type == 'close'){
			jQuery('#layer').attr('style','visibility:hidden');
		}
	}
	//구매하기
	//구매하기 버튼 미작동
	function fn_order_each_goods(goods_id, goods_title, goods_sales_price,goods_fileName){
		var _isLogOn=document.getElementById("isLogOn");
		var isLogOn=_isLogOn.value;
		
		if(isLogOn=="false" || isLogOn==''){
			alert("로그인 후 주문이 가능합니다!!!");
		}
		var total_price,fianl_total_price;//총 가격, 수정안하는 총가격?
		//상품갯수
		var order_goods_qty = document.getElementById("order_goods_qty");
		//form 생성
		var formObj = document.createElement("form");
		//상품ID input 생성
		var i_goods_id = document.createElement("input");
		//상품title input 생성
		var i_goods_title = document.createElement("input");
		//상품판매 가격 input 생성
		var i_goods_sales_price = document.createElement("input");
		//파일이름 input 생성
		var i_fileName = document.createElement("input");
		//상품갯수 input 생성
		var i_order_goods_qty = document.createElement("input");
		//생성한 input에 name속성에 name부여
		i_goods_id.name = "goods_id";
		i_goods_title.name="goods_title";
		i_goods_sales_price.name="goods_sales_price";
		i_fileName.name="goods_fileName";
		i_order_goods_qty.name="order_goods_qty";
		
		//생성한 input에 값 부여
		i_goods_id.value=goods_id;
		i_order_goods_qty.value=order_goods_qty.value;
		i_goods_title.value=goods_title;
		i_goods_sales_price.value=goods_sales_price;
		i_fileName.value=goods_fileName;
		
		//form에 태크 붙이기
		formObj.appendChild(i_goods_id);
		formObj.appendChild(i_goods_title);
		formObj.appendChild(i_goods_sales_price);
		formObj.appendChild(i_fileName);
		formObj.appendChild(i_order_goods_qty);
		
		document.body.appendChild(formObj);
		formObj.method="post";
		formObj.action="${contextPath}/order/orderEachGoods.do";
		formObj.submit();
	}
</script>
</head>
<body>
	<hgroup>
		<h1>컴퓨터와 인터넷</h1>
		<h2>국내외 도서 &gt; 컴퓨터와 인터넷 &gt; 웹 개발</h2>
		<h3>${goods.goods_title}</h3>
		<h4>${goods.goods_writer } &nbsp; 저| ${goods.goods_publisher }</h4>
	</hgroup>
	<div id="goods_image">
		<figure>
			<img alt="HTML5 &amp; CSS3" src="${contextPath }/thumbnails.do?goods_id=${goods.goods_id}&fileName=${goods.goods_fileName}">
		</figure>
	</div>
	<div id="detail_table">
		<table>
			<tbody>
				<tr>
					<td class="fixed">정가</td>
					<td class="active">
						<span>
							<fmt:formatNumber value="${goods.goods_price }" type="number" var="goods_price" />
							${goods_price }원
						</span>
					</td>
				</tr>
				<tr class="dot_line">
					<td class="fixed">판매가</td>
					<td class="active">
						<span>
							<fmt:formatNumber value="${goods.goods_price*0.9 }" type="number" var="discounted_price" />
							${discounted_price}원(10%할인)
						</span>
					</td>
				</tr>			
				<tr>
					<td class="fixed">포인트적립</td>
					<td class="actice">${goods.goods_point}P(10%적립)</td>
				</tr>
				<tr class="dot_line">
					<td class="fixed">포인트 추가적립</td>
					<td class="fixed">만원이상 구매시 1,000P, 5만원이상 구매시 2,000P추가적립 편의점 배송 이용시 300P 추가적립</td>
				</tr>
				<tr>
					<td>발행일</td>
					<td>
						<c:set var="pub_date" value="${goods.goods_published_date }" />
						<c:set var="arr" value="${fn:split(pub_date,' ')}"/>
						<c:out value="${arr[0] }" />
					</td>
				</tr>
				<tr>
					<td class="fixed">페이지 수</td>
					<td class="fixed">${goods.goods_total_page }쪽</td>
				</tr>
				<tr class="dot_line">
					<td class="fixed">ISBN</td>
					<td class="fixed">${goods_goods_isbn}</td>
				</tr>
				<tr>
					<td class="fixed">배송료</td>
					<td class="fixed"><strong>무료</strong></td>
				</tr>
				<tr>
					<td class="fixed">배송안내</td>
					<td class="fixed"><strong>[당일배송]</strong>당일배송 서비스 시작!<br><strong>[휴일배송]</strong>휴일에도 배송받는 Bookshop</td>
				</tr>
				<tr>
					<td class="fixed">수량</td>
					<td class="fixed">
						<select style="width:60px;" id="order_goods_qty">
							<option>1</option>
							<option>2</option>
							<option>3</option>
							<option>4</option>
							<option>5</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
		<ul>
			<li><a class="buy" href="javascript:fn_order_each_goods('${goods.goods_id }','${goods.goods_title }','${goods.goods_sales_price }','${goods.goods_fileName }')">구매하기</a></li>
			<li><a class="cart" href="javascript:add_cart('${goods.goods_id }','${goods.goods_title }','${goods.goods_sales_price }')">장바구니</a></li>
			<li><a class="wish" href="#">위시리스트</a></li>
		</ul>
	</div>
	<div class="clear"></div>
	<!-- 내용 들어 가는 곳  -->
	<div id="container">
		<ul class="tabs">
			<li><a href="#tab1">책소개</a></li>
			<li><a href="#tab2">저자소개</a></li>
			<li><a href="#tab3">책목차</a></li>
			<li><a href="#tab4">출판사서평</a></li>
			<li><a href="#tab5">추천사</a></li>
			<li><a href="#tab6">리뷰</a></li>
		</ul>
		<div class="tab_container">
			<div class="tab_content" id="tab1">
				<h4>책소개</h4>
				<p>${fn:replace(goods.goods_intro,crcn,br)}</p>
				<c:forEach var="image" items="${imageList}">
					<img src="${contextPath }/download.do?goods_id=${goods.goods_id}&fileName=${image.fileName}">
				</c:forEach>
			</div>
			<div class="tab_content" id="tab2">
				<h4>저자소개</h4>
				<p><div class="writer">저자 : ${goods.goods_writer}</div>
				<p>${fn:replace(goods.goods_writer_intro,crcn,br) }</p>
			</div>
			<div class="tab_content" id="tab3">
				<h4>책목차</h4>
				<p>${fn:replace(goods.goods_contents_order,crcn,br)}</p> 
			</div>
			<div class="tab_content" id="tab4">
				<h4>출판사서평</h4>
				 <p>${fn:replace(goods.goods_publisher_comment ,crcn,br)}</p> 
			</div>
			<div class="tab_content" id="tab5">
				<h4>추천사</h4>
				<p>${fn:replace(goods.goods_recommendation,crcn,br) }</p>
			</div>
			<div class="tab_content" id="tab6">
				<h4>리뷰</h4>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	<div id="layer" style="visibility: hidden;">
		<div id="popup">
			<a href="javascript:" onclick="javascript:imagePopup('close','.layer01');">
				<img src="${contextPath }/resources/image/close.png" id="close">
			</a><br>
			<font size="12" id="contents">장바구니에 담았습니다.</font><br>
			<form action="${contextPath }/cart/myCartList.do">
				<input type="submit" value="장바구니 보기">
			</form>
		</div>
	</div>
</body>
</html>
<!-- 자바스크립트 변수로 사용하기 위해서 작성한듯  -->
<input type="hidden" name="isLogOn" id="isLogOn" value="${isLogOn }" />