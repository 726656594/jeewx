<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>微信素材</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript">
	function newArticle() {
		//window.location.href = "weixinArticleController.do?addorupdate&isIframe";
		alert('test');
	}
</script>
</head>
<body>
	<c:forEach var="item" items="${result}" varStatus="status">
		<fieldset style="float: left;">
			<form id="formobj" action="weixinArticleController.do?addorupdate" name="formobj" method="post">
			<input type="hidden" id="id" name="id" value="${item.id}">
				<table style="width: 400px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td onclick="newArticle();"><label class="Validform_label">${item.wxTitle }</label>
							<div class="form">
								<img alt="." src="${item.wxPicUrl }" width="160px" height="120px"><br> ${item.wxContent}
							</div></td>
					</tr>
					<tr>
						<td>
							<div class="form">
								<button type="submit" value="Submit">修改</button>
							</div>
						</td>
					</tr>
				</table>
			</form>
		</fieldset>
	</c:forEach>

</body>