package org.androidpn.client.uitls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 	
  * @ClassName: NetUtils
  * @Description: 网络工具类
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2015年10月21日 上午10:42:50
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
	 * 判断是否联网
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
            	Log.i("NetUtils", i + "===状态===" + networkInfo[i].getState());
            	Log.i("NetUtils", i + "===类型===" + networkInfo[i].getTypeName());
                
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
		return false;
	}
	/**
	 * 判断手机的网络类型
	 * @param context
	 * @return返回网络类型
	 * ConnectivityManager.TYPE_MOBILE  0  手机网络
	 * ConnectivityManager.TYPE_WIFI 1 wifi网络连接
	 * 							     2  代表没有网络连接
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
	 * 获取当前的用户的运营商类型
	 * @param context
	 * @return
	 * 
	 */
	public static int networkOperate(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = tm.getSimOperator();
		if (operator != null) {
			 if (operator.equals("46000") || operator.equals("46002")) {
				    // operatorName="中国移动";
				    return CHINA_MOIBLE;
				    // Toast.makeText(this, "此卡属于(中国移动)",
				    // Toast.LENGTH_SHORT).show();
				   } else if (operator.equals("46001")) {
				    // operatorName="中国联通";
				   return CHINA_UNICON;
				    // Toast.makeText(this, "此卡属于(中国联通)",
				    // Toast.LENGTH_SHORT).show();
				   } else if (operator.equals("46003")) {
				    // operatorName="中国电信";
				  return CHINA_TELECOM;
				    // Toast.makeText(this, "此卡属于(中国电信)",
				    // Toast.LENGTH_SHORT).show();
				   }
		}
		return NO_OPERATE;
	}
	
	
}
