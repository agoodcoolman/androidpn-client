package org.androidpn.client.packetlistener;

import org.androidpn.client.XmppManager;
import org.androidpn.client.uitls.LogUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;
/**
 *  process message /解析用户之间聊天消息,收用户消息,当用户收到message的消息回调这个接口
  * @ClassName: MessagePacketListener
  * @Description: TODO
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2015年10月22日 上午11:13:32
  *
 */
public class MessagePacketListener implements PacketListener {
	 private static final String LOGTAG = LogUtil
	            .makeLogTag(NotificationPacketListener.class);

	    private final XmppManager xmppManager;

	    public MessagePacketListener(XmppManager xmppManager) {
	        this.xmppManager = xmppManager;
	    }
	@Override
	public void processPacket(Packet packet) {
		Log.i("MessagePacketListener.PacketListener",
                "processPacket()....."); 
        Log.i("MessagePacketListener.PacketListener", "packet="
                + packet.toXML());
        Message message = (Message)packet;
        String body = message.getBody();
		if (packet != null && packet instanceof Message) {
			// 处理消息接收
			Log.i(LOGTAG, "收到的消息"+packet.toString());
			// 然后给服务器发送消息回执
			Message reply = new Message();
			reply.setDefaultXmlns("jabber:message:receptconfirm");
			reply.setPacketID(message.getPacketID());
			xmppManager.getConnection().sendPacket(reply);
		}
		
	}

}
