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
  * @Description: �������Ĺ���
  * @author jin
  * @Company ���ڵ°¼������޹�˾
  * @date 2015��12��30�� ����2:04:41
  * 
  * ������+�������ԣ���Galaxy S3��ʹ�ã����������WIFI����3��20�룬�ֻ�������7���ӡ�
  *
 */
public class HeartManager implements PingFailedListener, PingSucessListener{
	
	 private static final String LOGTAG = LogUtil
	            .makeLogTag(HeartManager.class);
	
	
	private final int minFixHeart = 5000;// ��С�Ĺ̶�������,������ǰ̨ʹ��.
	private final int ATTEMPT_MIN_COUNT = 5; // ��С����
	private int pingFaildCount = 0;
	private int pingSucessCount = 0;
	private int subType = 0;
	private int pingTaskCount = 0; // ʹ��ServerPingTask ����������
	private int MinHeart = 5000, MaxHeart = 0; // ������ѡ����
	private int sucessHeart = MinHeart;// ��ǰ�ɹ�����,��ʼ����ֵΪMinHeart
	private final int heartStep = 5000;// �������Ӳ��� ,�ڲ�����ʹ��
	private final int sucessStep = 2000; // �ȶ��ڵ�̽�ⲽ��
	private int currentHeart ; // ��ǰ������ʼֵΪsuccessHeart
	private final int NAT_MOIBLE = 5* 60 * 1000; // �ƶ�NAT �ϻ�ʱ��
	private final int NAT_UNION = 5* 60 * 1000; // ��ͨNAT �ϻ�ʱ��
	private final int NAT_TELECOM = 28 * 60 * 1000; // ����NAT �ϻ�ʱ��
	private int connType; // ��������
	private int simOperate ;// �ֻ�����Ӫ�� ,�������ͳ����� NetUtils��
	private XmppManager xmppManager;

	private PingManager pingManager;
	
	private static HeartManager heartManger = null;

	

