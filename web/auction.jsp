<%@ page import="java.util.Date" %>
<%@ page import="database.DB" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<html>
<head>
    <title>Login</title>
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
    String auctionName = "";
    String cond = "";
    String type = "";
    String a = "";
    String b = "";
    String c = "";

    String sellerName = "";
    String endDates = "";
    String highestBid = "";
    String highestBidder = "";
    String reserve = "";
    String price = "";
    String description = "";

    String message = "";
    int aID;

    if(session.getAttribute("USER") != null){
        //CAN't Access this page!
        userID = (String)session.getAttribute("USER");
        user =  "<li><a>Logged in as " + database.DB.getNameFromID(Integer.parseInt(userID)) + "</a></li>";
        user += "<li><a href ='user.jsp'>User Panel</a></li>";
        user += "<li><a href='auth.jsp'>Log Out</a></li>";

    }else{
        user = "<li><a href='login.jsp'>Not logged in</a></li>";
    }

    if(request.getParameter("newAuction") != null && request.getParameter("newAuction").equals("newAuction")){
        if(request.getParameter("category") == null ||
                request.getParameter("name") == null ||
                request.getParameter("model") == null ||
                request.getParameter("end") == null ||
                request.getParameter("price") == null){
            message = "Name, Model, End date and start price  and description required. Please try again.";
        }else {
            type = request.getParameter("category");

            //Not protected
            auctionName = request.getParameter("name");
            modelNumber = request.getParameter("model");

            endDates = request.getParameter("end");
            reserve = request.getParameter("price");
            cond = request.getParameter("condition");
            description = request.getParameter("description");

            int modelNo = Integer.parseInt(modelNumber);


            if (request.getParameter("category").equals("backpacks")) {
                a = request.getParameter("pockets");
                b = request.getParameter("material");

                if (request.getParameter("waterproof") != null) c = "true";
                else c = "false";
            } else if (request.getParameter("category").equals("tents")) {
                a = request.getParameter("color");
                b = request.getParameter("capacity");

                if (request.getParameter("spare") != null) c = "true";
                else c = "false";
            } else {
                a = request.getParameter("battery");

                if (request.getParameter("rechargeable") != null) b = "true";
                else b = "false";

                if (request.getParameter("led") != null) c = "true";
                else c = "false";
            }


            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
            Date endDate = df.parse(endDates);
            Timestamp endTime = new Timestamp(endDate.getTime());

            aID = DB.createAuction(Integer.parseInt(userID), auctionName, modelNo, type, new String[]{a, b, c}, Float.parseFloat(reserve), endTime, cond);

            if (aID != -1) {
                message = "Could not create auction. Please try again.";
            } else {
                message = "Auction successfully created!";
            }
        }
    }else{
        if(request.getParameter("auctionID") != null){
            aID = Integer.parseInt(request.getParameter("auctionID"));
        }
    }

    //DO AUCITON FETCHING HERE

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
                <% if(session.getAttribute("USER") == null){ %>
                <li><a href="login.jsp">Login</a></li>
                <li><a href="register.jsp">Register</a></li>
                <% } %>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%=user%>
            </ul>
        </div>
    </div>
</nav>
<div class="container centered">
    <h2><%=message%></h2>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><%=auctionName%> Details</h3>
        </div>
        <div class="row">
            <div class="col-md-6">
                <h3>Auction Name: <%=auctionName%></h3>
                <h3>Model Number: <%=modelNumber%></h3>
                <h3>Condition: <%=cond%></h3>
                <h4>Type: <%=type%></h4>
                <h4><%=a%></h4>
                <h4><%=b%></h4>
                <h4><%=c%></h4>
            </div>
            <div class="col-md-6">
                <h3>End Date: <%=endDates%></h3>
                <h3>Reserve: <%=reserve%></h3>
                <h4>Highest Bid: <%=highestBid%></h4>
                <h4>Highest Bidder: <%=highestBidder%></h4>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Bids</h3>
        </div>
        <div class="row">

        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Questions</h3>
        </div>

    </div>
</div>
<h3></h3>
<!--   Core JS Files   -->
<script src="assets/js/jquery.min.js" type="text/javascript"></script>
<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/js/material.min.js"></script>
<!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
<script src="assets/js/nouislider.min.js" type="text/javascript"></script>
<!--  Plugin for the Datepicker, full documentation here: http://www.eyecon.ro/bootstrap-datepicker/ -->
<script src="assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
<!--Control Center for Material Kit: activating the ripples, parallax effects, scripts from the example pages etc -->
<script src="assets/js/material-kit.js" type="text/javascript"></script>
<script>
    $('.datepicker').datepicker({
        weekStart:1
    });
</script>
</body>
</html>