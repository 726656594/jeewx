package weixin.guanjia.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weixin.guanjia.base.entity.Subscribe;
import weixin.guanjia.base.service.SubscribeServiceI;
import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;
import weixin.guanjia.menu.entity.MenuEntity;
import weixin.guanjia.message.entity.AutoResponse;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.entity.ReceiveText;
import weixin.guanjia.message.entity.TextTemplate;
import weixin.guanjia.message.service.AutoResponseServiceI;
import weixin.guanjia.message.service.NewsItemServiceI;
import weixin.guanjia.message.service.NewsTemplateServiceI;
import weixin.guanjia.message.service.ReceiveTextServiceI;
import weixin.guanjia.message.service.TextTemplateServiceI;
import weixin.util.DateUtils;

@Service("wechatService")
public class WechatService {
	@Autowired
	private AutoResponseServiceI autoResponseService;
	@Autowired
	private TextTemplateServiceI textTemplateService;
	@Autowired
	private NewsTemplateServiceI newsTemplateService;
	@Autowired
	private ReceiveTextServiceI receiveTextService;
	@Autowired
	private NewsItemServiceI newsItemService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SubscribeServiceI subscribeService;

	public String coreService(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// Map<String, String> requestMap = new HashMap<String, String>();
			// // 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			String msgId = requestMap.get("MsgId");
			// 默认回复此文本消息
			TextMessageResp textMessage = new TextMessageResp();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			// textMessage.setFuncFlag(0);
			// 将文本消息对象转换成xml字符串
			respMessage = MessageUtil.textMessageToXml(textMessage);
			ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String content = requestMap.get("Content");
				// 保存接收到的信息
				ReceiveText receiveText = new ReceiveText();
				receiveText.setContent(content);
				Timestamp temp = Timestamp.valueOf(DateUtils
						.getDate("yyyy-MM-dd HH:mm:ss"));
				receiveText.setCreateTime(temp);
				receiveText.setFromUserName(fromUserName);
				receiveText.setToUserName(toUserName);
				receiveText.setMsgId(msgId);
				receiveText.setMsgType(msgType);
				receiveText.setResponse("0");
				receiveText.setAccountId(toUserName);
				this.receiveTextService.save(receiveText);
				// 判断关键字信息中是否管理该文本内容。有的话优先采用数据库中的回复
				AutoResponse autoResponse = findKey(content, toUserName);
				// 找到关键字
				if (autoResponse != null) {
					String resMsgType = autoResponse.getMsgType();
					if ("text".equals(resMsgType)) {
						TextTemplate textTemplate = this.textTemplateService
								.getEntity(TextTemplate.class, autoResponse
										.getResContent());
						textMessage.setContent(textTemplate.getContent());
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if ("news".equals(resMsgType)) {
						List<NewsItem> newsList = this.newsItemService
								.findByProperty(NewsItem.class,
										"newsTemplate.id", autoResponse
												.getResContent());
						NewsTemplate newsTemplate = newsTemplateService
								.getEntity(NewsTemplate.class, autoResponse
										.getResContent());
						List<Article> articleList = new ArrayList<Article>();
						for (NewsItem news : newsList) {
							Article article = new Article();
							article.setTitle(news.getTitle());
							article.setPicUrl(bundler.getString("domain") + "/"
									+ news.getImagePath());

							String url = "";
							if ("common".equals(newsTemplate.getType())) {
								url = bundler.getString("domain")
										+ "/newsItemController.do?newscontent&id="
										+ news.getId();
							} else {
								url = news.getContent();
							}
							article.setUrl(url);
							article.setDescription(news.getContent());
							articleList.add(article);
						}
						NewsMessageResp newsResp = new NewsMessageResp();
						newsResp.setCreateTime(new Date().getTime());
						newsResp.setFromUserName(toUserName);
						newsResp.setToUserName(fromUserName);
						newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsResp.setArticleCount(newsList.size());
						newsResp.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsResp);
					}
				} else {
					//TODO
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！回复\"?\"进入主菜单。";
					String accountid = requestMap.get("ToUserName");
					List<Subscribe> lst = subscribeService.findByProperty(
							Subscribe.class, "accountId", accountid);
					if (lst.size() != 0) {
						Subscribe subscribe = lst.get(0);
						String type = subscribe.getMsgType();
						if ("text".equals(type)) {
							TextTemplate textTemplate = this.textTemplateService
									.getEntity(TextTemplate.class, subscribe
											.getTemplateId());
							String content = textTemplate.getContent();
							textMessage.setContent(content);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
						} else if ("news".equals(type)) {
							List<NewsItem> newsList = this.newsItemService
									.findByProperty(NewsItem.class,
											"newsTemplate.id", subscribe
													.getTemplateId());
							List<Article> articleList = new ArrayList<Article>();
							NewsTemplate newsTemplate = newsTemplateService
									.getEntity(NewsTemplate.class, subscribe
											.getTemplateId());
							for (NewsItem news : newsList) {
								Article article = new Article();
								article.setTitle(news.getTitle());
								article.setPicUrl(bundler.getString("domain")
										+ "/" + news.getImagePath());
								String url = "";
								if ("commond".equals(newsTemplate.getType())) {
									url = bundler.getString("domain")
											+ "/newsItemController.do?newscontent&id="
											+ news.getId();
								} else {
									url = news.getContent();
								}
								article.setUrl(url);
								article.setDescription(news.getContent());
								articleList.add(article);
							}
							NewsMessageResp newsResp = new NewsMessageResp();
							newsResp.setCreateTime(new Date().getTime());
							newsResp.setFromUserName(toUserName);
							newsResp.setToUserName(fromUserName);
							newsResp
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsResp.setArticleCount(newsList.size());
							newsResp.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsResp);
						}
					}
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					String key = requestMap.get("EventKey");
					// TODO 自定义菜单CLICK类型
					MenuEntity menuEntity = this.systemService
							.findUniqueByProperty(MenuEntity.class, "menuKey",
									key);
					if (menuEntity != null
							&& menuEntity.getTemplateId() != null
							&& !"".equals(menuEntity.getTemplateId())) {
						String type = menuEntity.getMsgType();
						if ("text".equals(type)) {
							TextTemplate textTemplate = this.textTemplateService
									.getEntity(TextTemplate.class, menuEntity
											.getTemplateId());
							String content = textTemplate.getContent();
							textMessage.setContent(content);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
						} else if ("news".equals(type)) {
							List<NewsItem> newsList = this.newsItemService
									.findByProperty(NewsItem.class,
											"newsTemplate.id", menuEntity
													.getTemplateId());
							List<Article> articleList = new ArrayList<Article>();
							NewsTemplate newsTemplate = newsTemplateService
									.getEntity(NewsTemplate.class, menuEntity
											.getTemplateId());
							for (NewsItem news : newsList) {
								Article article = new Article();
								article.setTitle(news.getTitle());
								article.setPicUrl(bundler.getString("domain")
										+ "/" + news.getImagePath());
								String url = "";
								if ("commond".equals(newsTemplate.getType())) {
									url = bundler.getString("domain")
											+ "/newsItemController.do?newscontent&id="
											+ news.getId();
								} else {
									url = news.getContent();
								}
								article.setUrl(url);
								article.setDescription(news.getContent());
								articleList.add(article);
							}
							NewsMessageResp newsResp = new NewsMessageResp();
							newsResp.setCreateTime(new Date().getTime());
							newsResp.setFromUserName(toUserName);
							newsResp.setToUserName(fromUserName);
							newsResp
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsResp.setArticleCount(newsList.size());
							newsResp.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsResp);
						} else if ("expand".equals(type)) {
							//TODO

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}


	/**
	 * Q译通使用指南
	 * 
	 * @return
	 */
	public static String getTranslateUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("微译使用指南").append("\n\n");
		buffer.append("微译为用户提供专业的多语言翻译服务，目前支持以下翻译方向：").append("\n");
		buffer.append("    中 -> 英").append("\n");
		buffer.append("    英 -> 中").append("\n");
		buffer.append("    日 -> 中").append("\n\n");
		buffer.append("使用示例：").append("\n");
		buffer.append("    翻译我是中国人").append("\n");
		buffer.append("    翻译dream").append("\n");
		buffer.append("    翻译さようなら").append("\n\n");
		buffer.append("回复“?”显示主菜单");
		return buffer.toString();
	}

	/**
	 * 遍历关键字管理中是否存在用户输入的关键字信息
	 * 
	 * @param content
	 * @return
	 */
	private AutoResponse findKey(String content, String accountId) {
		// 获取关键字管理的列表，匹配后返回信息
		List<AutoResponse> autoResponses = autoResponseService.findByProperty(
				AutoResponse.class, "accountId", accountId);
		for (AutoResponse r : autoResponses) {
			// 如果包含关键字
			String kw = r.getKeyWord();
			String[] allkw = kw.split(",");
			for (String k : allkw) {
				if (k.equals(content)) {
					return r;
				}
			}
		}
		return null;
	}

}
