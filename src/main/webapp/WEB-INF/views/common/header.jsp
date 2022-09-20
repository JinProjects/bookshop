<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script type="text/javascript">
	var loopSearch = true;
	//검색창에 입력시 이벤트 발생
	function keywordSearch(){
		if(loopSearch == false){
			return;
		}
		//form태그 name을 이용해서 검색창에 입력한 데이터를 가져온다. 
		var value = document.frmSearch.searchWord.value;
		$.ajax({
			type : "get",
			async : true,
			url : "${contextPath}/goods/keywordSearch.do",
			data : {keyword:value},
			success : function(data, textStatus){
				//서버에서 받아온 데이터를 javascript객체(json)로 변환한다
				var jsonInfo = JSON.parse(data);
				//검색결과창을 생성하는 메서드
				displayResult(jsonInfo);
			},
			error : function(data,textStatus){
				alert("에러가 발생했습니다."+data);
			},
			complete : function(data, textStatus){
				
			}
		});
	}
	//검색결과를 보여주기 위한 메서드
	function displayResult(jsonInfo){
		//자바스크립트객체의 길이
		var count = jsonInfo.keyword.length;
		//데이터가 있으면 true 없으면 false 
		if(count > 0){
			var html = '';
			//배열의 순서대로 객체를 꺼내서 a태그를 문자열로 append
			//객체 배열을 for문 변수 i에 대입하면 number로 바뀌는 듯 
			for(var i in jsonInfo.keyword){
				//a태그로 감싸고 select() 메서드를 실행시킨다.
				html += "<a href=\"javascript:select('"+jsonInfo.keyword[i]+"')\">"+jsonInfo.keyword[i]+"</a><br>";
			}
			var listView = document.getElementById("suggestList");
			listView.innerHTML = html;
			show('suggest');
		}else{
			hide('suggest');
		}
	}
	//검색목록을 클릭하면 검색창에 검색어를 입력하게하는 메서드  
	function select(selectedKeyword){
		//검색목록 클릭 시 검색창에 입력 
		document.frmSearch.searchWord.value=selectedKeyword;
		loopSearch = false;
		hide('suggest');
	}
	function show(elementId){
		var element = document.getElementById(elementId);
		console.log("element="+element);
		//null이면 false 객체가 존재하면 true
		if(element){
			element.style.display = 'block';
		}
	}
	function hide(elementId){
		var element = document.getElementById(elementId);
		if(element){
			element.style.display = 'none';
		}
	}
</script>
</head>
<body>
	<div id="logo">
		<a href="${contextPath }/main/main.do">
			<img width="176" height="80" src="${contextPath }/resources/image/Booktopia_Logo.jpg">
		</a>
	</div>
	<div id="head_link">
		<ul>
			<c:choose>
				<c:when test="${isLogOn==true and not empty memberInfo }">
					<li><a href="${contextPath }/member/logout.do">로그아웃</a></li>
					<li><a href="${contextPath }/mypage/myPageMain.do">마이페이지</a></li>
					<li><a href="#">주문배송</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${contextPath }/member/loginForm.do">로그인</a></li>
					<li><a href="${contextPath }/member/memberForm.do">회원가입</a></li>
				</c:otherwise>
			</c:choose>
			<li><a href="${contextPath }/cart/myCartList.do">장바구니</a></li>
			<li><a href="#">고객센터</a></li>
			<c:if test="${isLogOn==true and memberInfo.member_id == 'admin' }">
				<li class="no_line"><a href="${contextPath }/admin/goods/adminGoodsMain.do">관리자</a></li>			
			</c:if>
		</ul>
	</div>
	<div id="search">
		<form name="frmSearch" action="${contextPath }/goods/searchGoods.do">
			<input name="searchWord" class="main_input" type="text" onKeyUp="keywordSearch()">
			<input type="submit" name="search" class="btn1" value="검 색">
		</form>
	</div>
	<!-- 검색어 자동완성 창 -->
	<div id="suggest">
		<div id="suggestList"></div>
	</div>
</body>
</html>