	private Timer calculateTimer; // ����heart ����ʹ�õ�ʱ����
	
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
		// ������������,�Ѿ��Զ� ����������ʱ����
		this.connType = connType;
		switch (connType) {
		// �����������ͽ����ж�,��ǰ��sp�д���������
		case NetUtils.TYPE_WIFI: // WIFI �����µ�����������
			MaxHeart = getSPMaxHeart(Constants.WIFI, connType);
			break;
		case NetUtils.TYPE_MOIBLE:
			if (simOperate == NetUtils.CHINA_MOIBLE) // �ƶ�NAT ����ϻ�ʱ��
				MaxHeart = getSPMaxHeart(Constants.CHINA_MOIBLE, connType);
			else if (simOperate == NetUtils.CHINA_TELECOM) // ����
				MaxHeart = getSPMaxHeart(Constants.CHINA_TELECOM, connType);
			else if (simOperate == NetUtils.CHINA_UNICON) // ��ͨ
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


	// �������ŵĺ�̨������,��󲽳�����,ÿ�����ھ��в���.���ڻ����Ѿ�����.ǰ���Ѿ����Ժò����ⲽ.
	public void calculateBestHeart () {
		// MaxHeart �Ѿ������������͸���������������
		currentHeart = MaxHeart;
		calculateTimer = new Timer();
		final TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				// pingMyServer �����ʱ���Ǳ�ʾ�ķ���������Ӧʱ��.
				boolean pingMyServer = pingManager.pingMyServer();
				if (pingMyServer) { // �ɹ�
					recoder.setPreSucess(true);// ֻҪ�ɹ���һ�ξ�һ���гɹ�����,ʧ�ܺ�����ж��Ƿ��гɹ�����,Ȼ��ʹ�óɹ������������϶�Ϊ
					recoder.sucessincrenment1();
					if (recoder.isPreFaild()) { //
						
						MaxHeart = currentHeart;
						sucessHeart = currentHeart;
						 // ���浽sp�а�.
						// ����ϴ�ʧ����.��γɹ�,����������,���ڵ�ǰ��ʱ�����½��������ͺ���.
							// ������е���ص�ʧ������
						// �����û��ʧ�ܹ�,��ô��ʹ����ʱ̽�ⲽ��,��������ʱ���̽��
						
						
					} else { // 
						calculateTimer.cancel();
						calculateTimer.purge();
						sucessHeart = currentHeart;
						currentHeart -= heartStep;
						calculateTimer = null;
						
						calculateBestHeart ();
					}
				} else { // ʧ��ping������ʧ��
					// �����ǽ����ּ�1
					int andIncrement = recoder.faildIncrenment1();
					if (andIncrement > 5) {
						// �������ʧ��,ʧ��֮��Ҫ�Ͽ�����,������������,Ȼ���ڽ��в���.
						// ʧ���϶�,��ô����ʹ���ϴγɹ������������в���.
						calculateTimer.cancel();
						calculateTimer.purge();
						calculateTimer = null;
						if (recoder.isPreSucess()) {
							// �ж��Ƿ�ɹ���.
							  //�ɹ���,�����ֱ��ʹ�óɹ�������ʱ������������
							
							  //û�гɹ���,��ô�������
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
		//     �ɹ�: sucessHeart = currentHeat; currentHeart += heartStep;
		
		
					
		//     ʧ��: ʧ�ܴ����������5,��ô�ͽ���
		
		// ����������ȶ�ֵ��Сһ���ֵ,��Ϊ��̨���ȶ�����ֵ.
	}
	
	// ��̨�ȶ�����,��̬��������
	private void backgroudStableHeart() {
		// �ȶ�����
		
		// ǰ̨�ȶ�����
		
		// ��̨�ȶ�����
		
		// �����硢����ʱ��ʱ����żȻʧ�ܡ�NAT��ʱ��С�ں�̨�ȶ��ڷ�����������ʧ�ܺ�����ʹ���ӳ��������Է�������Ρ�
		// �����һ�γɹ����򱣳ֵ�ǰ����ֵ���䣻�����β���ȫʧ�ܣ����¼����������ֵ���ù�����ͼ4-4��ʾ����һ����Ҫע�⣬
		// ÿ���½��ĳ�������Ҫ���ö������ɹ�ά��3�κ����successHeart����������
		
	}
	
	
	
	// ǰ̨��Ծ�Ĺ̶���������
	public void fixPing(int longTime) {
		// ȡ��Χ�м��,���������ȡ�߽�ֵ
		longTime = Math.min(Math.max(longTime, MinHeart), MaxHeart);
		currentHeart = longTime;
		pingManager.maybeScedulePingServerTask(longTime/1000);
	}
	
	// �������ζ�������,��֤�´β��Ի���������.������������true��ʾ���ζ������ɹ�, �����ȶ�
	public boolean send3Ping() { // default 5s
		
		boolean pingMyServer = pingManager.pingMyServer();
			
		if (pingMyServer) {
			pingFaildCount = 0;
			pingSucessCount ++;
			
			if (pingSucessCount > 2) // ���γɹ�
				pingManager.maybeScedulePingServerTask(sucessHeart - sucessStep); 
			else 
				send3Ping();
			pingSucessCount = 0; // ʹ��������
			return true;
		} else {
			pingFaildCount ++;
			pingSucessCount = 0;
			if(pingFaildCount > 4) 
				// ����5��ʧ��.����ʧ��
				startReconnectionThread();
			else 
				send3Ping();
			pingFaildCount = 0; //ʹ��������
			return false;
		}
		// ������pingManageTask��,�ɹ���.�Զ�������һ��Task���� ,������������������.
		
	}
	
	// Ӧ�ó�����ǰ̨ʱ
    public void frontTaskActivity() {
    	Log.i(LOGTAG, "HeartManager frontTaskActivity...");
    	// ����3�ζ�����ǰisPing3Count = true; ����Ϊtrue��.
    	
    	
    }
    // Ӧ�ó����ں�̨ʱ��
    public void backgroundTaskActivity() {
    	Log.i(LOGTAG, "HeartManager backgroundTaskActivity...");
    	
    }
	
    public void stopHeart() {
    	// pingmanage�Ѿ����������״̬,��task����ֹͣ��.����Ƿ�ȡ������Ҫ.
    	/*pingManager.unregisterPingFailedListener(this);
    	pingManager.unregisterPingSucessListener(this);*/
    	
    }
    
    /**
     * ��������.��Ҫ����������ʱ��ĵ���.
     */
    private void startReconnectionThread () {
    	xmppManager.startReconnectionThread();
    }
     
	// ʧ�ܲ���Ҫ����,�ɹ�����Ҫ����.
	@Override
	public void pingFailed() {
		Log.i(LOGTAG, "HeartManager pingFailed...");
		
			// ����5��,δ��5��,ʹ����һ�εĵ�ǰ��������
		
	}


	@Override
	public void pingSucess() {
		Log.i(LOGTAG, "HeartManager pingSucess...");
		// �ɹ���.
		 // �ɹ���,����ǰ�������ɹ�����
		
	}
	
	/**
	 * ������������ʱ�䵽sp�С��浽��ǰ������������.
	 * @param key
	 * @param connType
	 */
	private void saveSpMaxHeart(String key, int connType) {
		SharedPreferences sharedPreferences = xmppManager.getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int resultInt = sharedPreferences.getInt(key, -1);
		
	}
	
	/**
	 * ��ȡsp�д��ʱ��.��õ�ǰ���������µ���������ʱ����
	 * @param key   eg:Constants.WIFI
	 * @param connType
	 * @return
	 * 
	 * 
	 * ����û����л���,��������,���ʱ�����Ͳ�������.
	 */
	private int getSPMaxHeart (String key, int connType) {
		SharedPreferences sharedPreferences = xmppManager.getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int resultInt = sharedPreferences.getInt(key, -1);// ��ȡ��ǰ���������µ����ʱ��.
		
		if (resultInt == -1) {
			switch (connType) {
			// �����������ͽ����ж�
			case NetUtils.TYPE_WIFI: // WIFI �����µ�����������
				resultInt = 7 * 60 * 1000;
				break;
			case NetUtils.TYPE_MOIBLE:
				if (simOperate == NetUtils.CHINA_MOIBLE) // �ƶ�NAT ����ϻ�ʱ��
					resultInt = (5 * 60)*1000;
				else if (simOperate == NetUtils.CHINA_TELECOM) // ����
					resultInt = (15 * 60) * 1000;
				else if (simOperate == NetUtils.CHINA_UNICON) // ��ͨ
					resultInt = (5 * 60)*1000;
				break;
			case NetUtils.TYPE_ERROR: // û���������Ӱ�~~
				// TODO ������
				currentHeart = minFixHeart;
				break;
			}
		} 
		return resultInt;
	}
	
	
} 
