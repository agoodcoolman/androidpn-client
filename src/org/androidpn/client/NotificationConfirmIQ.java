package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;
/**
 * 
  * @ClassName: NotificationConfirmIQ
  * @Description: send respone message to confirm the message has receive
  * @author jin
  * @Company 
  * @date 2015年10月20日 上午9:33:40
  *
 */
public class NotificationConfirmIQ extends IQ {

	private String uuid;
	
	private String username;
	
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<").append("notification").append(" xmlns=\"").append(
                "androidpn:iq:notificationconfirm").append("\">");
        if (uuid != null) {
            buf.append("<uuid>").append(uuid).append("</uuid>");
        }
        buf.append("</").append("notification").append("> ");
        return buf.toString();
		
	}

	public String getUuid() {
		return uuid;
	}



	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
