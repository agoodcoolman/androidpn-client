package org.androidpn.client.message;

import org.androidpn.client.XmppPush;
import org.jivesoftware.smack.packet.Message;

import android.telephony.gsm.SmsMessage.MessageClass;

/**
 * 
  * @ClassName: MessageManager
  * @Description: 消息管理类
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2016年2月17日 下午4:01:45
  *
 */
public class MessageManager {
	private static MessageManager instance;
	
	
	public XmppVideoMessage getChatMessage(String to,  String body) {
		
		return new XmppVideoMessage(to, XmppVideoMessage.Type.chat, body, XmppPush.upload);
		
	}
	
	/**
     * Returns the singleton instance of MessageManager.
     * 
     * @return the instance
     */
    public static MessageManager getInstance() {
        if (instance == null) {
            synchronized (MessageManager.class) {
            	if (instance == null) {
            		instance = new MessageManager();
            	}
            }
        }
        return instance;
    }
}
