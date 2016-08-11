<%@ page import="java.sql.*" %>
<%@ page import="database.DB" %>
<%@ page import="database.Auction" %>

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



    String modelNumber = "";
    String cond = "";
    String type = "";
    String a = "";
    String b = "";
    String c = "";

    String sellerName = "";
    String endDate = "";
    String highestBid = "";
    String highestBidder = "";
    String reserve = "";
    String price = "";


    ResultSet rs = null;
    String message = "";

    if(session.getAttribute("USER") != null){
        userID = (String)session.getAttribute("USER");
        user =  "<li><a>Logged in as " + database.DB.getNameFromID(Integer.parseInt(userID)) + "</a></li>";
        user += "<li><a href='user.jsp'>User Panel</a></li>";
        user += "<li><a href='auth.jsp'>Log Out</a></li>";

    }else{
        user = "<li><a href='login.jsp'>Not logged in</a></li>";
    }

    if(request.getParameter("search") != null){
        if(request.getParameter("modelNumber") != null) {
            modelNumber = request.getParameter("modelNumber");
        }
        if(request.getParameter("browse") != null){
            System.out.println("Default keyword: " + modelNumber);
            rs = DB.searchAuction(modelNumber, null, null, null, true, modelNumber);
        }
        }else{
            System.out.println("No browse");

            if (request.getParameter("category") == null ||
                    request.getParameter("condition") == null ||
                    request.getParameter("end") == null) {
                        message = "Model, End date and start price required. Please try again.";
            }else{

                rs = DB.searchAuction(, type, null, null, true, modelNumber);
            }
        }

    }
//    if(request.getParameter(""))

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
            <ul class="nav navbar-nav">
                <li><a href="index.jsp">Home</a></li>
                <li><a href="login.jsp">Login</a></li>
                <li><a href="register.jsp">Register</a></li>
            </ul>

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
                    <h3 class="panel-title">Search Results</h3>
                </div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Model Number</th>
                                <th>Seller</th>
                                <th>Current Bid</th>
                                <th>End Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            if(rs != null){
                                while(rs.next()){
                                    int aid = rs.getInt("ItemID");
                                    Auction auction = DB.getAuction(aid);
                                    String bid = "No bids placed";
                                    if(auction.getBidList().size() > 0){
                                        bid = "" + auction.getBidList().get(0).amount;
                                    }
                        %>
                        <tr>
                            <td> <%=auction.name%></td>
                            <td> <%=auction.itemID%> </td>
                            <td> <%=DB.getNameFromID(auction.sellerID)%> </td>
                            <td> <%=bid%> </td>
                            <td> <%=auction.endTime.toString()%> </td>
                            <td> <form action="auction.jsp" method="POST">
                                    <input type="hidden" name="auctionID" value="<%=aid%>">
                                    <button class="btn btn-success" type="submit">View</button>
                                 </form>
                            </td>
                            <td> <button class="btn btn-success" type="button">Answer</button> </td>
                        </tr>
                        <%
                                }
                            }
                        %>

                        </tbody>
                        <!--Put stuff in here-->
                    </table>
                </div>

            </div>
        </div>
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