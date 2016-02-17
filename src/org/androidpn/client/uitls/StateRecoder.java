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
	// ����ִ�е�����
	private AtomicInteger totalInteger;
	private AtomicInteger sucessInteger;
	private AtomicInteger failedInteger;// ����heart����ʱ��ʹ�õļ�����
	// ��һ���������Ƿ�ɹ�
	private boolean isPreSucess = false;
	// ������һ�ο϶���û��ʧ�ܵ�.��һ���������Ƿ�ʧ��
	private boolean isPreFaild = false;

	// �Ƿ����ɹ���.
	private boolean isCalcSucess = false;
	
	// ��ʼ��
	public StateRecoder() {
		this.failedInteger = new AtomicInteger();
		this.sucessInteger = new AtomicInteger();
		this.totalInteger = new AtomicInteger();
	}

	public boolean isPreSucess() {
		return isPreSucess;
	}

	public void setPreSucess(boolean isPreSucess) {
		this.isPreSucess = isPreSucess;
	}

	
	
	public boolean isCalcSucess() {
		return isCalcSucess;
	}

	public void setCalcSucess(boolean isCalcSucess) {
		this.isCalcSucess = isCalcSucess;
	}

	/**
	 * ������������һ���Ƿ�ʧ��.
	 * @return
	 */
	public boolean isPreFaild() {
		return isPreFaild;
	}

	public void setPreFaild(boolean isPreFaild) {
		this.isPreFaild = isPreFaild;
	}

	/**
	 *  �ɹ�֮��������1���ҷ��ص�ǰ����
	 * @return ���ص�������֮��ǰ������
	 */
	public int  sucessincrenment1() {
		return this.sucessInteger.incrementAndGet();
	}
	
	/**
	 *  ʧ��������1���ҷ���
	 * @return ��ǰʧ����������
	 */
	public int faildIncrenment1() {
		return this.failedInteger.incrementAndGet();
	}
	
	
	
	public AtomicInteger getTotalInteger() {
		return totalInteger;
	}

	/**
	 * ��ǰ���ܵ������������� 
	 */
	public int increaseTotal1() {
		return totalInteger.incrementAndGet();
	}

	// �Ѽ�����������
	public void clear() {
		this.failedInteger.set(0);
		this.sucessInteger.set(0);
		this.totalInteger.set(0);
		this.isPreFaild = false;
		this.isPreSucess = false;
	}
	
}
