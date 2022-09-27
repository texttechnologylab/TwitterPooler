<html lang="en" xmlns:display="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
    <link rel="stylesheet" href="css/style.css">
    <#include "styles/style.css">
    <title>History</title>
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
            <h2>List of finished Queries</h2>
        </div>
        <div class="table-responsive-sm">
            <table class="table table-striped w-auto table-sm small">
                <thead>
                <tr>
                    <th>Delete</th>
                    <th>Number of tweets</th>
                    <th>Downloaded tweets</th>
                    <th>Status</th>
                    <th>Endpoint</th>
                    <th>Query</th>
                    <th>Start time</th>
                    <th>End time</th>
                    <th>QueryID</th>
                    <th>User</th>
                    <th>Created at</th>
                    <th>One</th>
                    <th>Multi</th>

                </tr>
                </thead>
                <tbody>
                <#list queryAccounts as query>
                <tr>
                    <td><a id="delQuery:${query.queryId}" onclick="delQuery('${query.queryId}')" class="btn btn-primary btn-sm btn-block btnColor" type="submit" >Delete</a></td>
                    <td>${query.allTweets}</td>
                    <td>${query.tweetCount}</td>
                    <td>${query.status}</td>
                    <td>${query.endpoint}</td>
                    <td>${query.query}</td>
                    <td>${query.startTime}</td>
                    <td>${query.endTime}</td>
                    <td>${query.queryId}</td>
                    <td>${query.user}</td>
                    <td>${query.createdAt}</td>
                    <td>${query.one}</td>
                    <td>${query.multi}</td>
                    <form id="form:${query.queryId}" action="download/upload" method="get">
                        <td> <a onclick="document.getElementById('form:${query.queryId}').submit(); timeout()"
                            <#if query.one != "no available" && query.multi != "no available">
                            class="btn btn-primary btn-sm btn-block btnColor disabled"
                            <#else>
                            class="btn btn-primary btn-sm btn-block btnColor"</#if> type="submit">Create</a>
                        <select id="${query.queryId}" class="form-select inputPoolingLimit" name="file" aria-label="Default select example">
                            <#if query.one == "no available">
                            <option selected value="one">One</option>
                        </#if>
                        <#if query.multi == "no available" && query.one != "no available">
                        <option selected value="multi">Multi</option>
                        <#elseif query.multi == "no available">
                        <option value="multi">Multi</option>
                    </#if>
                    </select>
                    <input name="type" value="query" hidden="true">
                    <input name="art" value="query" hidden="false">
                    <input name="queryId" value="${query.queryId}" hidden="true">
                    </td>
                    </form>
                    <form id="get:${query.queryId}" action="download/get" method="get">
                        <td> <a onclick="getFileQuery('one', '${query.queryId}')" value="One"  <#if query.one != "available">
                            class="btn btn-primary btn-sm btn-block btnColor disabled"
                            <#else>
                            class="btn btn-primary btn-sm btn-block btnColor"
                        </#if> type="submit">One</a>
                        <a id="delOne:${query.queryId}" onclick="delFileQuery('one', '${query.queryId}')" class="badge badge-light" <#if query.one == "available">style="visibility: inherit;"<#else>style="visibility: hidden;"</#if> type="submit">Delete</a>
                    </td>
                    <td> <a onclick="getFileQuery('multi', '${query.queryId}')" value="Multi" <#if query.multi != "available">
                        class="btn btn-primary btn-sm btn-block btnColor disabled"
                        <#else>
                        class="btn btn-primary btn-sm btn-block btnColor"</#if> type="submit" >Multi</a>
                    <a id="delMulti:${query.queryId}" onclick="delFileQuery('multi', '${query.queryId}')"  class="badge badge-light" type="submit" <#if query.multi == "available">style="visibility: inherit;"<#else>style="visibility: hidden;"</#if> >Delete</a>
                </td>
                <input id="file:${query.queryId}" name="file" value="" hidden="false">
                <input name="art" value="query" hidden="false">
                <input name="queryId" value="${query.queryId}" hidden="false">
                </form>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="container customHistory">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h2>List of finished pools</h2>
        </div>
        <div class="table-responsive-sm">
            <table class="table table-striped w-auto table-sm small">
                <thead>
                <tr>
                    <th>Delete</th>
                    <th>Status</th>
                    <th>Pooling art</th>
                    <th>Query</th>
                    <th>Created at </th>
                    <th>Number of pools </th>
                    <th>Number of org.texttechnologylab.twitterpooler.tweets</th>
                    <th>One </th>
                    <th>Multi </th>
                    <th>Create file</th>
                    <th>Download</th>
                    <th>Download</th>
                </tr>
                </thead>
                <tbody>
                <#list poolAccounts as pool>
                <tr>
                    <td><a id="delPool:${pool.poolArt}:${pool.queryId}" onclick="delPool('${pool.poolArt}', '${pool.queryId}')" class="btn btn-primary btn-sm btn-block btnColor" type="submit" >Delete</a></td>
                    <td>${pool.status}</td>
                    <td>${pool.poolArt}</td>
                    <td>${pool.queryId}</td>
                    <td>${pool.createdAt}</td>
                    <td>${pool.numberOfPools}</td>
                    <td>${pool.numberOfCollections}</td>
                    <td>${pool.one}</td>
                    <td>${pool.multi}</td>
                    <form id="${pool.poolArt}:${pool.queryId}" action="download/upload" method="get">
                        <td> <a onclick="document.getElementById('${pool.poolArt}:${pool.queryId}').submit(); timeout()"
                            <#if pool.one != "no available" && pool.multi != "no available">
                            class="btn btn-primary btn-sm btn-block btnColor disabled"
                            <#else>
                            class="btn btn-primary btn-sm btn-block btnColor"</#if> type="submit">Create</a>
                        <select id="${pool.poolArt}-${pool.queryId}" class="form-select inputPoolingLimit" name="file" aria-label="Default select example">
                            <#if pool.one == "no available">
                            <option selected value="one">One</option>
                            </#if>
                            <#if pool.multi == "no available" && pool.one != "no available">
                            <option selected value="multi">Multi</option>
                            <#elseif pool.multi == "no available">
                            <option value="multi">Multi</option>
                            </#if>
                        </select>
                            <input name="type" value="pool" hidden="true">
                            <input name="art" value="${pool.poolArt}" hidden="true">
                            <input name="queryId" value="${pool.queryId}" hidden="true">
                    </td>
                    </form>
                    <form id="get:${pool.poolArt}:${pool.queryId}" action="download/get" method="get">
                    <td> <a onclick="getFile('one', '${pool.poolArt}', '${pool.queryId}')" value="One"  <#if pool.one != "available">
                        class="btn btn-primary btn-sm btn-block btnColor disabled"
                        <#else>
                        class="btn btn-primary btn-sm btn-block btnColor"
                    </#if> type="submit">One</a>
                            <a id="delOne:${pool.poolArt}:${pool.queryId}" onclick="delFilePool('one', '${pool.poolArt}', '${pool.queryId}')" class="badge badge-light" <#if pool.one == "available">style="visibility: inherit;"<#else>style="visibility: hidden;"</#if> type="submit">Delete</a>
                        </td>
                    <td> <a onclick="getFile('multi', '${pool.poolArt}', '${pool.queryId}')" value="Multi" <#if pool.multi != "available">
                        class="btn btn-primary btn-sm btn-block btnColor disabled"
                        <#else>
                        class="btn btn-primary btn-sm btn-block btnColor"</#if> type="submit" >Multi</a>
                    <a id="delMulti:${pool.poolArt}:${pool.queryId}" onclick="delFilePool('multi', '${pool.poolArt}', '${pool.queryId}')"  class="badge badge-light" type="submit" <#if pool.multi == "available">style="visibility: inherit;"<#else>style="visibility: hidden;"</#if> >Delete</a>
                        </td>
                        <input id="file:${pool.poolArt}:${pool.queryId}" name="file" value="" hidden="false">
                        <input name="art" value="${pool.poolArt}" hidden="false">
                        <input name="queryId" value="${pool.queryId}" hidden="false">
                    </form>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="container customHistory" >
    <div class="panel panel-primary">
        <div class="panel-heading">
            <a class="btn btn-primary btn-lg btn-block btnColor"  href="home" role="button">Back to home</a>
        </div>
    </div>
