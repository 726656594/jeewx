package weixin.guanjia.message.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 微信单图消息
 * @author onlineGenerator
 * @date 2014-01-09 21:55:30
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_article", schema = "")
@SuppressWarnings("serial")
public class WeixinArticleEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**标题*/
	private java.lang.String wxTitle;
	/**作者*/
	private java.lang.String wxAuthor;
	/**图片链接*/
	private java.lang.String wxPicUrl;
	/**摘要*/
	private java.lang.String wxSummary;
	/**正文*/
	private java.lang.String wxContent;
	/**原文链接*/
	private java.lang.String wxOriginUrl;
	/**创建时间*/
	private Date createDate;
	private String accountId;

	@Column(name = "accountid",length=100)
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
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
	 *@return: java.lang.String  标题
	 */
	@Column(name ="WX_TITLE",nullable=true,length=255)
	public java.lang.String getWxTitle(){
		return this.wxTitle;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标题
	 */
	public void setWxTitle(java.lang.String wxTitle){
		this.wxTitle = wxTitle;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作者
	 */
	@Column(name ="WX_AUTHOR",nullable=true,length=255)
	public java.lang.String getWxAuthor(){
		return this.wxAuthor;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作者
	 */
	public void setWxAuthor(java.lang.String wxAuthor){
		this.wxAuthor = wxAuthor;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  图片链接
	 */
	@Column(name ="WX_PIC_URL",nullable=true,length=255)
	public java.lang.String getWxPicUrl(){
		return this.wxPicUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  图片链接
	 */
	public void setWxPicUrl(java.lang.String wxPicUrl){
		this.wxPicUrl = wxPicUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="WX_SUMMARY",nullable=true,length=255)
	public java.lang.String getWxSummary(){
		return this.wxSummary;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setWxSummary(java.lang.String wxSummary){
		this.wxSummary = wxSummary;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  正文
	 */
	@Column(name ="WX_CONTENT",nullable=true,length=255)
	public java.lang.String getWxContent(){
		return this.wxContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  正文
	 */
	public void setWxContent(java.lang.String wxContent){
		this.wxContent = wxContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原文链接
	 */
	@Column(name ="WX_ORIGIN_URL",nullable=true,length=255)
	public java.lang.String getWxOriginUrl(){
		return this.wxOriginUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原文链接
	 */
	public void setWxOriginUrl(java.lang.String wxOriginUrl){
		this.wxOriginUrl = wxOriginUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建时间
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建时间
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
}
