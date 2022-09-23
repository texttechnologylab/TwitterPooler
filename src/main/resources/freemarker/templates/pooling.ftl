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
    <title>Pooling site</title>
</head>
<body>
<form action="info" method="get">
    <nav class="navbar navbar-dark bg-dark customNavbar">
        <a class="navbar-brand" href="#">
            <img src="Logo.png" width="50" height="50" alt="myImage"/>
            TwitterPooler
        </a>
    </nav>
    <div class="container customPooling">
        <div class="panel panel-info">
            <h2>Please select the query which you want to use.</h2>
        </div>
    </div>

    <div class="container customPooling">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <h5>Querys</h5>
            </div>
            <div class="list-group">
                <#list queryAccounts as query>
                <button type="button"  value="${query.queryId}" class="list-group-item btnBigColore" onclick="setQueryId(this.value); setColor(this.value,this.className)">Query
                    : ${query.query} Number of org.texttechnologylab.twitterpooler.tweets : ${query.tweetCount}
                    Start time : ${query.startTime} End time : ${query.endTime}</button>
                </#list>
            </div>
        </div>
    </div>

    <!---<input type="hidden" name="poolArt" value="" id="pool">-->
    <input type="hidden" name="queryId" value="" id="uniqueId">

    <div class="container customPooling">
        <div class="input-group">
            <button class="btn btn-primary btnPooling btnColor" disabled="true" name="btn" value="hashtag" type="submit">Hashtag pooling</button>
            <span class="input-group-text lblPoolingLimit lblColor">Pool limit</span>
            <select class="form-select inputPoolingLimit" name="selectLimitHashtag" aria-label="Default select example">
                <option selected value="0">0</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="40">40</option>
                <option value="80">80</option>
                <option value="160">160</option>
                <option value="320">320</option>
                <option value="640">640</option>
                <option value="1280">1280</option>
            </select>
        </div>
    </div>


    <div class="container customPooling">
        <div class="input-group">
            <button class="btn btn-primary btnPooling btnColor" disabled="true" name="btn" value="author" type="submit">Author-wise pooling</button>
            <span class="input-group-text lblPoolingLimit lblColor">Pool limit</span>
            <select class="form-select inputPoolingLimit" name="selectLimitAuthor" aria-label="Default select example">
                <option selected value="0">0</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="40">40</option>
                <option value="160">160</option>
                <option value="320">320</option>
                <option value="640">640</option>
                <option value="1280">1280</option>
             </select>
        </div>
    </div>

    <div class="container customPooling">
        <div class="input-group">
            <button class="btn btn-primary btnPooling btnColor" disabled="true" name="btn" value="temporalDays" type="submit">Temporal pooling (Days)</button>
            <span class="input-group-text lblPoolingLimit lblColor">Pool limit</span>
            <select class="form-select inputPoolingLimit" name="selectLimitTempDays" aria-label="Default select example">
                <option selected value="0">0</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="40">40</option>
                <option value="160">160</option>
                <option value="320">320</option>
                <option value="640">640</option>
                <option value="1280">1280</option>
            </select>
            <span class="input-group-text lblPoolingLimit lblColor">Days size</span>
            <select class="form-select inputPoolingSize" name="selectSizeTempDays" aria-label="Default select example">
                <option selected value="1">1 day</option>
                <option value="2">2 days</option>
                <option value="4">4 days</option>
                <option value="6">6 days</option>
                <option value="8">8 days</option>
                <option value="12">12 days</option>
                <option value="24">24 days</option>
            </select>
        </div>
    </div>

    <div class="container customPooling">
        <div class="input-group">
            <button class="btn btn-primary btnPooling btnColor" disabled="true" name="btn" value="temporalHours" type="submit">Temporal pooling (Hours)</button>
            <span class="input-group-text lblPoolingLimit lblColor">Pool limit</span>
            <select class="form-select inputPoolingLimit" name="selectLimitTempHours" aria-label="Default select example">
                <option selected value="0">0</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="40">40</option>
                <option value="160">160</option>
                <option value="320">320</option>
                <option value="640">640</option>
                <option value="1280">1280</option>
            </select>
            <span class="input-group-text lblPoolingLimit lblColor">Hours size</span>
            <select class="form-select inputPoolingSize" name="selectSizeTempHours" aria-label="Default select example">
                <option selected value="1">1 hour</option>
                <option value="2">2 hours</option>
                <option value="4">4 hours</option>
                <option value="6">6 hours</option>
                <option value="8">8 hours</option>
                <option value="12">12 hours</option>
            </select>
        </div>
    </div>

    <div class="container customPooling">
        <div class="input-group">
            <button class="btn btn-primary btnPooling btnColor"  disabled="true" name="btn" value="burst" type="submit">Burst-score wise pooling</button>
            <span class="input-group-text lblPoolingLimit lblColor">Score limit</span>
            <select class="form-select inputPoolingLimit" name="selectLimitBurst" aria-label="Default select example">
                <option selected value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
            </select>
            <span class="input-group-text lblPoolingLimit lblColor">Days size</span>
            <select class="form-select inputPoolingSize" name="selectSizeDaysBrust" aria-label="Default select example">
                <option selected value="1">1 day</option>
                <option value="2">2 days</option>
                <option value="4">4 days</option>
                <option value="6">6 days</option>
                <option value="8">8 days</option>
                <option value="12">12 days</option>
                <option value="24">24 days</option>
            </select>
            <div class="input-group-addon">
                <span class="input-group-text lblColor" style="margin-left: 10px;" id="terms">Topics</span>
            </div>
            <input type="text" name="terms" class="form-control" style="margin-left: 10px;"  placeholder="" aria-label="Potential trending topics" aria-describedby="Potential trending topics">
        </div>
    </div>
    <div class="container customPooling">
        <a class="btn btn-primary btnPooling btnColor" id="cancel" role="button" href="home">Back to home</a>
    </div>
</form>
<script type="text/javascript">
    function setQueryId(name)
    {
        document.getElementById("uniqueId").value = name;

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