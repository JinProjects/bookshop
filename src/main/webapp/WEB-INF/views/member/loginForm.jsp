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
<!-- 로그인시 아이디와 비밀번호가 다르면 서버에서 메시지를 보내고
	 맞으면 실행하지 않는다.
	 그래서 처음 로그인창에서는 message가 null이기때문에 실행안되고
	 만약 틀렸을 시에 서버에서 message를 보내서 실행하게 한다.
 -->
<c:if test="${not empty message }">
<script type="text/javascript">
/* window.onload의미  
 *
 */
window.onload=function(){
	result();
}
function result(){
	alert("아이디나 비밀번호가 틀립니다. 다시 로그인해주세요.");
}
</script>
</c:if>
<title></title>
</head>
<body>
	<h3>회원 로그인 창</h3>
	<div id="detail_table">
		<form action="${contextPath }/member/login.do" method="post">
			<table>
				<tbody>
					<tr class="dot_line">
						<td class="fixed_join">아이디</td>
						<td><input type="text" size="20" name="member_id"></td>
					</tr>
					<tr class="solid_line">
						<td class="fixed_join">비밀번호</td>
						<td><input type="password" size="20" name="member_pw"></td>
					</tr>
				</tbody>
			</table>
			<br><br>
			<input type="submit" value="로그인">
			<input type="button" value="초기화">
			<br><br>
			<a href="#">아이디 찾기</a>
			<a href="#">비밀번호 찾기</a>
			<a href="${contextPath }/member/addMember.do">회원가입</a>
			<a href="#">고객 센터</a>
		</form>
	</div>
</body>
</html>