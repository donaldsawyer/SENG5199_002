<!doctype html>
<html ng-app="app">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <asset:javascript src="application.js"/>
    <asset:stylesheet src="application.css"/>
</head>

<body ng-controller="headerController">
<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">TWTR: {{ message2 }}</a>
        </div>

        <ul class="nav navbar-nav navbar-right">
            <li ng-class="{ active: isActive('/home')}"><a href="#home">Home</a></li>
            <li ng-class="{ active: isActive('/search')}"><a href="#search">Find Messages</a></li>
            <li ng-class="{ active: isActive('/userDetail')}"><a href="#userDetail">User Details</a></li>
            %{--<li ng-class="{ active: isActive('/userDetail')}"><a href=#userDetail">My Details</a></li>--}%
        </ul>
    </div>
</nav>
<a href="#userDetail?handle=luluwang">See handle: luluwang</a><br>
<a href="#userDetail?handle=donaldsawyer">See handle: donaldsawyer</a><br>
<a href="#userDetail?handle=mikecalvo">See handle: mikecalvo</a><br>
<a href="#userDetail?handle=blizzard">See handle: blizzard</a><br><br><br>
<div ng-view></div>

<footer class="jumbotron text-center">
    <p>Footer Content</p>
</footer>

</body>
</html>