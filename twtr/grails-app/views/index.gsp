<!doctype html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <asset:javascript src="application.js"/>
    <asset:stylesheet src="application.css"/>
</head>

<body ng-app="app">
<h1>Hello World</h1>

<div ng-controller="welcomeController">
    <h2>{{greeting}}</h2>
</div>

</body>
</html>