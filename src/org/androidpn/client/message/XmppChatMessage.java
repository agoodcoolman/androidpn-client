package org.androidpn.client.message;

import org.jivesoftware.smack.packet.Message;

/**
 * 聊天使用的消息的类
 * 
 * @ClassName: XmppChatMessage
 * @Description: TODO
 * @author jin
 * @Company 深圳德奥技术有限公司
 * @date 2015年10月21日 下午2:16:31 在asmack中已经封装聊天管理
 * 
 * 
 */
public class XmppChatMessage extends Message {

	public static final String Android_client_01_jid = "android01@127.0.0.1/android";

	public static final String Android_client_02_jid = "android02@127.0.0.1/android";

	public static final String platform_client_03_jid = "platform03@127.0.0.1/platform";

	/**
	 * 
	 * @param from
	 * @param to
	 * @param type
	 * @param body
	 * 
	 *            <message to='romeo@example.net'
	 *            from='juliet@example.com/balcony' type='chat' xml:lang='en'>
	 *            <body>Wherefore art thou, Romeo?</body> </message>
	 */
	public XmppChatMessage(String from, String to, Message.Type type, String body) {
		super(to, type);
		setFrom(from);
		setBody(body);
		setLanguage("en");
	}

}
