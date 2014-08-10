package weixin.guanjia.message.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.service.AutoResponseServiceI;
import weixin.guanjia.message.service.NewsItemServiceI;
import weixin.guanjia.message.service.NewsTemplateServiceI;


/**
 * 图文新闻中心
* 
 */
@Controller
@RequestMapping("/newsTemplateController")
public class NewsTemplateController {
	@Autowired
	private NewsTemplateServiceI newsTemplateService;
	@Autowired
	private NewsItemServiceI newsItemService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private AutoResponseServiceI autoResponseService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	private String message;
	
    @RequestMapping(params="list")
	public ModelAndView list(){
		return new ModelAndView("weixin/guanjia/newstemplate/newsTemplateList");
	}
    
    @RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(NewsTemplate newsTemplate,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
		CriteriaQuery cq = new CriteriaQuery(NewsTemplate.class, dataGrid);
		cq.eq("accountId", ResourceUtil.getWeiXinAccount().getAccountid());
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, newsTemplate);
		
		this.newsTemplateService.getDataGridReturn(cq, true);
		
		TagUtil.datagrid(response, dataGrid);
	}
    
  
	@RequestMapping(params="del")
	@ResponseBody
	public AjaxJson del(NewsTemplate newsTemplate,HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, newsTemplate.getId());
		
		this.newsTemplateService.delete(newsTemplate);
	
		message = "删除信息数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}
	
	
	@RequestMapping(params="jumpSuView")
	public ModelAndView jumpSuView(HttpServletRequest req){
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		if(StringUtil.isNotEmpty(id)){
			NewsTemplate newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, id);
			String type = newsTemplate.getType();
			req.setAttribute("type", type);
			req.setAttribute("tempateName", newsTemplate.getTemplateName());
		}
		return new ModelAndView("weixin/guanjia/newstemplate/newsTemplateInfo");
	}

	@RequestMapping(params="su")
	@ResponseBody
	public AjaxJson updateAndSave(NewsTemplate newsTemplate,HttpServletRequest req){
		
		AjaxJson j = new AjaxJson();
		String id= newsTemplate.getId();
		if(StringUtil.isNotEmpty(id)){
			
			NewsTemplate  tempAutoResponse= this.newsTemplateService.getEntity(NewsTemplate.class, newsTemplate.getId());
			this.message =  "修改图文模板成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(newsTemplate, tempAutoResponse);
				this.newsTemplateService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			newsTemplate.setAddTime(sdf.format(new Date()));
			String accountId = ResourceUtil.getWeiXinAccount()
			.getAccountid();
			if (!"-1".equals(accountId)) {
				this.newsTemplateService.save(newsTemplate);
			} else {
				j.setSuccess(false);
				j.setMsg("请添加一个公众帐号。");
			}
			
		}
		return j;
	}
	

	@RequestMapping(params="jumpNewsItem")
	public ModelAndView jumpNewsItem(HttpServletRequest req){
		String templateId = req.getParameter("templateId");
		req.setAttribute("templateId", templateId);
		
		if(StringUtil.isNotEmpty(templateId)){
			NewsTemplate newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, templateId);
			req.setAttribute("type", newsTemplate.getType());
		}
		
		
		
		
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		if(StringUtil.isNotEmpty(id)){
			NewsItem newsItem = this.newsTemplateService.getEntity(NewsItem.class, id);
			req.setAttribute("title", newsItem.getTitle());
			req.setAttribute("content", newsItem.getContent());
			req.setAttribute("author", newsItem.getAuthor());
			req.setAttribute("imagePath", newsItem.getImagePath());
			req.setAttribute("description", newsItem.getDescription());
			req.setAttribute("templateId", newsItem.getNewsTemplate().getId());
			req.setAttribute("type", newsItem.getNewsTemplate().getType());
			req.setAttribute("orders", newsItem.getOrders());
		}else{
			//判断是否是头一条短信
			List<NewsItem> newsItemList = this.newsTemplateService.findByProperty(NewsItem.class, "newsTemplate.id", templateId);
			req.setAttribute("orders", (newsItemList.size()+1));
		}
		
		return new ModelAndView("weixin/guanjia/newstemplate/itemInfo");
	}
	
	@RequestMapping(params="jumpupload")
	public ModelAndView jumpUpload(HttpServletRequest req){
		return new ModelAndView("weixin/guanjia/newstemplate/upload");
	}
	
	@RequestMapping(params="savenews")
	@ResponseBody
	public AjaxJson saveNews(NewsItem newsItem,HttpServletRequest req){
		
		AjaxJson j = new AjaxJson();
		String id= newsItem.getId();
		if(StringUtil.isNotEmpty(id)){
			
			NewsItem  tempAutoResponse= this.newsTemplateService.getEntity(NewsItem.class, newsItem.getId());
			this.message =  "修改关键字回复成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(newsItem, tempAutoResponse);
				this.newsItemService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			String templateId = req.getParameter("templateId");
//			org.jeecgframework.core.util.LogUtil.info("....imagepath...."+newsItem.getImagePath()+".......content..."+newsItem.getContent());
//			org.jeecgframework.core.util.LogUtil.info("....title...."+newsItem.getTitle()+"....templateId...."+templateId);
			NewsTemplate newsTemplate = this.newsTemplateService.getEntity(NewsTemplate.class, templateId);
			newsItem.setNewsTemplate(newsTemplate);
			this.newsItemService.save(newsItem);
		}
		return j;
	}
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}