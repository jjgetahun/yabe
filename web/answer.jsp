<%--
  Created by IntelliJ IDEA.
  User: arnold
  Date: 8/11/16
  Time: 4:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Answer</title>
</head>
<body>
<%
    if(session.getAttribute("USER") != null) {
        String contents = request.getParameter("answer");
        String qid = (String)request.getParameter("qid");
        int posterID = Integer.parseInt((String)session.getAttribute("USER"));
        System.out.println(qid);
        database.DB.answerQuestion(Integer.parseInt(qid), posterID, contents);
        response.sendRedirect ("index.jsp");
        return;
    }
%>
</body>
</html>
