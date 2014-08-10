﻿<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>微信单图消息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <link type="text/css" rel="stylesheet" href="plug-in/weixin/css/appmsg_edit.css" />
  <link type="text/css" rel="stylesheet" href="plug-in/weixin/css/jquery.fileupload.css" />
  <link type="text/css" rel="stylesheet" href="plug-in/bootstrap/css/bootstrap.min.css" />
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  
  <!--fileupload-->
  <script type="text/javascript" src="plug-in/weixin/js/vendor/jquery.ui.widget.js"></script>
  <script type="text/javascript" src="plug-in/weixin/js/load-image.min.js"></script>
  <script type="text/javascript" src="plug-in/weixin/js/jquery.fileupload.js"></script>
  <script type="text/javascript" src="plug-in/weixin/js/jquery.fileupload-process.js"></script>
  <script type="text/javascript" src="plug-in/weixin/js/jquery.fileupload-image.js"></script>
  <script type="text/javascript" src="plug-in/weixin/js/jquery.iframe-transport.js"></script>
  <!--UEditor-->
  <script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.config.js"></script>
  <script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.all.min.js"></script>
    
  <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
  <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
  <script type="text/javascript" charset="utf-8" src="plug-in/ueditor/lang/zh-cn/zh-cn.js"></script>
  
  <script type="text/javascript">
  //编写自定义JS代码
