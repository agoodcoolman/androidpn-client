package org.androidpn.client.uitls;

import java.util.Calendar;

/**
 * 
  * @ClassName: DateUtils
  * @Description: ���ڹ���
  * @author jin
  * @Company ���ڵ°¼������޹�˾
  * @date 2016��1��7�� ����6:58:11
  *
 */
public class DateUtils {
	/**
	 * �жϵ�����һ���е����ڼ�
	 * 1  ����
	 * 2  ��һ
	 * 3  �ܶ�
	 * .....
	 * @return
	 */
	public static int getDay_OF_WEEK() {
		Calendar instance = Calendar.getInstance();
		return instance.get(Calendar.DAY_OF_WEEK);
	}
}
