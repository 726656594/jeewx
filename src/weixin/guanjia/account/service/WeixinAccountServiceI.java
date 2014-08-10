package weixin.guanjia.account.service;

import org.jeecgframework.core.common.service.CommonService;

import weixin.guanjia.account.entity.WeixinAccountEntity;

import java.io.Serializable;
import java.util.List;

public interface WeixinAccountServiceI extends CommonService{
 	public <T> void delete(T entity);
 	public <T> Serializable save(T entity);
 	public <T> void saveOrUpdate(T entity);
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(WeixinAccountEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(WeixinAccountEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(WeixinAccountEntity t);
 	
 	public String getAccessToken();
 	public WeixinAccountEntity findLoginWeixinAccount();
 	public List<WeixinAccountEntity> findByUsername(String username);
}
