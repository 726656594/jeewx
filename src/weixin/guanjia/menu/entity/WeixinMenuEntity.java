package weixin.guanjia.menu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 微信菜单
 * @author onlineGenerator
 * @date 2014-01-02 22:29:31
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_menu", schema = "")
@SuppressWarnings("serial")
public class WeixinMenuEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**菜单名称*/
	private java.lang.String name;
	/**微信号*/
	private java.lang.String weixinCode;
	/**菜单类别*/
	private java.lang.String category;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	@Id
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  菜单名称
	 */
	@Column(name ="NAME",nullable=true,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  菜单名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  微信号
	 */
	@Column(name ="WEIXIN_CODE",nullable=true,length=100)
	public java.lang.String getWeixinCode(){
		return this.weixinCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  微信号
	 */
	public void setWeixinCode(java.lang.String weixinCode){
		this.weixinCode = weixinCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  菜单类别
	 */
	@Column(name ="CATEGORY",nullable=true,length=100)
	public java.lang.String getCategory(){
		return this.category;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  菜单类别
	 */
	public void setCategory(java.lang.String category){
		this.category = category;
	}
}
