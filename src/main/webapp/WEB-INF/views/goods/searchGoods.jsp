<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<%
	pageContext.setAttribute("crcn", "\r\n");
	pageContext.setAttribute("br", "<br>");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
	<hgroup>
		<h1>컴퓨터와 인터넷</h1>
		<h2>오늘의 책</h2>
	</hgroup>
		<!-- 슬라이드  -->
	<section id="new_book">
		<h3>새로나온 책</h3>
		<!-- 슬라이드 왼쪽 스크롤 이벤트 이미지  -->
		<div id="left_scroll">
			<a href='javascript:slide("left");'><img src="${contextPath }/resources/image/left.gif"></a>
		</div>
		<div id="carousel_inner">
			<ul id="carousel_ul">
			<c:choose>
				<c:when test="${empty goodsList }">
					<li><!-- block을 inline으로 바꾸는 듯  -->
						<div id="book">
							<a><h1>제품이없습니다.</h1></a>
						</div>
					</li>
				</c:when>
				<c:otherwise>
					<c:forEach var="item" items="${goodsList}">
						<li>
							<div id="book">
								<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
									<img width="75" src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
								</a>
								<div class="sort">[컴퓨터 인터넷]</div>
								<div class="title">
									<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
										${item.goods_title}
									</a>
								</div>
								<div class="writer">${item.goods_writer } | ${item.goods_publisher }</div>
								<div class="price">
									<span>
										<fmt:formatNumber value="${item.goods_price }" type="number" var="goods_price" />
										${goods_price}원
									</span><br>
										<fmt:formatNumber value="${item.goods_price*0.9}" type="number" var="discounted_price" />
										${discounted_price }원(10%할인)
								</div>
							</div>
						</li>
					</c:forEach>
				<li>
				</li>
				</c:otherwise>
			</c:choose>
			</ul>
		</div>
		<div id="right_scroll">
			<a href='javascript:slide("right");'><img src="${contextPath }/resources/image/right.gif"></a>
		</div>
		<input id="hidden_auto_slide_seconds" type="hidden" value="0">
		<div class="clear"></div>
	</section>
	<div class="clear"></div>
	<div id="sorting">
		<ul>
			<li><a class="active" href="#">베스트 셀러</a></li>
			<li><a href="#">최신 출간</a></li>
			<li><a style="border: currentColor; border-image: none;" href="#">최근 등록</a></li>
		</ul>
	</div>
	<table id="list_view">
		<tbody>
			<c:forEach var="item" items="${goodsList}">
				<tr>
					<td class="goods_image">
						<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">
							<img width="75" src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
						</a>
					</td>
					<td class="goods_description">
						<h2>
							<a href="${contextPath }/goods/goodsDetail.do?goods_id=${item.goods_id}">${item.goods_title }</a>
						</h2>
						<c:set var="goods_pub_date" value="${item.goods_published_date }" />
						<c:set var="arr" value="${fn:split(goods_pub_date,' ') }" />
						<div class="writer_press">${item.goods_writer }저
							|${item.goods_publisher }|<c:out value="${arr[0]}" />
						</div>
					</td>
					<td class="price">
						<span>${item.goods_price }원</span><br>
						<strong>
							<fmt:formatNumber value="${item.goods_price*0.9 }" type="number" var="discounted_price" />
								${discounted_price }원
						</strong><br>(10% 할인)
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>