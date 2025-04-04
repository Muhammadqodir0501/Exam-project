<%@ page import="uz.pdp.demo9.config.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: muham
  Date: 4/3/2025
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cabinet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("currentUser");
%>



<div class="row bg-dark text-white p-2">
    <div class="col-2 offset-10 d-flex align-items-center">
        <img src="/file/<%=currentUser.getPhotoId()%>" alt="User Photo" class="rounded-circle me-2" width="40" height="40">
        <span><%=currentUser.getFullName()%></span>
    </div>
</div>

<h1>cabinet</h1>
<form action="/logout" method="post">
    <button>logout</button>
</form>

</body>
</html>
