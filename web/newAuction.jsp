<html>
<head>
    <title>Login</title>
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/css/material-kit.css" rel="stylesheet">
    <link href="assets/css/yabe.css" rel="stylesheet">
</head>
<body>
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
            <!-- LOGIN MESSAGE HERE -->
            <ul class="nav navbar-nav navbar-right">
                <li><a href="login.jsp">Not logged in</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container centered">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">New Auction</h3>
        </div>
        <form class="panel-body">
            <div class="input-group">
                        <span class="input-group-btn">
                        <button class="btn btn-success" type="submit">Create</button>
                        </span>
                <input type="text" class="form-control" placeholder="Item Name" id="name">
            </div>
            <div class="row">

                <div class="col-md-4">
                    <h4>Condition</h4>
                    <div class="checkbox">
                        <label>
                            <input type="radio" id="new" checked>
                            New
                        </label>
                        <br/>
                        <label>
                            <input type="radio" id="used">
                            Used
                        </label>
                    </div>
                    <h4>Auction End Date</h4>
                    <input class="datepicker form-control" id="end" type="date" value="End"/>
                    <div class="form-group label-floating">
                        <label class="control-label">Starting Price</label>
                        <input type="number" class="form-control" name="price">
                    </div>
                </div>
                <div class="col-md-4">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#backpacks">Backpack</a></li>
                        <li><a data-toggle="tab" href="#tents">Tent</a></li>
                        <li><a data-toggle="tab" href="#flashlights">Flashlight</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="backpacks" class="tab-pane fade in active">
                            <div class="form-group">
                                <label for="pockets">Number of pockets:</label>
                                <select class="form-control" id="pockets">
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
                                <select class="form-control" id="material">
                                    <option>Nylon</option>
                                    <option>Canvas</option>
                                    <option>Leather</option>
                                    <option>Mixed</option>
                                </select>
                            </div>
                            <div class="checkbox">
                                <label><input name="hydration" type="checkbox" value="">Hydration</label>
                            </div>
                            <div class="checkbox">
                                <label><input name="waterproof" type="checkbox" value="">Waterproof</label>
                            </div>
                        </div>
                        <div id="tents" class="tab-pane fade">
                            <div class="form-group">
                                <div class="form-group">
                                    <label for="pockets">Color:</label>
                                    <select class="form-control" id="color">
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
                                <select class="form-control" id="capacity">
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
                                    <label><input type="checkbox" value="">Spare Parts</label>
                                </div>
                            </div>
                        </div>
                        <div id="flashlights" class="tab-pane fade">
                            <div class="form-group">
                                <label for="battery">Color:</label>
                                <select class="form-control" id="battery">
                                    <option>AA</option>
                                    <option>AAA</option>
                                    <option>C</option>
                                    <option>D</option>
                                </select>
                            </div>
                            <div class="checkbox">
                                <label><input name="rechargeable" type="checkbox" value="">Rechargeable</label>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="col-md-4">
                    <h4>Description</h4>
                    <textarea class="form-control" placeholder="A description of the product and it's condition." rows="5"></textarea>
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
</script>
</body>
</html>