package weixin.guanjia.account.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.util.WeixinUtil;

@Service("weixinAccountService")
@Transactional
public class WeixinAccountServiceImpl extends CommonServiceImpl implements
		WeixinAccountServiceI {

	@Override
	public <T> void delete(T entity) {
		super.delete(entity);
		// 执行删除操作配置的sql增强
		this.doDelSql((WeixinAccountEntity) entity);
	}

	@Override
	public <T> Serializable save(T entity) {
		Serializable t = super.save(entity);
		// 执行新增操作配置的sql增强
		this.doAddSql((WeixinAccountEntity) entity);
		return t;
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		super.saveOrUpdate(entity);
		// 执行更新操作配置的sql增强
		this.doUpdateSql((WeixinAccountEntity) entity);
	}

	/**
	 * 默认按钮-sql增强-新增操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doAddSql(WeixinAccountEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-更新操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doUpdateSql(WeixinAccountEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-删除操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doDelSql(WeixinAccountEntity t) {
		return true;
	}


	public String getAccessToken() {
		String token = "";
		WeixinAccountEntity account = findLoginWeixinAccount();
		token = account.getAccountaccesstoken();
		if (token != null && !"".equals(token)) {
			// 判断有效时间 是否超过2小时
			java.util.Date end = new java.util.Date();
			java.util.Date start = new java.util.Date(account.getAddtoekntime()
					.getTime());
			if ((end.getTime() - start.getTime()) / 1000 / 3600 >= 2) {
				// 失效 重新获取
				String requestUrl = WeixinUtil.access_token_url.replace(
						"APPID", account.getAccountappid()).replace(
						"APPSECRET", account.getAccountappsecret());
				JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl,
						"GET", null);
				if (null != jsonObject) {
					try {
						token = jsonObject.getString("access_token");
						// 重置token
						account.setAccountaccesstoken(token);
						// 重置事件
						account.setAddtoekntime(new Date());
						this.saveOrUpdate(account);
					} catch (Exception e) {
						token = null;
						// 获取token失败
						String wrongMessage = "获取token失败 errcode:{} errmsg:{}"+ jsonObject.getInt("errcode")+ jsonObject.getString("errmsg");
					}
				}
			} else {
				return account.getAccountaccesstoken();
			}
		} else {
			String requestUrl = WeixinUtil.access_token_url.replace("APPID",
					account.getAccountappid()).replace("APPSECRET",
					account.getAccountappsecret());
			JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET",
					null);
			if (null != jsonObject) {
				try {
					token = jsonObject.getString("access_token");
					// 重置token
					account.setAccountaccesstoken(token);
					// 重置事件
					account.setAddtoekntime(new Date());
					this.saveOrUpdate(account);
				} catch (Exception e) {
					token = null;
					// 获取token失败
					String wrongMessage = "获取token失败 errcode:{} errmsg:{}"+ jsonObject.getInt("errcode")+ jsonObject.getString("errmsg");
				}
			}
		}
		return token;
	}

	public WeixinAccountEntity findLoginWeixinAccount() {
		TSUser user = ResourceUtil.getSessionUserName();
		List<WeixinAccountEntity> acclst = this.findByProperty(
				WeixinAccountEntity.class, "userName", user.getUserName());
		WeixinAccountEntity weixinAccountEntity = acclst.size() != 0 ? acclst
				.get(0) : null;
		if (weixinAccountEntity != null) {
			return weixinAccountEntity;
		} else {
			weixinAccountEntity = new WeixinAccountEntity();
			// 返回个临时对象，防止空指针
			weixinAccountEntity.setAccountid("-1");
			return weixinAccountEntity;
		}
	}

	public List<WeixinAccountEntity> findByUsername(String username) {
		List<WeixinAccountEntity> acclst = this.findByProperty(
				WeixinAccountEntity.class, "userName", username);
		return acclst;
	}

	/**
	 * 替换sql中的变量
	 * 
	 * @param sql
	 * @return
	 */
	public String replaceVal(String sql, WeixinAccountEntity t) {
		sql = sql.replace("#{id}", String.valueOf(t.getId()));
		sql = sql.replace("#{accountname}", String.valueOf(t.getAccountname()));
		sql = sql.replace("#{accounttoken}",
				String.valueOf(t.getAccounttoken()));
		sql = sql.replace("#{accountnumber}",
				String.valueOf(t.getAccountnumber()));
		sql = sql.replace("#{accounttype}", String.valueOf(t.getAccounttype()));
		sql = sql.replace("#{accountemail}",
				String.valueOf(t.getAccountemail()));
		sql = sql.replace("#{accountdesc}", String.valueOf(t.getAccountdesc()));
		sql = sql.replace("#{accountappid}",
				String.valueOf(t.getAccountappid()));
		sql = sql.replace("#{accountappsecret}",
				String.valueOf(t.getAccountappsecret()));
		sql = sql.replace("#{accountaccesstoken}",
				String.valueOf(t.getAccountaccesstoken()));
		sql = sql.replace("#{addtoekntime}",
				String.valueOf(t.getAddtoekntime()));
		sql = sql.replace("#{UUID}", UUID.randomUUID().toString());
		return sql;
	}
}