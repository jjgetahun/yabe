<%--
  Created by IntelliJ IDEA.
  User: elby
  Date: 7/20/16
  Time: 3:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/material-kit.css" rel="stylesheet">
    <link href="assets/css/yabe.css" rel="stylesheet">
</head>
<body>

<!-- LOGIN MESSAGE HERE -->
<%
    String user = "";
    String userID = "";
    String message = "";

    if(session.getAttribute("USER") != null){
        userID = (String)session.getAttribute("USER");
        user =  "<li><a>Logged in as " + database.DB.getNameFromID(Integer.parseInt(userID)) + "</a></li>";
        user += "<li><a href ='user.jsp'>User Panel</a></li>";
        user += "<li><a href='auth.jsp'>Log Out</a></li>";

    }else{
        user = "<li><a href='login.jsp'>Not logged in</a></li>";
    }

    if(request.getParameter("login") != null){
        if(request.getParameter("login").equals("true")){
            message = "Login successful";
        }else{
            message = "Login failed";
        }
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
    <h4><%=message%></h4>
    <div class="row">
        <div class="col-md-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Actions</h3>
                </div>
                <div class="panel-body">
                    <%
                        if(session.getAttribute("USER") != null){
                    %>
                    <div class="input-group">
                    <a href="newAuction.jsp">
                        <button class="btn btn-success" type="button">Create Auction</button>
                    </a>
                    </div>
                    <h5></h5>
                    <form action="alert.jsp" method="POST" class="row">
                        <div class="input-group">
                            <span class="input-group-btn">
                            <button class="btn btn-success" type="submit">Create Alert</button>
                            </span>
                            <input type="number" class="form-control" placeholder="'Model No. (ex. 1234)'" name="modelNumber">
                        </div>
                    </form>

                    <%
                        }
                    %>
                    <form action="search.jsp" method="POST" class="row">
                        <div class="input-group">
                            <input type="hidden" name="search" value="search">
                            <input type="hidden" name="searchByUser" value="searchByUser">
                            <span class="input-group-btn">
                            <button class="btn btn-success" type="submit">Search User</button>
                            </span>
                            <input type="text" class="form-control" placeholder="username" name="username">

                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-md-8">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Search - Filter by type attributes</h3>
                </div>
                <form class="panel-body" action="search.jsp" method="POST">
                    <input type="hidden" name="search" value="search">
                    <div class="input-group">
                        <span class="input-group-btn">
                        <button class="btn btn-success" type="submit">Search</button>
                        </span>
                        <input type="number" class="form-control" placeholder="Model Number" name="modelNumber">
                    </div>
                        <div class="col-md-6">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="browse" value="browse">
                                    Browse Mode (Only searches by keyword)
                                </label>
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="noCategories" value="noCategories">
                                    Exclude Type Filters
                                </label>
                            </div>
                            <label for="condition">Condition:</label>
                            <select class="form-control" name="condition" id="condition">
                                <option></option>
                                <option>New</option>
                                <option>Used</option>
                            </select>
                            <div>End Date:</div>
                                <input type="text" class="datepicker form-control" name="end">
                        </div>
                        <div class="col-md-6">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#backpacks">Backpacks</a></li>
                                <li><a data-toggle="tab" href="#tents">Tents</a></li>
                                <li><a data-toggle="tab" href="#flashlights">Flashlights</a></li>
                            </ul>
                            <div class="tab-content">
                                <input type="hidden" name="category" id="category" value="backpacks">
                                <div id="backpacks" class="tab-pane fade in active">
                                    <div class="form-group">
                                        <label for="pockets">Number of pockets:</label>
                                        <select class="form-control" id="pockets" name="pockets">
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                            <option>4</option>
                                            <option>5</option>
                                            <option>6</option>
                                            <option>7</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="material">Material:</label>
                                        <select class="form-control" id="material" name="material">
                                            <option>Nylon</option>
                                            <option>Canvas</option>
                                            <option>Leather</option>
                                            <option>Mixed</option>
                                        </select>
                                    </div>
                                    <div class="checkbox">
                                        <label><input name="waterproof" type="checkbox" value="">Waterproof</label>
                                    </div>
                                </div>
                                <div id="tents" class="tab-pane fade">
                                    <div class="form-group">
                                        <div class="form-group">
                                            <label for="color">Color:</label>
                                            <select class="form-control" id="color" name="color">
                                                <option>Red</option>
                                                <option>Blue</option>
                                                <option>Yellow</option>
                                                <option>White</option>
                                                <option>Black</option>
                                                <option>Green</option>
                                                <option>Purple</option>
                                                <option>Orange</option>
                                            </select>
                                        </div>
                                        <label for="capacity">Capacity</label>
                                        <select class="form-control" id="capacity" name="capacity">
                                            <option>1</option>
                                            <option>2</option>
                                            <option>3</option>
                                            <option>4</option>
                                            <option>5</option>
                                            <option>6</option>
                                            <option>7</option>
                                            <option>8</option>
                                            <option>9</option>
                                            <option>10</option>
                                        </select>
                                        <div class="checkbox">
                                            <label><input type="checkbox" name="spare" value="">Spare Parts</label>
                                        </div>
                                    </div>
                                </div>
                                <div id="flashlights" class="tab-pane fade">
                                    <div class="form-group">
                                        <label for="battery">Battery:</label>
                                        <select class="form-control" id="battery" name="battery">
                                            <option>AA</option>
                                            <option>AAA</option>
                                            <option>C</option>
                                            <option>D</option>
                                        </select>
                                    </div>
                                    <div class="checkbox">
                                        <label><input name="rechargeable" type="checkbox" value="">Rechargeable</label>
                                    </div>
                                    <div class="checkbox">
                                        <label><input name="led" type="checkbox" value="">LED</label>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </form>
            </div>
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

    $('#backpacks').click(function(){
        $('#category').val("backpacks");
    });

    $('#tents').click(function(){
        $('#category').val("tents");
    });

    $('#flashlights').click(function(){
        $('#category').val("flashlights");
    });


</script>
</body>
</html>
