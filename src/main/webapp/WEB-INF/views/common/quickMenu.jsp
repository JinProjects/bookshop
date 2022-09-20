<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript">
	var array_index=0;
	var SERVER_URL="${contextPath}/thumbnails.do";
	//최근 본 상품 중 전 상품을 가져온다.
	function fn_show_next_goods(){
		//이미지 태그를 가져온다.
		var img_sticky=document.getElementById("img_sticky");
		console.log("img_sticky:"+img_sticky);
		//현재 상품의 number를 가져온다.
		var cur_goods_num = document.getElementById("cur_goods_num");
		//배열로 값을 가져온다.
		var _h_goods_id = document.frm_sticky.h_goods_id;
		//배열로 값을 가져온다.
		var _h_goods_fileName = document.frm_sticky.h_goods_fileName;
		console.log("_h_goods_id.length="+_h_goods_id.length);
		//_h_goods_id.length = 변수의 길이가 아니라 배열의 길이다.
		//array_index의 초기값이 0인데
		//_h_goods_id.length보다 작으면 array_index를 1증가 시킨다.		
		if(array_index < _h_goods_id.length-1){
			array_index++;
		}
		var goods_id = _h_goods_id[array_index].value;
		var fileName = _h_goods_fileName[array_index].value;
		//다음 썸네일파일을 받아서 img주소에 저장
		img_sticky.src=SERVER_URL+"?goods_id="+goods_id+"&fileName="+fileName;
		//cur_goods_num의 값을 변경한다
		cur_goods_num.innerHTML=array_index+1;
	}
	//최근 본 상품목록 중 가져온다. 
	function fn_show_previous_goods(){
		//img_sticky 객체를 가져온다.
		var img_sticky=document.getElementById("img_sticky");
		//cur_goods_num 객체를 가져온다.
		var cur_goods_num=document.getElementById("cur_goods_num");
		//배열로 값을 가져옴
		var _h_goods_id=document.frm_sticky.h_goods_id;
		//배열로 값을 가져옴
		var _h_goods_fileName=document.frm_sticky.h_goods_fileName;
		//이전 목록을 가져오는 거라서 
		//현재 index에서 1을 빼야한다.
		if(array_index>0){
			array_index--;
		}
		console.log("array_index="+array_index);
		var goods_id=_h_goods_id[array_index].value;
		var fileName=_h_goods_fileName[array_index].value;
		img_sticky.src=SERVER_URL+"?goods_id="+goods_id+"&fileName="+fileName;
		//array_index는 0부터 시작이라서 1을 더해주어야 한다.
		cur_goods_num.innerHTML=array_index+1;
	}
	//클릭하면 상세페이지로 넘어간다.
	function goodsDetail(){
		//퀵메뉴 상품 pagenum 숫자
		var cur_goods_num = document.getElementById("cur_goods_num");
		//아래 h_goods_id는 배열로 저장되어있기 때문에 index를 얻어와야한다.
		//그래서 cur_goods_num의 pageNum을 이용해서 index를 사용한다.
		//배열을 0~3까지 저장하기 때문에 pageNum의 첫 숫자는 1이라서 -1을 빼준다.
		//그럼 처음 등록 된 상품이 담긴 배열 index는 0
		var arrIdx = cur_goods_num.innerHTML-1;
		//상품 id를 가져온다.
		//foreach가 루프를 돈 만큼 내부에서 배열로 저장되어 리턴한다.
		var h_goods_id = document.frm_sticky.h_goods_id;
		//배열의 길이를 얻는다
		var len = h_goods_id.length;
		//배열이면 true 아니면 false;
		//이유 : h_goods_id는 foreach문으로 반복되서 리턴되기 때문에
		//foreach가 한번만 돌면 배열이 되서 리턴되지 않는다.
		//배열이면 true 변수면 false
		if(len>1){
			goods_id=h_goods_id[arrIdx].value;
		}else{
			goods_id=h_goods_id.value;
		}
		
		var formObj = document.createElement("form");
		var i_goods_id = document.createElement("input");
		
		i_goods_id.name = "goods_id";
		//현재 클릭한 상품의 id를 저장한다.
		i_goods_id.value= goods_id;
		
		formObj.appendChild(i_goods_id);
		document.body.appendChild(formObj);
		formObj.method='get';
		formObj.action="${contextPath}/goods/goodsDetail.do?goods_id="+goods_id;
		formObj.submit();
	}
</script>
</head>
<body>
	<div id="sticky">
		<ul>
			<li>
				<a href="#">
					<img width="24" height="24" src="${contextPath }/resources/image/facebook_icon.png">페이스북
				</a>
			</li>
			<li>
				<a href="#">
					<img width="24" height="24" src="${contextPath }/resources/image/twitter_icon.png">트위터
				</a>
			</li>
			<li>
				<a>
					<img width="24" height="24" src="${contextPath }/resources/image/rss_icon.png">RSS 피드
				</a>
			</li>
		</ul>
		<div class="recent">
			<h3>최근 본 상품</h3>
			<ul>
				<c:choose>
					<c:when test="${empty quickGoodsList }">
						<strong>상품이 없습니다.</strong>
					</c:when>
					<c:otherwise>
					<!-- form을 사용해서 javascript에서 값을 가지고 온다.
						 form을 사용하지말고 값을 가지고 와보자
					 -->
						<form name="frm_sticky">
							<c:forEach var="item" items="${quickGoodsList }" varStatus="itemNum">
								<c:choose>
									<c:when test="${itemNum.count==1 }">
										<a href="javascript:goodsDetail();">
											<img width="75" height="95" id="img_sticky"
												src="${contextPath }/thumbnails.do?goods_id=${item.goods_id}&fileName=${item.goods_fileName}">
										</a>
										<input type="hidden" name="h_goods_id" value="${item.goods_id }">
										<input type="hidden" name="h_goods_fileName" value="${item.goods_fileName }">
										<br>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="h_goods_id" value="${item.goods_id }">
										<input type="hidden" name="h_goods_fileName" value="${item.goods_fileName }">
									</c:otherwise>
								</c:choose>
							</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
		</form>
		</div>
		<div>
			<c:choose>
				<c:when test="${empty quickGoodsList }">
					<h5> &nbsp; &nbsp; &nbsp; &nbsp; 0/0 &nbsp; </h5>
				</c:when>
				<c:otherwise>
					<h5><a href='javascript:fn_show_previous_goods();'>이전</a>&nbsp;<span id="cur_goods_num">1</span>/${quickGoodsListNum} &nbsp; <a href="javascript:fn_show_next_goods();">다음</a></h5>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>