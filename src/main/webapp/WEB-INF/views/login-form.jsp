<%--
  Created by IntelliJ IDEA.
  User: 84913
  Date: 2025-01-03
  Time: 오후 5:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LoginPage</title>
</head>
<body>
<form action="/bro/login" method="post">
    id: <input type="text" name="id"/>
    pw: <input type="password" name="pw">
    <button type="submit">로그인</button>
</form>
<br>
<!-- 회원가입 버튼 -->
<form action="/bro/join-form" method="get">
    <button type="submit">회원가입</button>
</form>
</body>
</html>
