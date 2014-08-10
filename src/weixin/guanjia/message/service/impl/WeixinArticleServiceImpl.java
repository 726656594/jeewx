package weixin.guanjia.message.service.impl;

import java.io.Serializable;
import java.util.UUID;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.message.entity.WeixinArticleEntity;
import weixin.guanjia.message.service.WeixinArticleServiceI;

@Service("weixinArticleService")
@Transactional
public class WeixinArticleServiceImpl extends CommonServiceImpl implements WeixinArticleServiceI {
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Override
 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((WeixinArticleEntity)entity);
 	}
 	@Override
 	public <T> Serializable save(T entity) {
 		((WeixinArticleEntity)entity).setAccountId(ResourceUtil.getWeiXinAccount().getAccountid());
 		Serializable t = super.save(entity);
 		//执行新增操作配置的sql增强
 		this.doAddSql((WeixinArticleEntity)entity);
 		return t;
 	}
 	@Override
 	public <T> void saveOrUpdate(T entity) {
 		super.saveOrUpdate(entity);
 		//执行更新操作配置的sql增强
 		this.doUpdateSql((WeixinArticleEntity)entity);
 	}
 	
 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param id
	 * @return
	 */
 	public boolean doAddSql(WeixinArticleEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param id
	 * @return
	 */
 	public boolean doUpdateSql(WeixinArticleEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param id
	 * @return
	 */
 	public boolean doDelSql(WeixinArticleEntity t){
	 	return true;
 	}
 	
 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,WeixinArticleEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{wx_title}",String.valueOf(t.getWxTitle()));
 		sql  = sql.replace("#{wx_author}",String.valueOf(t.getWxAuthor()));
 		sql  = sql.replace("#{wx_pic_url}",String.valueOf(t.getWxPicUrl()));
 		sql  = sql.replace("#{wx_summary}",String.valueOf(t.getWxSummary()));
 		sql  = sql.replace("#{wx_content}",String.valueOf(t.getWxContent()));
 		sql  = sql.replace("#{wx_origin_url}",String.valueOf(t.getWxOriginUrl()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}