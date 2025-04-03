<%--
  Created by IntelliJ IDEA.
  User: muham
  Date: 4/3/2025
  Time: 4:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Object object = session.getAttribute("currentUser");
    if(object == null){
        response.sendRedirect("/auth/login.jsp");
        return;
    }

%>

<h1>admin</h1>
</body>
</html>
