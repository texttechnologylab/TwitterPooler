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
    <title>Query count!</title>
</head>
<body>
<form action="info" method="get">
    <nav class="navbar navbar-dark bg-dark customNavbar">
        <a class="navbar-brand" href="#">
            <img src="Logo.png" width="50" height="50" alt="myImage"/>
            TwitterPooler
        </a>
    </nav>
<div class="container customCount">
    <div class="panel panel-info">
        <h2><#if tweetCount??>You want to download ${tweetCount} org.texttechnologylab.twitterpooler.tweets. Please
            select
            account which you want to use.<#else>Please select account which you want to use.</#if></h2>
    </div>
</div>

<div class="container customCount">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h5>Accounts</h5>
        </div>
        <div class="list-group">
            <#list accounts as account>
            <button type="button"  value="${account.name}" class="list-group-item" onclick="setAccountValue(this.value); setColor(this.value,this.className)">${account.name}</button>
            </#list>
        </div>
    </div>
</div>


<input type="hidden" name="twitterAccount" value="" id="accountName">
<input type="hidden" name="queryId" value="${queryId}" id="uniqueId">
<div class="container customCount">
    <button class="btn btn-primary btnColor" id="submit" disabled="true" type="submit">Send request</button>
    <a class="btn btn-primary btnColor" id="cancel" role="button" href="search">Back to search</a>
</div>

</form>
<script type="text/javascript">
    function setAccountValue(name)
    {
        document.getElementById("accountName").value = name;
        var buttons = document.getElementsByClassName('btn btn-primary');
        for(const btn of buttons)
        {
            if(btn.id != 'cancel')
            {
                btn.disabled = false;
            }
        }
    }
</script>
</body>
</html>