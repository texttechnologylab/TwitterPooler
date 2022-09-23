<html lang="en" xmlns:display="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <#include "styles/style.css">
    <title>TTLab TwitterPooler - Hello ${name}!</title>
</head>
<body>
<nav class="navbar navbar-dark bg-dark customNavbar">
    <a class="navbar-brand" href="#">
        <img src="Logo.png" width="50" height="50" alt="myImage"/>
        TwitterPooler
    </a>
</nav>

<div class="container" style ="display:inline-table; max-width: 35%;">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h2>List of not finished Queries</h2>
        </div>
        <div class="table-responsive-sm">
            <table class="table table-striped w-auto table-sm small">
                <thead>
                <tr>
                    <th>Number tweets</th>
                    <th>Downloaded tweets</th>
                    <th>Status</th>
                    <th>Endpoint</th>
                    <th>Query</th>
                    <th>Start time</th>
                    <th>End time</th>
                    <th>Next token</th>
                    <th>QueryID</th>
                    <th>User</th>
                    <th>Created at </th>

                </tr>
                </thead>
                <tbody>
                <#list queryAccounts as query>
                <tr>
                    <td>${query.allTweets}</td>
                    <td>${query.tweetCount}</td>
                    <td>${query.status}</td>
                    <td>${query.endpoint}</td>
                    <td>${query.query}</td>
                    <td>${query.startTime}</td>
                    <td>${query.endTime}</td>
                    <td>${query.nextToken}</td>
                    <td>${query.queryId}</td>
                    <td>${query.user}</td>
                    <td>${query.createdAt}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="container center customHome">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h2>List of not finished pools</h2>
        </div>
        <div class="table-responsive-sm">
            <table class="table table-striped w-auto table-sm small">
                <thead>
                <tr>
                    <th>Status</th>
                    <th>Pooling art</th>
                    <th>Query</th>
                    <th>Created at </th>
                </tr>
                </thead>
                <tbody>
                <#list poolAccounts as pool>
                <tr>
                    <td>${pool.status}</td>
                    <td>${pool.poolArt}</td>
                    <td>${pool.queryId}</td>
                    <td>${pool.createdAt}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="container center customHome">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h2>Twitter accounts List</h2>
        </div>
        <div class="table-responsive-sm">
            <table class="table table-striped w-auto table-sm small">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Twitter cap</th>
                    <th>Twitter limit</th>
                    <th>Type</th>
                    <th>Reset on</th>
                </tr>
                </thead>
                <tbody>
                <#list twitterAccounts as account>
                <tr>
                    <td>${account.name}</td>
                    <td>${account.twitterCap}</td>
                    <td>${account.twitterLimit}</td>
                    <td>${account.type}</td>
                    <td>${account.reset?string}</td>

                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="container center customHome">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <a class="btn btn-primary btn-lg btn-block btnColor"  href="search" role="button">Create query</a>
            <a class="btn btn-primary btn-lg btn-block btnColor"  href="resume" role="button">Resume query</a>
            <a class="btn btn-primary btn-lg btn-block btnColor"  href="pooling" role="button">Start pooling</a>
            <a class="btn btn-primary btn-lg btn-block btnColor"  href="history" role="button">History</a>
        </div>
    </div>
</div>
</body>
</html>