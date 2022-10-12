<html lang="en" xmlns:display="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <#include "styles/style.css">
    <#include "scripts/scripts.js">
    <title>Info!</title>
</head>
<body>
<nav class="navbar navbar-dark bg-dark customNavbar">
    <a class="navbar-brand" href="#">
        <img src="images/Logo.png" width="50" height="50" alt="myImage"/>
        TwitterPooler
    </a>
</nav>
<div class="infoContainer">
    <div class="panel panel-info">
        <h4><#if check??>${check}<#else>Your job startet, click 'Back to home' to redirect.</#if></h4>
    </div>
    <#if continueBtn??>
    <a class="btn btn-primary btnInfo btnColor" id="startAnyway" role="button" onclick="return httpGet('info/start')" >Start anyway</a>
    </#if>
    <a class="btn btn-primary btnInfo btnColor" role="button" href="home">Back to home</a>
</div>
<script type="text/javascript">
    function httpGet(theUrl)
    {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open( "GET", theUrl, true );
        xmlHttp.send( null );
        setTimeout(function() {
    window.location ="home";
    }, 2000);
        //window.location ="home";
        //return xmlHttp.responseText;
    }

</script>
</body>
</html>
