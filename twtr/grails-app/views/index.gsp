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

            <a class="navbar-brand" href="/">TWTR: {{ message }}</a>
            <input type="checkbox" id="chkDebug" ng-model="isDebug"> Debug</input>
        </div>

        <ul class="nav navbar-nav navbar-right">
            <li ng-class="{ active: isActive('/home')}"><a href="#home">Home</a></li>
            <li ng-class="{ active: isActive('/search')}"><a href="#search">Find Messages</a></li>
            <li ng-class="{ active: isActive('/userDetail')}"><a href="#userDetail">User Details</a></li>
            %{--<li ng-class="{ active: isActive('/userDetail')}"><a href=#userDetail">My Details</a></li>--}%
        </ul>
    </div>
</nav>

<div id="debug-pane" class="container well" ng-show="isDebug">
    <h2>Debug Pane</h2>
    <div>
        Auth Token: {{ auth.token }}
    </div>
    <div>
        Auth Account: {{ auth.account }}
    </div>
    <div>
        My Page?: {{ myDetail }}
    </div>
    <div>
        Am I Following?: {{ account.amIfollowing }}
    </div>

    <a href="#userDetail?handle=luluwang">See handle: luluwang</a><br>
    <a href="#userDetail?handle=donaldsawyer">See handle: donaldsawyer</a><br>
    <a href="#userDetail?handle=mikecalvo">See handle: mikecalvo</a><br>
    <a href="#userDetail?handle=blizzard">See handle: blizzard</a><br><br><br>
</div>

<div ng-view></div>

<footer class="jumbotron text-center">
    <p>Footer Content</p>
</footer>

</body>
</html>