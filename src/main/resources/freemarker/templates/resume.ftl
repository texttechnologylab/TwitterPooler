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
<nav class="navbar navbar-dark bg-dark customNavbar">
    <a class="navbar-brand" href="#">
        <img src="Logo.png" width="50" height="50" alt="myImage"/>
        TwitterPooler
    </a>
</nav>
<form action="info" method="get">
    <div class="container customResume">
        <div class="panel panel-info">
            <h2>Please select the query and account which you want to use.</h2>
        </div>
    </div>

    <div class="container customResume">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h5>Querys</h5>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <#list queryAccounts as query>
                    <button type="button" id="queryBtn" value="${query.queryId}" class="list-group-item query" style="background: #21D5D31F;" onclick="setQueryId(this.value); setColor(this.value,this.className)">Query
                        : ${query.query} Number of org.texttechnologylab.twitterpooler.tweets : ${query.tweetCount}
                        Start time : ${query.startTime} End time : ${query.endTime}</button>
                    </#list>
                </div>
            </div>
        </div>
    </div>

    <div class="container customResume">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h5>Accounts</h5>
            </div>
            <div class="list-group">
                <#list accounts as account>
                <button type="button" id="accountBtn"  value="${account.name}" class="list-group-item account" style="background: #21D5D31F;" onclick="setAccountValue(this.value); setColor(this.value,this.className)" >${account.name}</button>
                </#list>
            </div>
        </div>
    </div>

    <div class="container customResume" style ="margin-bottom:10px">
        <div class="input-group">
            <div class="input-group-addon">
                <span class="input-group-text lblColor" id="maxResult">Max result</span>
            </div>
            <input type="text" name="maxResult" class="form-control" placeholder="" aria-label="Max result" aria-describedby="maxResult">
        </div>
    </div>

    <input type="hidden" name="twitterAccount" value="" id="accountName">
    <input type="hidden" name="queryId" value="" id="queryId">
    <div class="container">
        <button class="btn btn-primary btnColor" disabled="true" type="submit">Send request</button>
        <a class="btn btn-primary btnColor" role="button" href="home">Back to home</a>
    </div>

</form>
<script type="text/javascript">
    function setAccountValue(name)
    {
        document.getElementById("accountName").value = name;
        if(document.getElementById("queryId").value != "")
                {
                    var buttons = document.getElementsByClassName('btn btn-primary');
                    for(const btn of buttons)
                    {
                        if(btn.id != 'cancel')
                        {
                            btn.disabled = false;
                        }
                    }
                }
    }
</script>
<script type="text/javascript">
    function setQueryId(name)
    {
        document.getElementById("queryId").value = name;

        if(document.getElementById("accountName").value != "")
        {
            var buttons = document.getElementsByClassName('btn btn-primary');
            for(const btn of buttons)
            {
                if(btn.id != 'cancel')
                {
                    btn.disabled = false;
                }
            }
        }
    }
</script>

</body>
</html>