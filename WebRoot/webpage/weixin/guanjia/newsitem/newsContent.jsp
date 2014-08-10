<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="GBK"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
   <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

<title></title>
<style type="text/css">
#pp{width:96%;padding:0px 2%;margin:auto;}
#pp p{display:inline-block !important;width:100%;text-align:center;margin:auto;}
#pp div{width:100% !important;text-align:center;}


</style>
<script type="text/javascript" src="plug-in/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript">

	 jQuery(window).load(function () {  
	    var screenWidth = document.body.clientWidth;
           jQuery("div#pp img").each(function () {  
              if(this.width>screenWidth){
		this.width=screenWidth-0.08*screenWidth ;
                $(this).removeAttr("style");
		$(this).parent().removeAttr("style")

              }
           });  
       });  


     
</script>
</head>
<body>
<div id="pp">
<a href="weixin://contacts/profile/zzyoushijia" style="text-decoration:none;">关注中国洛阳10086</a>
${newsContent}
</div>
</body>
</html>