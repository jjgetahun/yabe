<%--
  Created by IntelliJ IDEA.
  User: elby
  Date: 7/21/16
  Time: 7:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
    import="database.*"
%>
<html>
<title>User Panel</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/material-kit.css" rel="stylesheet">
    <link href="assets/css/yabe.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-success" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.jsp">YABE</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="index.jsp">Login</a></li>
                <li><a href="register.jsp">Register</a></li>
            </ul>
        </div>
    </div>
</nav>
    <%
        System.out.println(session.getAttribute("USER"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        int id = database.DB.getUserID(username, password);
        String message = "";
        String button = "";
        if(id != -1){
            message = "Login Successful! Welcome to YABE, " + id;
            session.setAttribute("USER", id);
            button = "    <form action=\"index.jsp\" method=\"POST\">\n" +
                    "        <input type=\"hidden\" name=\"logout\" value=\"true\">\n" +
                    "        <button type=\"submit\" class=\"btn\">Logout</button><br>\n" +
                    "    </form>";
        }else{
            message = "Login unsuccessful. Please return to the login.";
        }
    %>

    <h1><%=message%></h1>
    <%=button%>


</body>
</html>
