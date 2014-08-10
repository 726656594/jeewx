<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="weixinArticleList" checkbox="true" fitColumns="false" title="微信单图消息" actionUrl="weixinArticleController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="group"  width="36"></t:dgCol>
   <t:dgCol title="标题"  field="wxTitle"  hidden="true"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="作者"  field="wxAuthor"  hidden="true"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="图片链接"  field="wxPicUrl"  hidden="false"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="摘要"  field="wxSummary"  hidden="true"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="正文"  field="wxContent"  hidden="false"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="原文链接"  field="wxOriginUrl"  hidden="true"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="创建时间"  field="createDate"  hidden="true"  queryMode="group"  width="0"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="weixinArticleController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="weixinArticleController.do?goAdd" width="1040" height="400"  funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="weixinArticleController.do?goUpdate" width="1040" height="400" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="weixinArticleController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="weixinArticleController.do?goUpdate" width="1040" height="400" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 });
 </script>