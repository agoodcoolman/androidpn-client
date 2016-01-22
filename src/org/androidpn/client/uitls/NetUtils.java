package org.androidpn.client.uitls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 	
  * @ClassName: NetUtils
  * @Description: ���繤����
  * @author jin
  * @Company ���ڵ°¼������޹�˾
  * @date 2015��10��21�� ����10:42:50
  *
 */
public class NetUtils {
	
	public static final int CHINA_MOIBLE = 0x00001;
	public static final int CHINA_UNICON = 0x00002;
	public static final int CHINA_TELECOM = 0x00003;
	public static final int NO_OPERATE = 0x00004;
	
	public static final int TYPE_WIFI = 0x00005;
	public static final int TYPE_MOIBLE = 0x00006;
	public static final int TYPE_ERROR = 0x00007;
	
	/**
	 * �ж��Ƿ�����
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnection(Context context) {
		ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo[] networkInfo = cwjManager.getAllNetworkInfo();
		Log.i("NetUtils", cwjManager.toString());
		
		if (networkInfo != null && networkInfo.length > 0)
        {
            for (int i = 0; i < networkInfo.length; i++)
            {
            	Log.i("NetUtils", i + "===״̬===" + networkInfo[i].getState());
            	Log.i("NetUtils", i + "===����===" + networkInfo[i].getTypeName());
                
                // �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
		return false;
	}
	/**
	 * �ж��ֻ�����������
	 * @param context
	 * @return������������
	 * ConnectivityManager.TYPE_MOBILE  0  �ֻ�����
	 * ConnectivityManager.TYPE_WIFI 1 wifi��������
	 * 							     2  ����û����������
	 */
	public static int networkConnectionType(Context context) {
		ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetworkInfo = cwjManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null) {
			return activeNetworkInfo.getType();
		}
		return 2;
	}
	
	/**
	 * ��ȡ��ǰ���û�����Ӫ������
	 * @param context
	 * @return
	 * 
	 */
	public static int networkOperate(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = tm.getSimOperator();
		if (operator != null) {
			 if (operator.equals("46000") || operator.equals("46002")) {
				    // operatorName="�й��ƶ�";
				    return CHINA_MOIBLE;
				    // Toast.makeText(this, "�˿�����(�й��ƶ�)",
				    // Toast.LENGTH_SHORT).show();
				   } else if (operator.equals("46001")) {
				    // operatorName="�й���ͨ";
				   return CHINA_UNICON;
				    // Toast.makeText(this, "�˿�����(�й���ͨ)",
				    // Toast.LENGTH_SHORT).show();
				   } else if (operator.equals("46003")) {
				    // operatorName="�й�����";
				  return CHINA_TELECOM;
				    // Toast.makeText(this, "�˿�����(�й�����)",
				    // Toast.LENGTH_SHORT).show();
				   }
		}
		return NO_OPERATE;
	}
	
	
}
