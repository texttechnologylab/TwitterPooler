<html lang="en" xmlns:display="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          rel="stylesheet" >
    <link rel="stylesheet" href="css/style.css">-->
    <!-- Extra JavaScript/CSS added manually in "Settings" tab -->
    <!--<link rel="stylesheet" href="./styles/style.css">-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css">
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.18/css/bootstrap-select.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.18/js/bootstrap-select.min.js" integrity="sha512-yDlE7vpGDP7o2eftkCiPZ+yuUyEcaBwoJoIhdXv71KZWugFqEphIS3PU60lEkFaz8RxaVsMpSvQxMBaKVwA5xg==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <#include "styles/style.css">
    <title>Search site ${name}</title>
</head>
<body>
<nav class="navbar navbar-default customNavbar" style="height:80px !important; background: #343A40; width: 140%; margin-top:-3px;">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
                <img src="Logo.png" width="50" height="50" alt="myImage"/>
                <p class="navbar-text" style="margin-top: -37px; margin-left: 55px; color: white; font-size: 20px;">TwitterPooler</p>
            </a>
        </div>
    </div>
</nav>
<form action="count" method="get">
<div class="container customSearch">
    <div class="input-group">
      <div class="input-group-addon customInputLblSearch lblColor">
        <span class="input-group-text" id="search">Search</span>
      </div>
      <input type="text" name="search" onchange="disableOnChange(this.name)" class="form-control" placeholder="" aria-label="Search" aria-describedby="search">
        <div class="input-group-btn">
            <input type="dropdown" name="endpHash" class="btn btn-default lblColor" value="Recent" text="searchRecentText" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Recent</button>
            <ul class="dropdown-menu dropdown-menu-right" role="menu">
                <li><a data-value="FullArchive">Full archive</a></li>
                <li><a data-value="Recent">Recent</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container" style="visibility: collapse;">
    <div class="input-group">
      <div class="input-group-addon">
        <span class="input-group-text" id="keywords">Keywords</span>
      </div>
      <input type="text" name="keywords" onchange="disableOnChange(this.name)" class="form-control" placeholder="" aria-label="Keywords" aria-describedby="keywords">
        <div class="input-group-btn">
            <input type="dropdown" name="endpKeywords" class="btn btn-default dropdown-toggle" value="Recent" text="searchRecentText" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Recent<span class="caret"></span></button>
            <ul class="dropdown-menu dropdown-menu-right" role="menu">
                <li><a data-value="FullArchive">Full archive</a></li>
                <li><a data-value="Recent">Recent</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="userTweetTimelineById">User Timeline</span>
        </div>
        <input type="text" onchange="disableOnChange(this.name)" name="userTweetTimelineById" class="form-control customInputSearch" placeholder="" aria-label="User Timeline" aria-describedby="userTimeline">
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="userMentionTimelineById">User Mentioned</span>
        </div>
        <input type="text" onchange="disableOnChange(this.name)" name="userMentionTimelineById" class="form-control customInputSearch" placeholder="" aria-label="User Mentioned" aria-describedby="userMentioned">
    </div>
</div>

<div class="container" style="visibility: collapse;">
    <div class="input-group">
        <div class="input-group-addon">
            <span class="input-group-text" id="ddcCategory">DDC Category</span>
        </div>
        <input type="text" class="form-control" name="ddcCategory" placeholder="" aria-label="DDC Category" aria-describedby="ddcCategory">
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="liked">User Liked</span>
        </div>
        <input type="text" onchange="disableOnChange(this.name)" name="liked" class="form-control customInputSearch" placeholder="" aria-label="User Liked" aria-describedby="userLiked">
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="liking">Tweet Liking</span>
        </div>
        <input type="text" onchange="disableOnChange(this.name)" name="liking" class="form-control customInputSearch" placeholder="" aria-label="Tweet Liking" aria-describedby="tweetLiking">
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="maxResult">Max result</span>
        </div>
        <input type="text" name="maxResult" class="form-control customInputSearch" placeholder="" aria-label="Max result" aria-describedby="maxResult">
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="startTimeDesc">Start time</span>
        </div>
        <div class='input-group date' style="min-width: 100%;" id='startTime'>
            <input type='text' name="startTime" class="form-control"/>
            <span class="input-group-addon lblColor">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
</div>

<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon customInputLblSearch lblColor">
            <span class="input-group-text" id="endTimeDesc">End time</span>
        </div>
        <div class='input-group date' style="min-width: 100%;" id='endTime'>
            <input type='text' name="endTime" class="form-control"/>
            <span class="input-group-addon lblColor">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
</div>
<div class="container customSearch">
    <div class="input-group">
        <div class="input-group-addon lblColor">
            <span class="input-group-text"  id="STools">NLP Tools</span>
        </div>
        <select class="multi_select multi_select_box" name="STools" multiple data-selected-text-format="count>1">
        <#list nlpTools as tool>
            <option >${tool}</option>
        </#list>
         </select>
    </div>
</div>
<div class="container customSearch ">
    <button type="submit" name="subBtn" class="btn btn-primary btn-lg btn-block btnColor" disabled="true">Send request</button>
</div>
<div class="container customSearch">
    <a class="btn btn-primary btn-lg btn-block btnColor"  href="home" role="button">Back to home</a>
</div>
</form>
<script type="text/javascript">
    $(document).ready(function()
    {
        $('.multi_select').selectpicker();
    })
</script>

<script type="text/javascript">
        $(function() {
            $('#startTime').datetimepicker({
    format: 'YYYY-MM-DD HH:mm:ss'
});
        });
</script>
<script type="text/javascript">
        $(function() {
            $('#endTime').datetimepicker({
    format: 'YYYY-MM-DD HH:mm:ss'
});
        });
</script>
<script type="text/javascript">
    function disableOnChange(name)
    {
        const textBoxList = [document.getElementsByName("search")[0],document.getElementsByName("keywords")[0],
        document.getElementsByName("userTweetTimelineById")[0],document.getElementsByName("userMentionTimelineById")[0],document.getElementsByName("ddcCategory")[0],
        document.getElementsByName("liked")[0],document.getElementsByName("liking")[0]]

        const additionalBox = [document.getElementsByName("startTime")[0],
            document.getElementsByName("endTime")[0], document.getElementsByName("maxResult")[0]]

        var count = 0;
        for (const element of textBoxList) {
            if (element.value === "")
            {
                element.disabled = true;
                count += 1;
            }
        }
        if(count === textBoxList.length)
        {
            for (const element of textBoxList)
                {
                    element.disabled = false;
                }

            for (const element of additionalBox)
            {
                if (element.value === "")
                {
                    element.disabled = false;
                }
            }
        }

        if(name==="liked" && count != textBoxList.length)
        {
            document.getElementsByName("startTime")[0].disabled=true;
            document.getElementsByName("endTime")[0].disabled=true;
        }

        if (name==="liking" && count != textBoxList.length)
        {
            for (const element of additionalBox)
            {
                if (element.value === "")
                {
                    element.disabled = true;
                }
            }
        }

        //set button
        if (count != textBoxList.length)
        {
            document.getElementsByName("subBtn")[0].disabled=false;
        }
        else
        {
            document.getElementsByName("subBtn")[0].disabled=true;
        }
    }
</script>
<script>
   $(".dropdown-menu li a").click(function(){
  $(this).parents(".input-group-btn").find('.btn').html($(this).text() + ' <span class="caret"></span>');
  $(this).parents(".input-group-btn").find('.btn').val($(this).data('value'));
});
</script>
</body>

</html>
