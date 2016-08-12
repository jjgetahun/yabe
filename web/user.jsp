<%@ page import="java.sql.*" %>

<html>
<head>
    <title>User Page</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/material-kit.css" rel="stylesheet">
    <link href="assets/css/yabe.css" rel="stylesheet">
</head>
<body>
<!-- LOGIN MESSAGE HERE -->
<%
    String user = "";
    String userID = "";
    ResultSet rs = null;
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
                    <h3 class="panel-title">User Messages</h3>
                </div>
                <div class="panel-body">

                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>MessageID</th>
                            <th>Contents</th>
                            <th>Time Sent</th>
                        </tr>
                        </thead>
                        <tbody>

                        <%
                            rs = database.DB.getMessages(Integer.parseInt(userID));
                            while (rs.next()) {
                        %>
                        <tr>
                            <td> <%=rs.getInt("MessageID")%></td>
                            <td> <%=rs.getString("Contents")%> </td>
                            <td> <%=rs.getDate("TimeSent")%> </td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>

                        <!--Put stuff in here-->
                    </table>


                </div>
            </div>
        </div>
        <%

            if(database.DB.isAdmin(Integer.parseInt(userID))){
        %>
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Admin Panel</h3>
                </div>
                <div class="panel-body">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Create User</h3>
                            </div>
                            <div class="panel-body">
                                <!--Create user form-->
                                <form action="auth.jsp" method="POST" class="row">
                                    <div class="col-md-6">
                                        <div class="form-group label-floating">
                                            <label class="control-label">Username</label>
                                            <input name="username" type="text" class="form-control">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group label-floating">
                                            <label class="control-label">Full Name</label>
                                            <input name="name" type="text" class="form-control">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group label-floating">
                                            <label class="control-label">Password</label>
                                            <input name="password" type="password" class="form-control">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="admin">
                                                Admin
                                            </label>
                                            <br/>
                                            <label>
                                                <input type="checkbox" name="rep">
                                                Customer Rep.
                                            </label>
                                        </div>
                                    </div>
                                    <input type="hidden" name="admin_reg" value="true"/>
                                    <div class="col-md-6 ">
                                        <div class="form-group label-floating">
                                            <input class="btn btn-success" type="submit" value="Submit" />
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Reports</h3>
                            </div>
                            <div class="panel-body">
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="totalEarnings.jsp">
                                            <button class="btn btn-success" type="submit">Total Earnings</button>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="earningsPerItem.jsp">
                                            <button class="btn btn-success" type="submit">Earnings per Item</button>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="earningsPerItemType.jsp">
                                            <button class="btn btn-success" type="submit">Earnings per Item Type</button>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="earningsPerEndUser.jsp">
                                            <button class="btn btn-success" type="submit">Earnings per End-User</button>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="bestSellingItems.jsp">
                                            <button class="btn btn-success" type="submit">Best-Selling Items</button>
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-6 ">
                                    <div class="input-group">
                                        <a href="bestBuyers.jsp">
                                            <button class="btn btn-success" type="submit">Best Buyers</button>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </div>
<%
    } if (database.DB.isCustomerRep(Integer.parseInt(userID))){

%>
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Customer Rep. Panel</h3>
                </div>
                <div class="panel-body">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title">Questions unanswered</h3>
                        </div>
                        <div class="panel-body">
                            <!--Div wrap and overflow auto for scroll-->

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Auction</th>
                                    <th>User</th>
                                    <th>Header</th>
                                    <th>Contents</th>
                                    <th>Time Posted</th>
                                    <th>Action</th>
                                </tr>
                                </thead>
                                <%
                                    rs = database.DB.getAllQuestions();
                                    while (rs.next()) {
                                %>
                                <tbody>
                                <tr>
                                    <td> <%=rs.getInt("QuestionID")%></td>
                                    <td> <%=rs.getInt("AuctionID")%> </td>
                                    <td> <%=rs.getInt("PosterID")%> </td>
                                    <td> <%=rs.getString("Header")%> </td>
                                    <td> <%=rs.getString("Contents")%> </td>
                                    <td> <%=rs.getString("TimePosted")%> </td>
                                    <td>
                                        <form action="answer.jsp" method="POST" class="row">
                                            <div class="input-group">
                                                <input type="hidden" name="qid" value="<%=rs.getInt("QuestionID")%>">
                                                <input type="text" class="form-control" placeholder="'Answer'" name="answer">
                                                <span class="input-group-btn">
                                                    <button class="btn btn-success" type="submit">Answer</button>
                                                </span>
                                            </div>
                                        </form>
                                    </td>
                                </tr>
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

        <%

    }

        %>
    </div>
</div>
<script>
    $('.datepicker').datepicker({
        weekStart:1
    });
</script>
<h3></h3>
<!--   Core JS Files   -->
<script src="assets/js/jquery.min.js" type="text/javascript"></script>
<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/js/material.min.js"></script>
<!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
<script src="assets/js/nouislider.min.js" type="text/javascript"></script>
<!--  Plugin for the Datepicker, full documentation here: http://www.eyecon.ro/bootstrap-datepicker/ -->
<script src="assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
<!-- Control Center for Material Kit: activating the ripples, parallax effects, scripts from the example pages etc -->
<script src="assets/js/material-kit.js" type="text/javascript"></script>
</body>
</html>