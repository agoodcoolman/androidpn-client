package org.androidpn.client.heart;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.androidpn.client.Constants;
import org.androidpn.client.XmppManager;
import org.androidpn.client.uitls.LogUtil;
import org.androidpn.client.uitls.NetUtils;
import org.androidpn.client.uitls.StateRecoder;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.PingSucessListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 
  * @ClassName: HeartManager
  * @Description: 心跳包的管理
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2015年12月30日 下午2:04:41
  * 
  * 长连接+心跳策略（在Galaxy S3上使用），心跳间隔WIFI下是3分20秒，手机网络是7分钟。
  *
 */
public class HeartManager implements PingFailedListener, PingSucessListener{
	
	 private static final String LOGTAG = LogUtil
	            .makeLogTag(HeartManager.class);
	
	
	private final int minFixHeart = 5000;// 最小的固定心跳包,用于在前台使用.
	private final int ATTEMPT_MIN_COUNT = 5; // 最小次数
	private int pingFaildCount = 0;
	private int pingSucessCount = 0;
	private int subType = 0;
	private int pingTaskCount = 0; // 使用ServerPingTask 进行心跳的
	private int MinHeart = 5000, MaxHeart = 0; // 心跳可选区间
	private int sucessHeart = MinHeart;// 当前成功心跳,初始化的值为MinHeart
	private final int heartStep = 5000;// 心跳增加步长 ,在测试期使用
	private final int sucessStep = 2000; // 稳定期的探测步长
	private int currentHeart ; // 当前心跳初始值为successHeart
	private final int NAT_MOIBLE = 5* 60 * 1000; // 移动NAT 老化时间
	private final int NAT_UNION = 5* 60 * 1000; // 联通NAT 老化时间
	private final int NAT_TELECOM = 28 * 60 * 1000; // 电信NAT 老化时间
	private int connType; // 连接类型
	private int simOperate ;// 手机卡运营商 ,具体类型常亮在 NetUtils中
	private XmppManager xmppManager;

	private PingManager pingManager;
	
	private static HeartManager heartManger = null;

	

	private Timer calculateTimer; // 测量heart 长度使用的时间器
	
	private StateRecoder recoder = new StateRecoder();
	
	
	private HeartManager (XmppManager xmppManager) {
		this.xmppManager = xmppManager;
		pingManager = PingManager.getInstanceFor(xmppManager.getConnection());
		pingManager.registerPingFailedListener(this);
		pingManager.registerPingSucessListener(this);
	}

	
	public static HeartManager getInstance (XmppManager xmppManager, int connType, int sim_operate) {
		
		if (heartManger == null) {
			synchronized (HeartManager.class) {
				if (heartManger == null)
					heartManger = new HeartManager(xmppManager);
			}
		}
		heartManger.setSimOperate(sim_operate);
		heartManger.setConnType(connType);
		return heartManger;
	}

	public int getConnType() {
		return connType;
	}

	public void setConnType(int connType) {
		// 按照网络类型,已经自动 给定了最大的时间间隔
		this.connType = connType;
		switch (connType) {
		// 根据网络类型进行判断,当前在sp中存贮的最大的
		case NetUtils.TYPE_WIFI: // WIFI 连接下的最大心跳间隔
			MaxHeart = getSPMaxHeart(Constants.WIFI, connType);
			break;
		case NetUtils.TYPE_MOIBLE:
			if (simOperate == NetUtils.CHINA_MOIBLE) // 移动NAT 最大老化时间
				MaxHeart = getSPMaxHeart(Constants.CHINA_MOIBLE, connType);
			else if (simOperate == NetUtils.CHINA_TELECOM) // 电信
				MaxHeart = getSPMaxHeart(Constants.CHINA_TELECOM, connType);
			else if (simOperate == NetUtils.CHINA_UNICON) // 联通
				MaxHeart = getSPMaxHeart(Constants.CHINA_UNICON, connType);
			break;
		case NetUtils.TYPE_ERROR:
			currentHeart = minFixHeart;
			break;
		}
	}
	
