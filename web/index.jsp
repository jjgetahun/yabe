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
    <title>$Title$</title>
      <link href="assets/css/bootstrap.min.css" rel="stylesheet">
      <link href="assets/css/material-kit.css" rel="stylesheet">
      <link href="assets/css/yabe.css" rel="stylesheet">
  </head>
  <body>
  <nav class="navbar navbar-success" role="navigation">
      <div class="container-fluid">
          <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#">YABE</a>
          </div>

          <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul class="nav navbar-nav">
                  <li class="active"><a href="#">Login</a></li>
              </ul>
          </div>
      </div>
  </nav>
  <form action="/login" method="POST">
      <div class="container centered">
          <form action="/login" method="POST" class="row">
              <div class="col-md-3 col-md-offset-4">
                  <div class="form-group label-floating">
                      <label class="control-label">Username</label>
                      <input name="username" type="text" class="form-control">
                  </div>
              </div>
              <div class="col-md-3 col-md-offset-4">
                  <div class="form-group label-floating">
                      <label class="control-label">Password</label>
                      <input name="password" type="password" class="form-control">
                  </div>
              </div>
              <div class="col-md-3 col-md-offset-4">
                  <div class="form-group label-floating">
                      <input class="btn btn-success" type="submit" value="Submit" />
                  </div>
              </div>
          </div>
      </div>
  </form>
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