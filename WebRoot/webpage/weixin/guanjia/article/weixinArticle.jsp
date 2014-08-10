<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>微信素材</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckeditor/ckeditor.js"></script>
</head>
<body>
	<div id="content">
		<div id="wrapper">
			<div id="steps">
				<form id="formobj" action="weixinArticleController.do?save" name="formobj" method="post">
					<input id="id" name="id" type="hidden" value="${weixinArticlePage.id }">
					<fieldset style="float: left;">
						<div id="s_title">${weixinArticlePage.wxTitle}</div>
						<div id="s_pic_div">
							<img alt="." src="${weixinArticlePage.wxPicUrl}" id="s_pic" width="160px" height="120px" >
						</div>
					</fieldset>
					<fieldset style="float: right;">
						<table style="width: 400px;" cellpadding="0" cellspacing="1" class="formtable">
							<tr>
								<td><label class="Validform_label">标题</label>
									<div class="form">
										<input class="inputxt" id="wxTitle" name="wxTitle" ignore="ignore" value="${weixinArticlePage.wxTitle}"
											onblur="copyTitle();"> <span class="Validform_checktip"></span>
									</div></td>
							</tr>
							<tr>
								<td><label class="Validform_label">作者</label>
									<div class="form">
										<input class="inputxt" id="wxCrtuser" name="wxCrtuser" ignore="ignore" value="${weixinArticlePage.wxCrtuser}">
										<span class="Validform_checktip"></span>
									</div></td>
							</tr>
							<tr>
								<td><label class="Validform_label">封面</label>
									<div class="form">
										<t:upload name="fiels" uploader="systemController.do?saveFiles" extend="pic" id="file_upload" multi="false"></t:upload>
										<div id="picdiv" >
											<div id="filediv" style="height: 50px; display: none;"></div>
											<img alt="." src="${weixinArticlePage.wxPicUrl}" id="picShowDiv" width="240px" height="190px"> 
											<input type="hidden" id="wxPicUrl" name="wxPicUrl"
												ignore="ignore" value="${weixinArticlePage.wxPicUrl}">
										</div>
									</div></td>
							</tr>
							<tr>
								<td><label class="Validform_label">摘要</label>
									<div class="form">

										<textarea class="inputxt" id="wxSummary" name="wxSummary" ignore="ignore"
											value="${weixinArticlePage.wxSummary}" cols="60" rows="6">${weixinArticlePage.wxSummary}</textarea>
										<span class="Validform_checktip"></span>
									</div></td>
							</tr>
							<tr>
								<td><label class="Validform_label">正文</label>
									<div class="form">
										<textarea id="wxContent" name="wxContent" ignore="ignore" value="${weixinArticlePage.wxContent}" cols="60"
											rows="6">${weixinArticlePage.wxContent}</textarea>
										<script>
								CKEDITOR.replace('wxContent', {
									uiColor : '#9AB8F3',
									fullPage : false,
									allowedContent : false,
									toolbar : [ {
										name : 'basicstyles',
										items : [ 'Bold', 'Italic',
												'Underline', 'Strike',
												'Subscript', 'Superscript' ]
									} ]
								});
							</script>
									</div></td>
							</tr>
							<tr>
								<td>
									<div class="form">
										<label class="Validform_label">原文链接</label> <input class="inputxt" id="wxOriginUrl" name="wxOriginUrl"
											ignore="ignore" value="${weixinArticlePage.wxOriginUrl}"> <span class="Validform_checktip"></span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="form">
										<button type="submit" value="Submit">保存</button>
									</div>
								</td>
							</tr>
						</table>
					</fieldset>
					<link rel="stylesheet" href="plug-in/Validform/css/divfrom.css" type="text/css" />
					<link rel="stylesheet" href="plug-in/Validform/css/style.css" type="text/css" />
					<link rel="stylesheet" href="plug-in/Validform/css/tablefrom.css" type="text/css" />
					<script type="text/javascript" src="plug-in/Validform/js/Validform_v5.3.1_min.js"></script>
					<script type="text/javascript" src="plug-in/Validform/js/Validform_Datatype.js"></script>
					<script type="text/javascript" src="plug-in/Validform/js/datatype.js"></script>
					<SCRIPT type="text/javascript" src="plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js"></SCRIPT>
					<script type="text/javascript">
$(function(){$("#formobj").Validform();
		</script>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$('#file_upload')
					.uploadify(
							{
								buttonText : '上传',
								auto : true,
								progressData : 'speed',
								multi : false,
								height : 25,
								overrideEvents : [ 'onDialogClose' ],
								fileTypeDesc : '文件格式',
								queueID : 'filediv',
								fileTypeExts : '*.jpg;*,jpeg;*.png;*.gif;*.bmp;*.ico;*.tif',
								fileSizeLimit : '15MB',
								swf : 'plug-in/uploadify/uploadify.swf',
								uploader : 'systemController.do?saveFiles&jsessionid='
										+ $("#sessionUID").val() + '',
								onUploadStart : function(file) {
								},
								onQueueComplete : function(queueData) {
									var win = frameElement.api.opener;
									win.reloadTable();
									win.tip(serverMsg);
									frameElement.api.close();
								},
								onUploadSuccess : function(file, data, response) {
									data = eval('(' + data + ')');
									$('#wxPicUrl').val(data.attributes.url);
									$('#picShowDiv').attr("src",
											data.attributes.url);
									$('#picdiv').show();
									$('#filediv').hide();
									$('#s_pic')
											.attr("src", data.attributes.url)
											.show();
								},
								onFallback : function() {
									tip("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试")
								},
								onSelectError : function(file, errorCode,
										errorMsg) {
									switch (errorCode) {
									case -100:
										tip("上传的文件数量已经超出系统限制的"
												+ $('#file_upload').uploadify(
														'settings',
														'queueSizeLimit')
												+ "个文件！");
										break;
									case -110:
										tip("文件 ["
												+ file.name
												+ "] 大小超出系统限制的"
												+ $('#file_upload').uploadify(
														'settings',
														'fileSizeLimit')
												+ "大小！");
										break;
									case -120:
										tip("文件 [" + file.name + "] 大小异常！");
										break;
									case -130:
										tip("文件 [" + file.name + "] 类型不正确！");
										break;
									}
								},
								onUploadProgress : function(file,
										bytesUploaded, bytesTotal,
										totalBytesUploaded, totalBytesTotal) {
									$('#filediv').show();
								}
							});
		});

		function copyTitle() {
			$('#s_title').empty().append($('#wxTitle').val());
		}
	</script>
</body>