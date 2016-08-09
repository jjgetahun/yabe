<%--
  Created by IntelliJ IDEA.
  User: elby
  Date: 8/5/16
  Time: 7:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
</head>
<body>
<%
    if(session.getAttribute("USER") != null){
        session.setAttribute("USER", null);
        response.sendRedirect("index.jsp");
        return;
    }else if(request.getParameter("register") != null && request.getParameter("register").equals("true")) {
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (username.length() == 0 || name.length() == 0 || password.length() == 0) {
            //need a failed register page
            response.sendRedirect("register.jsp");
            return;
        }
        if (database.DB.insertUser(username, name, password))
            request.setAttribute("register", "true");
        else
            request.setAttribute("register", "false");

        response.sendRedirect("login.jsp");
        return;
    }else if(request.getParameter("login") != null && request.getParameter("login").equals("true")){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int uid = database.DB.validateLogin(username, password);
        if(uid != -1){
            session.setAttribute("USER", Integer.toString(uid));
        }
        //we need a failed login page
        response.sendRedirect("index.jsp");
        return;
    }

%>
<%--<%--%>
    <%--if(session.getAttribute("USER") != null &&--%>
            <%--request.getParameter("logout") != null &&--%>
            <%--request.getParameter("logout").equals("true")) {--%>
        <%--message = "Logout Successful.";--%>
        <%--session.setAttribute("USER", null);--%>
    <%--}else if( request.getParameter("register") != null && request.getParameter("register").equals("true")) {--%>
        <%--String username = request.getParameter("username");--%>
        <%--if (database.DB.validateLogin(username) == -1){--%>
            <%--String name = request.getParameter("name");--%>
            <%--String password = request.getParameter("password");--%>
            <%--database.DB.insertUser(username, name, password);--%>
            <%--message = "Registration successful";--%>
        <%--}--%>
        <%--else message = "User already exists";--%>
    <%--}--%>
<%--%>--%>

</body>
</html>