</div>
<script type="text/javascript">
    function timeout()
    {
        setTimeout(() => window.location.reload(), 5000);
    }
</script>
<script type="text/javascript">
    function getFile(file, art, query)
    {
        var id = "file:"+art+":"+query;
        document.getElementById(id).value = file;

        var formId = "get:"+art+":"+query;
        document.getElementById(formId).submit();
    }
</script>
<script type="text/javascript">
    function getFileQuery(file, query)
    {
        var id = "file:"+query;
        document.getElementById(id).value = file;

        var formId = "get:"+query;
        document.getElementById(formId).submit();
    }
</script>
<script type="text/javascript">
    function delFilePool(file, art, query)
    {
        var name = art+"-"+query+"-"+file+".zip";
        //document.getElementById("filename").value = file;

        if(file==="one")
        {
        var formId = "delOne:"+art+":"+query;
        document.getElementById(formId).href = "download/delete?filename="+name+"&art="+art+"&queryId="+query+"&file="+file;
        }
        else
        {
        var formId = "delMulti:"+art+":"+query;
        document.getElementById(formId).href = "download/delete?filename="+name+"&art="+art+"&queryId="+query+"&file="+file;

        }
        timeout();
    }
</script>
<script type="text/javascript">
    function delPool(art, query)
    {

        var formId = "delPool:"+art+":"+query;
        document.getElementById(formId).href = "history/delete?type=pool"+"&art="+art+"&queryId="+query;

        timeout();
    }
</script>
<script type="text/javascript">
    function delQuery(query)
    {
        var formId = "delQuery:"+query;
        document.getElementById(formId).href = "history/delete?type=query"+"&queryId="+query;

        timeout();
    }
</script>
<script type="text/javascript">
    function delFileQuery(file, query)
    {
        var name = query+".zip";

        if(file==="one")
        {
        name = query+".xmi";
        var formId = "delOne:"+query;
        document.getElementById(formId).href = "download/delete?filename="+name+"&art=query"+"&queryId="+query+"&file="+file;
        }
        else
        {
        var formId = "delMulti:"+query;
        document.getElementById(formId).href = "download/delete?filename="+name+"&art=query"+"&queryId="+query+"&file="+file;

        }
        timeout();
    }
</script>
<script type="text/javascript">
    function check(status)
    {
        if (status === "no available")
        {
            return true;
        }
        else
        {
            return false;
        }
    }
</script>

</body>
</html>
