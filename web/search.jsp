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
    String type = "";


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
        if(request.getParameter("modelNumber") != null)
            modelNumber = request.getParameter("modelNumber");
        else
            modelNumber = null;

        if(request.getParameter("browse") != null){
            System.out.println("Default keyword: " + modelNumber);
            rs = DB.searchAuction(modelNumber, null, null, null, true, modelNumber);
        }
        else if(request.getParameter("searchByUser") != null){
            rs = DB.getAuctionsParticipatedIn(DB.getUserID(request.getParameter("username")));
        }
        else{
            if (request.getParameter("category") == null ||
                    request.getParameter("condition") == null ||
                    request.getParameter("end") == null) {
                        message = "End date and start price required. Please try again.";
            }else{

                String[] attr = new String[3];

                if (request.getParameter("category").equals("backpacks")) {
                    attr[0] = request.getParameter("pockets");
                    attr[1] = request.getParameter("material");

                    if (request.getParameter("waterproof") != null) attr[2] = "true";
                    else attr[2] = "false";
                } else if (request.getParameter("category").equals("tents")) {
                    attr[0] = request.getParameter("color");
                    attr[1] = request.getParameter("capacity");

                    if (request.getParameter("spare") != null) attr[2] = "true";
                    else attr[2] = "false";
                } else {
                    attr[0] = request.getParameter("battery");

                    if (request.getParameter("rechargeable") != null) attr[1] = "true";
                    else attr[1] = "false";

                    if (request.getParameter("led") != null) attr[2] = "true";
                    else attr[2] = "false";
                }
                String date = request.getParameter("end");

                rs = DB.searchAuction(modelNumber, request.getParameter("category"), attr, date, false, request.getParameter("condition"));
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

                                    int aid = rs.getInt("AuctionID");
                                    Auction auction = DB.getAuction(aid);
                                    String bid = "No bids placed";
                                    if(auction.getBidList().size() > 0){
                                        bid = "" + auction.getBidList().get(0).amount;
                                    }

                                    String time = "bad";
                                    if(auction == null) time =  "oops";
                                    if(auction.endTime != null) time = auction.endTime.toString();

                        %>
                        <tr>
                            <td> <%=auction.name%></td>
                            <td> <%=auction.itemID%> </td>
                            <td> <%=DB.getNameFromID(auction.sellerID)%> </td>
                            <td> <%=bid%> </td>
                            <td> <%=time%> </td>
                            <td> <form action="auction.jsp" method="POST">
                                    <input type="hidden" name="auctionID" value="<%=aid%>">
                                    <button class="btn btn-success" type="submit">View</button>
                                 </form>
                            </td>
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