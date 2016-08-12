<%--
  Created by IntelliJ IDEA.
  User: arnold
  Date: 8/11/16
  Time: 2:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Alert</title>
</head>
<body>
<%
    if(session.getAttribute("USER") != null && request.getParameter("modelNumber") != "") {
        int modelNumber = Integer.parseInt(request.getParameter("modelNumber"));
        database.DB.setAlert(Integer.parseInt((String) session.getAttribute("USER")), modelNumber);
    }
    response.sendRedirect("index.jsp");
%>
</body>
</html>
