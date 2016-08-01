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
 * @Description: ����C����,��Ҫʵ�ֱ��ַ��񲻱�ɱ.
 * @author jin
 * @Company 
 * @date 2016��2��21�� ����2:43:08
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
	 * Native����������һ�������ӽ���.
	 * 
	 * @param userId
	 *            ��ǰ���̵��û�ID,�ӽ���������ǰ����ʱ��Ҫ�õ���ǰ���̵��û�ID.
	 * @return ����ӽ��̴����ɹ�����true�����򷵻�false
	 */
	private native boolean createWatcher(String userId);

	/**
	 * Native�������õ�ǰ�������ӵ����ӽ���.
	 * 
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	private native boolean connectToMonitor();

	/**
	 * Native����������ӽ��̷���������Ϣ
	 * 
	 * @param ����monitor����Ϣ
	 * @return ʵ�ʷ��͵��ֽ�
	 */
	private native int sendMsgToMonitor(String msg);
	
	
}
