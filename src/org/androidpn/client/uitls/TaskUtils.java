package org.androidpn.client.uitls;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

/**
 * 
 * @ClassName: DateUtils
 * @Description: �Ƿ�Ϊǰ̨Ӧ�ù�����
 * @author jin
 * @Company ���ڵ°¼������޹�˾
 * @date 2016��1��7�� ����11:25:41
 *
 */
public class TaskUtils {

	/**
	 * �жϵ�ǰӦ�õ��Ƿ�Ϊǰ̨task
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
