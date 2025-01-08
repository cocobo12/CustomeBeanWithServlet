<%@ page import="khj.model.Member" %><%--
  Created by IntelliJ IDEA.
  User: 84913
  Date: 2025-01-03
  Time: 오후 5:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh; /* Viewport height: 100% */
            margin: 0;
        }

        form {
            display: flex;
            flex-direction: column; /* Stack elements vertically */
            justify-content: center; /* Align elements vertically */
            align-items: center; /* Align elements horizontally */
            border: 1px solid #ccc;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        button {
            margin-top: 10px;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #007BFF;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .request-data {
            margin-bottom: 20px;
            font-size: 14px;
            color: #333;
        }
    </style>
</head>
<body>
<form action="/bro/login-form" method="get">
    <div class="request-data">
        <%
            Member member = (Member) request.getAttribute("member");

            if (member != null && member.getEmail() != null) {
        %>
        Welcome, <strong><%= member.getEmail() %>
    </strong>!
        <%
        } else {
        %>
        No session data found.
        <%
            }
        %>
    </div>
    <span>home</span>
    <button type="submit">login</button>
</form>
</body>
</html>
