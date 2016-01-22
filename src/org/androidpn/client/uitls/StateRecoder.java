package org.androidpn.client.uitls;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
  * @ClassName: Counter
  * @Description: ��¼��,������������,��¼�Ƿ�ɹ�ʧ��.
  * @author jin
  * @Company ���ڵ°¼������޹�˾
  * @date 2016��1��7�� ����8:12:48
  *
 */
public class StateRecoder {

	private AtomicInteger sucessInteger;
	private AtomicInteger failedInteger;// ����heart����ʱ��ʹ�õļ�����
	// ��һ���������Ƿ�ɹ�
	private boolean isPreSucess = false;
	// ������һ�ο϶���û��ʧ�ܵ�.��һ���������Ƿ�ʧ��
	private boolean isPreFaild = false;

	// ��ʼ��
	public StateRecoder() {
		this.failedInteger = new AtomicInteger();
		this.sucessInteger = new AtomicInteger();
	}

	public boolean isPreSucess() {
		return isPreSucess;
	}

	public void setPreSucess(boolean isPreSucess) {
		this.isPreSucess = isPreSucess;
	}

	public boolean isPreFaild() {
		return isPreFaild;
	}

	public void setPreFaild(boolean isPreFaild) {
		this.isPreFaild = isPreFaild;
	}

	// �ɹ�֮��������1���ҷ��ص�ǰ����
	public int  sucessincrenment1() {
		return this.sucessInteger.incrementAndGet();
	}
	
	// ʧ��������1���ҷ���
	public int faildIncrenment1() {
		return this.failedInteger.incrementAndGet();
	}
	
	// �Ѽ�����������
	public void clear() {
		this.failedInteger.set(0);
		this.sucessInteger.set(0);
		this.isPreFaild = false;
		this.isPreSucess = false;
	}
	
}
