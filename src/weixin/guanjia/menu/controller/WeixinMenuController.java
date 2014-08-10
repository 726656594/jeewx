package weixin.guanjia.menu.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.guanjia.menu.entity.WeixinMenuEntity;
import weixin.guanjia.menu.service.WeixinMenuServiceI;

/**   
 * @Title: Controller
 * @Description: 微信菜单
 * @author onlineGenerator
 * @date 2014-01-02 22:29:31
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/weixinMenuController")
public class WeixinMenuController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WeixinMenuController.class);

	private static final String ID_LENGTH = "3"; //菜单ID长度
	
	@Autowired
	private WeixinMenuServiceI weixinMenuService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 微信菜单列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "weixinMenu")
	public ModelAndView weixinMenu(HttpServletRequest request) {
		return new ModelAndView("weixin/guanjia/menu/weixinMenuList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public List<TreeGrid> datagrid(WeixinMenuEntity weixinMenu, TreeGrid queryTreeGrid) {
		String pId = "";
		if (queryTreeGrid != null && StringUtil.isNotEmpty(queryTreeGrid.getId())) {
			pId = queryTreeGrid.getId();
		}
		String hql = "from WeixinMenuEntity where SUBSTR(id,1,LENGTH(id)-"+ID_LENGTH+") = '" + pId + "'";
		//1.将组织好HQL语句查询
		List<WeixinMenuEntity> weixinMenuList = systemService.findByQueryString(hql);
		//2.封装treeGrid结果
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		for (WeixinMenuEntity weixinMenuEntity : weixinMenuList) {
			TreeGrid treeGrid = new TreeGrid();
			treeGrid.setId(weixinMenuEntity.getId());
			treeGrid.setText(weixinMenuEntity.getName());
			treeGrid.setSrc(weixinMenuEntity.getWeixinCode());
			treeGrid.setOperations(weixinMenuEntity.getCategory());
			treeGrid.setState("closed");
			treeGrids.add(treeGrid);
		}
		return treeGrids;
	}
	
	/**
	 * 下拉菜单
	 */
	@RequestMapping(params = "queryParenTreeList")
	@ResponseBody
	public List<ComboTree> queryParenTreeList(HttpServletRequest request, ComboTree qureyComboTree) {
		String pId = "";
		if (qureyComboTree != null && StringUtil.isNotEmpty(qureyComboTree.getId())) {
			pId = qureyComboTree.getId();
		}
		String hql = "from WeixinMenuEntity where SUBSTR(id,1,LENGTH(id)-"+ID_LENGTH+") = '" + pId + "'";
		//1.查询出菜单列表
		List<WeixinMenuEntity> weixinMenuEntityList = systemService.findByQueryString(hql);
		//2.组织comboTree
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		for (WeixinMenuEntity weixinMenuEntity : weixinMenuEntityList) {
			ComboTree comboTree = new ComboTree();
			comboTree.setId(weixinMenuEntity.getId());
			comboTree.setText(weixinMenuEntity.getName());
			comboTree.setState("closed");
			comboTrees.add(comboTree);
		}
		return comboTrees;
	}
	

	/**
	 * 删除微信菜单
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WeixinMenuEntity weixinMenu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		//1.以ID开头的所有菜单
		String hql = "from WeixinMenuEntity where id like '" + weixinMenu.getId() + "%'";
		List<WeixinMenuEntity> weixinMenuList = systemService.findByQueryString(hql);
		message = "微信菜单删除成功";
		try {
			for (WeixinMenuEntity weixinMenuEntity : weixinMenuList) {
				weixinMenuService.delete(weixinMenuEntity);
			}
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信菜单删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 添加微信菜单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WeixinMenuEntity weixinMenu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "微信菜单添加成功";
		//1.取得父菜单的值
		String parentId = StringUtil.isEmpty(request.getParameter("parentId"), "");
		//%[index$][标识][最小宽度]转换方式
		String formatStr = "%1$0" + ID_LENGTH + "d";
		//2.查询同级子菜单的最大值
		String hql = "from WeixinMenuEntity where SUBSTR(id, 1, LENGTH(id) - "+ID_LENGTH+") = '" + parentId + "' ORDER BY  id DESC";
		List<WeixinMenuEntity> weixinMenuList = systemService.findByQueryString(hql);
		//3.查到同级最大的子菜单+1，否则parentId + 初始值
		if (weixinMenuList != null && weixinMenuList.size() > 0) {
			String maxSubMenuId = weixinMenuList.get(0).getId().substring(parentId.length());
			weixinMenu.setId(parentId + String.format(formatStr, Integer.parseInt(maxSubMenuId) + 1));
		} else {
			weixinMenu.setId(parentId + String.format(formatStr, 1));
		}
		try {
			weixinMenuService.save(weixinMenu);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信菜单添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新微信菜单
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WeixinMenuEntity weixinMenu, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "微信菜单更新成功";
		WeixinMenuEntity t = weixinMenuService.get(WeixinMenuEntity.class, weixinMenu.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(weixinMenu, t);
			weixinMenuService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "微信菜单更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 微信菜单新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(WeixinMenuEntity weixinMenu, HttpServletRequest req, String parentId) {
		WeixinMenuEntity weixinMenuPage = new WeixinMenuEntity();
		if (StringUtil.isNotEmpty(parentId)) {
			weixinMenuPage = systemService.getEntity(WeixinMenuEntity.class, parentId);
		}
		req.setAttribute("weixinMenuPage", weixinMenuPage);
		return new ModelAndView("weixin/guanjia/menu/weixinMenu-add");
	}
	
	/**
	 * 微信菜单编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WeixinMenuEntity weixinMenu, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(weixinMenu.getId())) {
			weixinMenu = weixinMenuService.getEntity(WeixinMenuEntity.class, weixinMenu.getId());
			req.setAttribute("weixinMenuPage", weixinMenu);
		}
		return new ModelAndView("weixin/guanjia/menu/weixinMenu-update");
	}
}
