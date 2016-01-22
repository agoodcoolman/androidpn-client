package org.androidpn.client.uitls;

import java.util.Calendar;

/**
 * 
  * @ClassName: DateUtils
  * @Description: 日期工具
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2016年1月7日 下午6:58:11
  *
 */
public class DateUtils {
	/**
	 * 判断当天是一周中的星期几
	 * 1  周日
	 * 2  周一
	 * 3  周二
	 * .....
	 * @return
	 */
	public static int getDay_OF_WEEK() {
		Calendar instance = Calendar.getInstance();
		return instance.get(Calendar.DAY_OF_WEEK);
	}
}
