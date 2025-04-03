<%--
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
</head>
<body>
<%
    Object object = session.getAttribute("currentUser");
    if(object == null){
        response.sendRedirect("/auth/login.jsp");
        return;
    }

%>

<h1>cabinet</h1>
<form action="/logout" method="post">
    <button>logout</button>
</form>

</body>
</html>
