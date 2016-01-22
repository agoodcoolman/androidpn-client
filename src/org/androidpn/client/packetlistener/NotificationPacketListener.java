/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client.packetlistener;

import org.androidpn.client.Constants;
import org.androidpn.client.NotificationConfirmIQ;
import org.androidpn.client.NotificationIQ;
import org.androidpn.client.XmppManager;
import org.androidpn.client.uitls.LogUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.util.Log;

/**
 * This class notifies the receiver of incoming notifcation packets asynchronously.  
 * 通知消息监听器，在连接后被注册，由asmack库调用，当NotificationIQProvider接收
 * 到服务器消息后，会调用监听器的回调方法 processPacket，再发送广播给NotificationReceiver
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationPacketListener.class);

    private final XmppManager xmppManager;

    public NotificationPacketListener(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
    }

    @Override
    public void processPacket(Packet packet) {
        Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
        Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());
        // IQ(info/query)
        if (packet instanceof NotificationIQ) {
            NotificationIQ notification = (NotificationIQ) packet;

            if (notification.getChildElementXML().contains(
                    "androidpn:iq:notification")) {
                String notificationId = notification.getId();
                String notificationApiKey = notification.getApiKey();
                String notificationTitle = notification.getTitle();
                String notificationMessage = notification.getMessage();
                //                String notificationTicker = notification.getTicker();
                String notificationUri = notification.getUri();
                
                
//                <iq type="set" id="47-0">
//                <notification xmlns="androidpn:iq:notification">
//                  <id>2e0c3f73</id>
//                  <apiKey>1234567890</apiKey>
//                  <title>Dokdo Island</title>
//                  <message>Dokdo is a Korean island, the far east of the Korean territory. No doubt! No question! Don't mention it any more!</message>
//                  <uri></uri>
//                </notification>
//              </iq>
                Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
                intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
                intent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
                intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
                intent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
                intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);
                //                intent.setData(Uri.parse((new StringBuilder(
                //                        "notif://notification.androidpn.org/")).append(
                //                        notificationApiKey).append("/").append(
                //                        System.currentTimeMillis()).toString()));
                //sendBroadcast 发送广播
                xmppManager.getContext().sendBroadcast(intent);
                
                // new a confirm message ,and send to server
                NotificationConfirmIQ notificationConfirmIQ = new NotificationConfirmIQ();
                notificationConfirmIQ.setUuid(notificationId);
                notification.setType(IQ.Type.SET);
                notification.toXML();
                
                xmppManager.getConnection().sendPacket(notificationConfirmIQ);
            }
        }

    }

}
