<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
	<div id="ad_main_banner">
		<ul class="bjqs">
			<li><img src="${contextPath }/resources/image/main_banner01.jpg" width="775" height="145"/></li>
			<li><img src="${contextPath }/resources/image/main_banner02.jpg" width="775" height="145"></li>
			<li><img src="${contextPath }/resources/image/main_banner03.jpg" width="775" height="145"></li>
		</ul>
	</div>
	<div class="main_book">
		<c:set var="goods_count" value="0" />
		<h3>베스트셀러</h3>
		<c:forEach var="item" items="${goodsMap.bestseller }">
			<c:set var="goods_count" value="${goods_count+1 }" />
			<div class="book">
				<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
					<img class="link" src="${contextPath }/resources/image/1px.gif">
				</a>
				<img width="121" height="154" src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
				<div class="title">${item.goods_title}</div>
				<div class="price">
					<fmt:formatNumber value="${item.goods_price }" type="number" var="goods_price" />
					${goods_price}원
				</div>
			</div>
			<c:if test="${goods_count==15}">
				<div class="book">
					<font size=20><a href="#">more</a></font>
				</div>
			</c:if>
		</c:forEach>
	</div>
	<div class="clear"></div>
	<div id="ad_sub_banner">
		<img src="${contextPath }/resources/image/sub_banner01.jpg" width="770" height="117">
	</div>
	<div class="main_book">
		<c:set var="goods_count" value="0"/>
		<h3>새로 출판된 책</h3>
		<c:forEach var="item" items="${goodsMap.newbook }">
			<c:set var="goods_count" value="${goods_count+1 }" />
			<div class="book">
				<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
					<img class="link" src="${contextPath }/resources/image/1px.gif">
				</a>
				<img width="121" height="154" 
				     src="${contextPath}/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
				<div class="title">${item.goods_title}</div>
				<div class="price">
					<fmt:formatNumber value="${item.goods_price }" type="number" var="goods_price" />
					${goods_price}원
				</div>
			</div>
			<c:if test="${goods_count == 15 }">
				<div class="book">
					<font size="20"><a href="#">more</a></font>
				</div>
			</c:if>
		</c:forEach>
	</div>
	<div class="clear"></div>
	<div>
		<img width="770" height="117" src="${contextPath }/resources/image/sub_banner02.jpg">
	</div>
	
	<div class="main_book">
		<c:set var="goods_count" value="0" />
			<h3>스테디셀러</h3>
			<c:forEach var="item" items="${goodsMap.steadyseller }">	
				<c:set var="goods_count" value="${goods_count+1}"/>
				<div class="book">
					<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
						<img src="${contextPath }/resources/image/1px.gif">
					</a>
					<img width="121" height="154" src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
					<div class="title">${item.goods_title}</div>
					<div class="price">
						<fmt:formatNumber value="${item.goods_price }" type="number" var="goods_price" />
						${goods_price}원
					</div>
				</div>
				<c:if test="${goods_count == 15 }">
					<div class="book"><a href="#">more</a></div>
				</c:if>
			</c:forEach>
	</div>
</body>
</html>