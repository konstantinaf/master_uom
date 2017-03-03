<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="images/favicon.png">
    <title>Jira. Get your Jira Bugs!</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
    <script type="text/javascript"	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/home.js"></script>
</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Jira Reporter</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#">Page 1</a></li>
            <li><a href="#">Page 2</a></li>
        </ul>
    </div>
</nav>
<div class="container-fluid">
    <div class="row">
        <div class="jumbotron">
            <h1><label for="link_input">Enter your JIRA link:</label></h1>
            <p>
            <div class="form-group">
                <input type="text" class="form-control" id="link_input" placeholder="Your link"><br/>
            </div>
            </p>
            <p id="urlerrorcontainer"></p>
            <p><button type="button" id="getbugs_btn" data-loading-text="Loading..." class="btn btn-primary btn-lg" >Get Bug Reports!</button></p>
            <p id="servermessagescontainer"></p>
        </div>
    </div>
</div>

</body>
</html>