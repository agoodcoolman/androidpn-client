package org.androidpn.client;

import org.androidpn.client.deamon.NativeWatchClass;
import org.androidpn.client.message.MessageManager;
import org.androidpn.client.packetlistener.ConnectLoginSucessListener;
import org.androidpn.client.uitls.NetUtils;
import org.androidpn.demoapp.R;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @ClassName: XmppPush
 * @Description: 初始化
 * @author jin
 * @Company 深圳德奥技术有限公司
 * @date 2016年2月17日 下午3:51:45
 *
 */
public class XmppPush {
	private static ServiceManager serviceManager;
	public static final String newUsername = "android02";
	public static final String newPassword = "000000";
	public static final String upload = "rtmp://192.168.1.103/live/trand";

	public static ServiceManager initialize(final Context context) {

		if (serviceManager == null) {
			synchronized (XmppPush.class) {
				if (serviceManager == null) {
					if (NetUtils.isNetWorkConnection(context)) {
						setListener(context);
						serviceManager = new ServiceManager(context);
						serviceManager
								.setNotificationIcon(R.drawable.notification);

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
	
	
	private static void setListener(final Context context) {
		NotificationService
		.setConnectLoginSucessListener(new ConnectLoginSucessListener() {

			@Override
			public void onLoginSucess() {
				NativeWatchClass nativeWatchClass = new NativeWatchClass(
						context);

				String processName = null;
				int pid = android.os.Process.myPid();
				ActivityManager mActivityManager = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
						.getRunningAppProcesses()) {
					if (appProcess.pid == pid) {
						processName = appProcess.processName;
						break;
					}
				}
				Log.e("tag", String.format(
						"当前进程名为：%s进程pid为：%s",
						processName, pid + ""));

				nativeWatchClass.createAppMonitor(pid
						+ "");

			}
		});
	}
}