/*jslint unparam: true, regexp: true */
/*global window, $ */
$(function () {
    'use strict';
    // Change this to the location of your server-side upload handler:
    var url = 'weixinArticleController.do?upload',
        uploadButton = $('<button/>')
            .addClass('btn btn-primary')
            .prop('disabled', true)
            .text('上传中...')
            .on('click', function () {
                var $this = $(this), data = $this.data();
                $this.off('click').text('正在上传...').on('click', function () {
                        $this.remove();
                        data.abort();
                });
                data.submit().always(function () {
                    $this.remove();
                });
            });
    $('#fileupload').fileupload({
        url: url,
        dataType: 'json',
        autoUpload: false,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        maxFileSize: 2000000, // 2 MB
        disableImageResize: /Android(?!.*Chrome)|Opera/
            .test(window.navigator.userAgent),
        previewMaxWidth: 290,
        previewMaxHeight: 160,
        previewCrop: true
    }).on('fileuploadadd', function (e, data) {
        $("#files").text("");
        data.context = $('<div/>').appendTo('#files');
        $.each(data.files, function (index, file) {
            //var node = $('<p/>').append($('<span/>').text(file.name));
            //fileupload
            var node = $('<p/>');
            if (!index) {
                node.append('<br>').append(uploadButton.clone(true).data(data));
            }
            node.appendTo(data.context);
        });
    }).on('fileuploadprocessalways', function (e, data) {
        var index = data.index,
            file = data.files[index],
            node = $(data.context.children()[index]);
        if (file.preview) {
            node.prepend('<br>').prepend(file.preview);
        }
        if (file.error) {
            node
                .append('<br>')
                .append($('<span class="text-danger"/>').text(file.error));
        }
        if (index + 1 === data.files.length) {
            data.context.find('button')
                .text('上传')
                .prop('disabled', !!data.files.error);
        }
    }).on('fileuploadprogressall', function (e, data) {
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#progress .progress-bar').css(
            'width',
            progress + '%'
        );
    }).on('fileuploaddone', function (e, data) {
        console.info(data);
        var  file = data.files[0];
        //var delUrl = "<a class=\"js_removeCover\" onclick=\"return false;\" href=\"javascript:void(0);\">删除</a>";
        $("#imgName").text("").append(file.name);
        $("#progress").hide();
		var d =data.result;
		if (d.success) {
			var link = $('<a>').attr('target', '_blank').prop('href', d.attributes.viewhref);
        	$(data.context.children()[0]).wrap(link);
        	console.info(d.attributes.viewhref);
        	$("#imagePath").val(d.attributes.url);
		}else{
			var error = $('<span class="text-danger"/>').text(d.msg);
            $(data.context.children()[0]).append('<br>').append(error);
		}
    }).on('fileuploadfail', function (e, data) {
        $.each(data.files, function (index, file) {
            var error = $('<span class="text-danger"/>').text('File upload failed.');
            $(data.context.children()[index])
                .append('<br>')
                .append(error);
        });
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});
</script>
  
 </head>
 <body>
 	<div class="main_bd">
 		<div class="media_preview_area">
				<div class="appmsg editing">
					<div class="appmsg_content" id="js_appmsg_preview">
							<h4 class="appmsg_title">
								<a target="_blank" href="javascript:void(0);"
									onclick="return false;">标题</a>
							</h4>
							<div class="appmsg_info">
								<em class="appmsg_date"></em>
							</div>
							<div id="files" class="files">
								<i class="appmsg_thumb default">封面图片</i>
							</div>
							 <div id="progress" class="progress">
						        <div class="progress-bar progress-bar-success"></div>
						    </div>
							<p class="appmsg_desc"></p>
					</div>
				</div>
		</div>
 
 		<div class="media_edit_area" id="js_appmsg_editor">	
 			<div class="appmsg_editor" style="margin-top: 0px;">
	 			<div class="inner">
		  		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="weixinArticleController.do?doAdd" tiptype="1">
							<input id="id" name="id" type="hidden" value="${weixinArticlePage.id }">
							<input id="templateId" name="templateId" type="hidden" value="${templateId}">
							<input id="createDate" name="createDate" type="hidden" value="${weixinArticlePage.createDate }">
				<table style="width: 500px;" cellpadding="0" cellspacing="1" class="formtable">
						<tr>
							<td align="right">
								<label class="Validform_label">
									标题:
								</label>
							</td>
							<td class="value">
							     	 <input id="title" name="title" type="text" style="width: 300px" class="inputxt"  datatype="*">
									<span class="Validform_checktip"></span>
									<label class="Validform_label" style="display: none;">标题</label>
								</td>
						</tr>
						<tr>
							<td align="right">
								<label class="Validform_label">
									作者:
								</label>
							</td>
							<td class="value">
							     	 <input id="author" name="author" type="text" style="width: 300px" class="inputxt" >
									<span class="Validform_checktip"></span>
									<label class="Validform_label" style="display: none;">作者</label>
								</td>
						</tr>
						<tr>
							<td align="right">
								<label class="Validform_label">
									图片链接:
								</label>
							</td>
							<td class="value">
								<span class="btn btn-success fileinput-button">
							        <i class="glyphicon glyphicon-plus"></i>
							        <span>浏览</span>
							        <!-- The file input field used as target for the file upload widget -->
							        <input id="fileupload" type="file" name="files[]" multiple>
							        <input id="imagePath" name=imagePath type="hidden"  datatype="*" nullmsg="请添加图片">
							    </span>
							    <span id="imgName"></span> 
									<span class="Validform_checktip"></span>
									<label class="Validform_label" style="display: none;">图片链接</label>
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="Validform_label">
									摘要:
								</label>
							</td>
							<td class="value">
							     	 <input id="description" name="description" type="text" style="width: 300px" class="inputxt"  
										               
										               >
									<span class="Validform_checktip"></span>
									<label class="Validform_label" style="display: none;">摘要</label>
								</td>
						</tr>
						<tr>
							<td align="right">
								<label class="Validform_label">
									正文:
								</label>
							</td>
							<td class="value">
								 <textarea name="content" id="content"></textarea>
							    <script type="text/javascript">
							        var editor = UE.getEditor('content');
							    </script>
								</td>
						</tr>
						<tr>
							<td align="right">
								<label class="Validform_label">
									顺序:
								</label>
							</td>
							<td class="value">
							     	 <input id="orders" name="orders" type="text" style="width: 150px" class="inputxt" datatype="*">
									<span class="Validform_checktip"></span>
									<label class="Validform_label" style="display: none;">顺序</label>
								</td>
						</tr>
					</table>
				</t:formvalid>
				</div>
				<i class="arrow arrow_out" style="margin-top: 0px;"></i>
				<i class="arrow arrow_in" style="margin-top: 0px;"></i>
		</div>
	</div>
   </div>
 </body>
  <script src = "webpage/weixin/newstemplate/weixinArticle.js"></script>		