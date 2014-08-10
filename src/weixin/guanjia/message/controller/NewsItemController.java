package weixin.guanjia.message.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.service.NewsItemServiceI;


/**
 * 图文消息子类
* 
 */
@Controller
@RequestMapping("/newsItemController")
public class NewsItemController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private NewsItemServiceI newsItemService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	private String message;
	
    @RequestMapping(params="list")
	public ModelAndView jumpList(HttpServletRequest request){
    	
    	String templateId = request.getParameter("templateId");
    	request.setAttribute("templateId", templateId);
		return new ModelAndView("weixin/guanjia/newsitem/newsItemList");
	}
    
    @RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(NewsItem newsItem,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
    	String templateId = request.getParameter("templateId");
		CriteriaQuery cq = new CriteriaQuery(NewsItem.class, dataGrid);
		cq.eq("newsTemplate.id", templateId);
		cq.eq("accountId", ResourceUtil.getWeiXinAccount().getAccountid());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, newsItem);
		this.newsItemService.getDataGridReturn(cq, true);
		/*List<NewsItem> list =  dataGrid.getReaults();
		List<NewsItem> tempLisgt =  new ArrayList<NewsItem>();
		for(NewsItem news:list){
			news.getImagePath();
			news.setImagePath("12");
			tempLisgt.add(news);
		}
		dataGrid.setReaults(tempLisgt);*/
		TagUtil.datagrid(response, dataGrid);
	}
    
  
	@RequestMapping(params="del")
	@ResponseBody
	public AjaxJson deleteSmsGroup(NewsItem newsItem,HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		newsItem = this.systemService.getEntity(NewsItem.class, newsItem.getId());
		this.newsItemService.delete(newsItem);
		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}
	
	@RequestMapping(params="newscontent")
	public ModelAndView jumpNewsShow(HttpServletRequest request){
		String id = request.getParameter("id");
		NewsItem newsItem = this.newsItemService.getEntity(NewsItem.class, id);
		request.setAttribute("newsContent", newsItem.getContent());
		return new ModelAndView("weixin/guanjia/newsitem/newsContent");
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}