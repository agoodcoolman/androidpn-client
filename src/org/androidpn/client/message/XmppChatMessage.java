package org.androidpn.client.message;

import org.jivesoftware.smack.packet.Message;

/**
 * ����ʹ�õ���Ϣ����
 * 
 * @ClassName: XmppChatMessage
 * @Description: TODO
 * @author jin
 * @Company ���ڵ°¼������޹�˾
 * @date 2015��10��21�� ����2:16:31 ��asmack���Ѿ���װ�������
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
