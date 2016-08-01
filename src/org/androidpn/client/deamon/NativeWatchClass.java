package org.androidpn.client.deamon;

import java.util.ArrayList;

import org.androidpn.client.uitls.LogUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;
     
/**
 * 
 * @ClassName: NativeClass
 * @Description: 启用C代码,主要实现保持服务不被杀.
 * @author jin
 * @Company 
 * @date 2016年2月21日 下午2:43:08
 *
 */
public class NativeWatchClass {
	private static final String LOGTAG = LogUtil
	            .makeLogTag(NativeWatchClass.class);
	static {
		System.loadLibrary("monitor");
	}

	private static String PACKAGE = "";
	private String mMonitoredService = "";
	private volatile boolean bHeartBreak = false;
	private Context mContext;
	private boolean mRunning = true;

	public void createAppMonitor(String userId) {
		if (!createWatcher(userId)) {
			Log.e("Watcher", "<<Monitor created failed>>");
		}
	}

	public NativeWatchClass(Context context) {
		mContext = context;	
		PACKAGE = context.getPackageName() +"/";
	}

	private int isServiceRunning() {
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) am
				.getRunningServices(1024);
		for (int i = 0; i < runningService.size(); ++i) {
			if (mMonitoredService.equals(runningService.get(i).service
					.getClassName().toString())) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Native方法，创建一个监视子进程.
	 * 
	 * @param userId
	 *            当前进程的用户ID,子进程重启当前进程时需要用到当前进程的用户ID.
	 * @return 如果子进程创建成功返回true，否则返回false
	 */
	private native boolean createWatcher(String userId);

	/**
	 * Native方法，让当前进程连接到监视进程.
	 * 
	 * @return 连接成功返回true，否则返回false
	 */
	private native boolean connectToMonitor();

	/**
	 * Native方法，向监视进程发送任意信息
	 * 
	 * @param 发给monitor的信息
	 * @return 实际发送的字节
	 */
	private native int sendMsgToMonitor(String msg);
	
	
}
