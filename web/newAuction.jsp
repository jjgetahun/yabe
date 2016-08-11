<html>
<head>
    <title>New Auction</title>
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
        user += "<li><a href ='user.jsp'>User Panel</a></li>";
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
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">New Auction</h3>
        </div>
        <form action="auction.jsp" method="POST" class="panel-body">
            <input type="hidden" name="newAuction" value="newAuction">
            <div class="input-group">
                  <span class="input-group-btn">
                  <button class="btn btn-success" type="submit">Create</button>
                  </span>
                <input type="text" class="form-control" placeholder="Item Name" name="name">
                <input type="text" class="form-control" placeholder="Model Number" name="model">
            </div>
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label for="condition">Condition:</label>
                        <select class="form-control" name="condition" id="condition">
                            <option>New</option>
                            <option>Used</option>
                        </select>
                    </div>
                    <h4>Auction End Date</h4>
                    <input class="datepicker form-control" name="end"/>
                    <div class="form-group label-floating">
                        <label class="control-label">Start Price</label>
                        <input type="number" class="form-control" name="price">
                    </div>
                    <div class="form-group label-floating">
                        <label class="control-label">Reserve</label>
                        <input type="number" class="form-control" name="reserve">
                    </div>
                </div>
                <div class="col-md-4">
                    <ul class="nav nav-tabs">
                        <li id="b" class="active"><a data-toggle="tab" href="#backpacks">Backpack</a></li>
                        <li id="t"><a data-toggle="tab" href="#tents">Tent</a></li>
                        <li id="f"><a data-toggle="tab" href="#flashlights">Flashlight</a></li>
                    </ul>
                    <div class="tab-content">
                        <input type="hidden" name="category" id="category" value="backpacks">
                        <div id="backpacks" class="tab-pane fade in active">
                            <div class="form-group">
                                <label for="pockets">Number of pockets:</label>
                                <select class="form-control" name="pockets" id="pockets">
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
                                <select class="form-control" name="material" id="material">
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
                                    <label for="pockets">Color:</label>
                                    <select class="form-control" name="color" id="color">
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
                                <select class="form-control" name="capacity" id="capacity">
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
                                <label for="battery">Color:</label>
                                <select class="form-control" name="battery" id="battery">
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
                <div class="col-md-4">
                    <h4>Description</h4>
                    <textarea class="form-control" name="description" placeholder="A description of the product and its condition." rows="5"></textarea>
                </div>
            </div>
        </form>
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

    $('#b').click(function(){
        $('#category').val("backpacks");
    });

    $('#t').click(function(){
        $('#category').val("tents");
    });

    $('#f').click(function(){
        $('#category').val("flashlights");
    });

</script>
</body>
</html>