	public int getSimOperate() {
		return simOperate;
	}


	public void setSimOperate(int simOperate) {
		this.simOperate = simOperate;
	}


	// 测算最优的后台心跳包,最大步长测算,每个星期就行测算.现在环境已经正常.前面已经测试好才有这步.
	public void calculateBestHeart () {
		// MaxHeart 已经按照网络类型给定了最大的网络间隔
		currentHeart = MaxHeart;
		calculateTimer = new Timer();
		final TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// pingMyServer 里面的时间是表示的服务器的响应时间.
				boolean pingMyServer = pingManager.pingMyServer();
				if (pingMyServer) { // 成功
					recoder.setPreSucess(true);// 只要成功了一次就一定有成功心跳,失败后根据判断是否有成功心跳,然后使用成功的心跳进行认定为
					recoder.sucessincrenment1();
					if (recoder.isPreFaild()) { //
						
						MaxHeart = currentHeart;
						sucessHeart = currentHeart;
						 // 保存到sp中吧.
						// 如果上次失败了.这次成功,代表测算结束,就在当前的时间间隔下进行心跳就好了.
							// 清空所有的相关的失败数据
						// 如如果没有失败过,那么就使用延时探测步骤,进行心跳时间的探测
						
						
					} else { // 
						calculateTimer.cancel();
						calculateTimer.purge();
						sucessHeart = currentHeart;
						currentHeart -= heartStep;
						calculateTimer = null;
						
						calculateBestHeart ();
					}
				} else { // 失败ping服务器失败
					// 这里是将数字加1
					int andIncrement = recoder.faildIncrenment1();
					if (andIncrement > 5) {
						// 这里等于失败,失败之后要断开连接,并且重新连接,然后在进行测算.
						// 失败认定,那么就是使用上次成功的心跳包进行测试.
						calculateTimer.cancel();
						calculateTimer.purge();
						calculateTimer = null;
						if (recoder.isPreSucess()) {
							// 判断是否成功过.
							  //成功过,这里就直接使用成功的心跳时间间隔进行心跳
							
							  //没有成功过,那么这里就重
						} 
						currentHeart = MaxHeart - heartStep;
						MaxHeart = MaxHeart - heartStep;
						calculateTimer = null;
						recoder.clear();
						
						
						calculateBestHeart ();
					}
						
				}
				
			}
		};
		calculateTimer.schedule(timerTask, currentHeart, currentHeart);
		//     成功: sucessHeart = currentHeat; currentHeart += heartStep;
		
		
					
		//     失败: 失败次数如果大于5,那么就结束
		
		// 测算出来的稳定值稍小一点的值,作为后台的稳定心跳值.
	}
	
	// 后台稳定心跳,动态调整测略
	private void backgroudStableHeart() {
		// 稳定心跳
		
		// 前台稳定心跳
		
		// 后台稳定心跳
		
		// 无网络、网络时好时坏、偶然失败、NAT超时变小在后台稳定期发生心跳发生失败后，我们使用延迟心跳测试法测试五次。
		// 如果有一次成功，则保持当前心跳值不变；如果五次测试全失败，重新计算合理心跳值。该过程如图4-4所示，有一点需要注意，
		// 每个新建的长连接需要先用短心跳成功维持3次后才用successHeart进行心跳。
		
	}
	
	
	
	// 前台活跃的固定的心跳包
	public void fixPing(int longTime) {
		// 取范围中间的,如果超过就取边界值
		longTime = Math.min(Math.max(longTime, MinHeart), MaxHeart);
		currentHeart = longTime;
		pingManager.maybeScedulePingServerTask(longTime/1000);
	}
	
	// 发送三次短心跳包,保证下次测试环境的正常.三次连续返回true表示三次短心跳成功, 环境稳定
	public boolean send3Ping() { // default 5s
		
		boolean pingMyServer = pingManager.pingMyServer();
			
		if (pingMyServer) {
			pingFaildCount = 0;
			pingSucessCount ++;
			
			if (pingSucessCount > 2) // 三次成功
				pingManager.maybeScedulePingServerTask(sucessHeart - sucessStep); 
			else 
				send3Ping();
			pingSucessCount = 0; // 使用完毕清空
			return true;
		} else {
			pingFaildCount ++;
			pingSucessCount = 0;
			if(pingFaildCount > 4) 
				// 连续5次失败.才算失败
				startReconnectionThread();
			else 
				send3Ping();
			pingFaildCount = 0; //使用完毕清空
			return false;
		}
		// 这里在pingManageTask中,成功了.自动继续下一个Task任务 ,利用三次连续短心跳.
		
	}
	
	// 应用程序在前台时
    public void frontTaskActivity() {
    	Log.i(LOGTAG, "HeartManager frontTaskActivity...");
    	// 进行3次短心跳前isPing3Count = true; 设置为true了.
    	
    	
    }
    // 应用程序在后台时候
    public void backgroundTaskActivity() {
    	Log.i(LOGTAG, "HeartManager backgroundTaskActivity...");
    	
    }
	
    public void stopHeart() {
    	// pingmanage已经监控了连接状态,将task任务停止了.这个是否取消不重要.
    	/*pingManager.unregisterPingFailedListener(this);
    	pingManager.unregisterPingSucessListener(this);*/
    	
    }
    
    /**
     * 重新连接.需要进行心跳包时间的调整.
     */
    private void startReconnectionThread () {
    	xmppManager.startReconnectionThread();
    }
     
	// 失败才需要计数,成功不需要计数.
	@Override
	public void pingFailed() {
		Log.i(LOGTAG, "HeartManager pingFailed...");
		
			// 尝试5次,未到5次,使用上一次的当前心跳进行
		
	}


	@Override
	public void pingSucess() {
		Log.i(LOGTAG, "HeartManager pingSucess...");
		// 成功了.
		 // 成功了,将当前心跳给成功心跳
		
	}
	
	/**
	 * 保存最大的心跳时间到sp中。存到当前的网络类型下.
	 * @param key
	 * @param connType
	 */
	private void saveSpMaxHeart(String key, int connType) {
		SharedPreferences sharedPreferences = xmppManager.getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int resultInt = sharedPreferences.getInt(key, -1);
		
	}
	
	/**
	 * 获取sp中存的时间.获得当前网络类型下的最大的心跳时间间隔
	 * @param key   eg:Constants.WIFI
	 * @param connType
	 * @return
	 * 
	 * 
	 * 如果用户进行换号,换网络了,这个时间好像就不能用了.
	 */
	private int getSPMaxHeart (String key, int connType) {
		SharedPreferences sharedPreferences = xmppManager.getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int resultInt = sharedPreferences.getInt(key, -1);// 获取当前网络类型下的最大时间.
		
		if (resultInt == -1) {
			switch (connType) {
			// 根据网络类型进行判断
			case NetUtils.TYPE_WIFI: // WIFI 连接下的最大心跳间隔
				resultInt = 7 * 60 * 1000;
				break;
			case NetUtils.TYPE_MOIBLE:
				if (simOperate == NetUtils.CHINA_MOIBLE) // 移动NAT 最大老化时间
					resultInt = (5 * 60)*1000;
				else if (simOperate == NetUtils.CHINA_TELECOM) // 电信
					resultInt = (15 * 60) * 1000;
				else if (simOperate == NetUtils.CHINA_UNICON) // 联通
					resultInt = (5 * 60)*1000;
				break;
			case NetUtils.TYPE_ERROR: // 没有网络连接吧~~
				// TODO 待处理
				currentHeart = minFixHeart;
				break;
			}
		} 
		return resultInt;
	}
	
	
} 
