package org.androidpn.client;

import org.androidpn.client.message.MessageManager;
import org.androidpn.client.uitls.NetUtils;
import org.androidpn.demoapp.R;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @ClassName: XmppPush
 * @Description: ��ʼ��
 * @author jin
 * @Company ���ڵ°¼������޹�˾
 * @date 2016��2��17�� ����3:51:45
 *
 */
public class XmppPush {
	private static ServiceManager serviceManager;
	public static final String newUsername = "android01";
	public static final String newPassword = "000000";

	public static ServiceManager initialize(Context context) {
		
		if (serviceManager == null) {
            synchronized (XmppPush.class) {
            	if (serviceManager == null) {
            		if (NetUtils.isNetWorkConnection(context)) {
            			serviceManager = new ServiceManager(context);
            			serviceManager.setNotificationIcon(R.drawable.notification);
            			serviceManager.startService();
            		}
            	}
            }
        }
		return serviceManager;
	}

	public static void onStart() {
		serviceManager.onStart();
	}

	public static void onPause() {

		serviceManager.onPause();
	}

	public static void onDestroy() {

		serviceManager.stopService();
	}
}
