package org.androidpn.client.packetlistener;

import org.androidpn.client.XmppManager;
import org.androidpn.client.uitls.LogUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;
/**
 *  process message /�����û�֮��������Ϣ,���û���Ϣ,���û��յ�message����Ϣ�ص�����ӿ�
  * @ClassName: MessagePacketListener
  * @Description: TODO
  * @author jin
  * @Company ���ڵ°¼������޹�˾
  * @date 2015��10��22�� ����11:13:32
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
			// ������Ϣ����
			Log.i(LOGTAG, "�յ�����Ϣ"+packet.toString());
			// Ȼ���������������Ϣ��ִ
			Message reply = new Message();
			reply.setDefaultXmlns("jabber:message:receptconfirm");
			reply.setPacketID(message.getPacketID());
			xmppManager.getConnection().sendPacket(reply);
		}
		
	}

}
