package org.androidpn.client.uitls;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

/**
 * 
 * @ClassName: DateUtils
 * @Description: 是否为前台应用工具类
 * @author jin
 * @Company 深圳德奥技术有限公司
 * @date 2016年1月7日 上午11:25:41
 *
 */
public class TaskUtils {

	/**
	 * 判断当前应用的是否为前台task
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAppForgroud(Context context) {
		if (context != null) {
			String packName = context.getPackageName();
			List<RunningTaskInfo> rTasks = getRunningTask(context, 1);
			RunningTaskInfo task = rTasks.get(0);
			return packName.equalsIgnoreCase(task.topActivity.getPackageName());
		}
		return false;
	}

	public static List<RunningTaskInfo> getRunningTask(Context context, int num) {
		if (context != null) {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> rTasks = am.getRunningTasks(1);
			return rTasks;
		}
		return null;
	}
}
