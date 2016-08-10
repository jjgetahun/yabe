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
                                                <input type="checkbox" name="admin" checked>
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
                                <!--Div wrap and overflow auto for scroll-->
                                <h5>Total Earnings: </h5>
                                <h5>Earnings per item</h5>
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Item</th>
                                        <th>Earnings</th>

                                    </tr>
                                    </thead>
                                    <tbody></tbody>

                                    <!--Put stuff in here-->
                                </table>

                                <h5>Earnings per category: </h5>
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Category</th>
                                        <th>Earnings</th>

                                    </tr>
                                    </thead>
                                    <tbody></tbody>

                                    <!--Put stuff in here-->
                                </table>

                                <h5>Earnings per user: </h5>
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>User</th>
                                        <th>Earnings</th>

                                    </tr>
                                    </thead>
                                    <tbody></tbody>

                                    <!--Put stuff in here-->
                                </table>
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
                            <%
                                ResultSet rs = database.DB.getAllQuestions();
                                while (rs.next()) {
                            %>
                            <!--Div wrap and overflow auto for scroll-->

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Auction</th>
                                    <th>User</th>
                                    <th>Header</th>
                                    <th>Contents</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td> <%=rs.getInt("QuestionID")%></td>
                                    <td> <%=rs.getInt("AuctionID")%> </td>
                                    <td> <%=rs.getInt("PosterID")%> </td>
                                    <td> <%=rs.getString("Header")%> </td>
                                    <td> <%=rs.getString("Contents")%> </td>
                                </tr>
                                </tbody>
                                <!--Put stuff in here-->
                            </table>
                            <%
                                }
                            %>
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