package weixin.guanjia.message.service.impl;

import java.io.Serializable;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.message.entity.AutoResponse;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.entity.ReceiveText;
import weixin.guanjia.message.service.ReceiveTextServiceI;

@Service("receiveTextService")
@Transactional
public class ReceiveTextServiceImpl extends CommonServiceImpl implements ReceiveTextServiceI {
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Override
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((ReceiveText)entity);
 	}
 	@Override
 	public <T> Serializable save(T entity) {
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((ReceiveText)entity);
 		return t;
 	}
 	@Override
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((ReceiveText)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(ReceiveText t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(ReceiveText t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(ReceiveText t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
// 	public String replaceVal(String sql,ReceiveText t){
// 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
// 		sql  = sql.replace("#{accountname}",String.valueOf(t.getAccountname()));
// 		sql  = sql.replace("#{accounttoken}",String.valueOf(t.getAccounttoken()));
// 		sql  = sql.replace("#{accountnumber}",String.valueOf(t.getAccountnumber()));
// 		sql  = sql.replace("#{accounttype}",String.valueOf(t.getAccounttype()));
// 		sql  = sql.replace("#{accountemail}",String.valueOf(t.getAccountemail()));
// 		sql  = sql.replace("#{accountdesc}",String.valueOf(t.getAccountdesc()));
// 		sql  = sql.replace("#{accountappid}",String.valueOf(t.getAccountappid()));
// 		sql  = sql.replace("#{accountappsecret}",String.valueOf(t.getAccountappsecret()));
// 		sql  = sql.replace("#{accountaccesstoken}",String.valueOf(t.getAccountaccesstoken()));
// 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
// 		return sql;
// 	}
}