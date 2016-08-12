<%--
  Created by IntelliJ IDEA.
  User: Jon
  Date: 8/12/16
  Time: 1:14 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.sql.*" %>
<%@ page import="javax.xml.transform.Result" %>

<html>
<head>
    <title>Earnings per End-User Report Page</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/material-kit.css" rel="stylesheet">
    <link href="assets/css/yabe.css" rel="stylesheet">
</head>
<body>
<!-- LOGIN MESSAGE HERE -->
    <%
    String user = "";
    String userID = "";
    if(session.getAttribute("USER") != null){
        userID = (String)session.getAttribute("USER");
        user =  "<li><a>Logged in as " + database.DB.getNameFromID(Integer.parseInt(userID)) + "</a></li>";
        user += "<li><a href='user.jsp'>User Panel</a></li>";
        user += "<li><a href='auth.jsp'>Log Out</a></li>";

    }else{
        user = "<li><a href='login.jsp'>Not logged in</a></li>";
    }

%>

<nav class="navbar navbar-success" role="navigation">
    <div class="container">
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
            <ul class="nav navbar-nav navbar-right">
                <%=user%>
            </ul>
        </div>
    </div>
</nav>

<div class="container centered">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Earnings per End-User</h3>
                </div>
                <div class="panel-body">

                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Seller ID</th>
                            <th>Username</th>
                            <th>Earnings</th>
                        </tr>
                        </thead>
                        <%
                            ResultSet rs = database.DB.getEarningsPerEndUserReport();
                            while (rs != null && rs.next()) {
                        %>
                        <tbody>
                        <td> <%=rs.getInt("SellerID")%></td>
                        <td> <%=database.DB.getNameFromID(rs.getInt("SellerID"))%></td>
                        <td> <%=rs.getFloat("Amount")%> </td>
                        </tbody>
                        <%
                            }
                        %>

                        <!--Put stuff in here-->
                    </table>

                </div>
            </div>
        </div>
    </div>
</div>