package org.androidpn.client.uitls;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
  * @ClassName: Counter
  * @Description: 记录器,计算心跳长度,记录是否成功失败.
  * @author jin
  * @Company 深圳德奥技术有限公司
  * @date 2016年1月7日 下午8:12:48
  *
 */
public class StateRecoder {
	// 心跳执行的总数
	private AtomicInteger totalInteger;
	private AtomicInteger sucessInteger;
	private AtomicInteger failedInteger;// 测量heart长度时候使用的计数器
	// 上一次心跳包是否成功
	private boolean isPreSucess = false;
	// 进来第一次肯定是没有失败的.上一次心跳包是否失败
	private boolean isPreFaild = false;

	// 是否计算成功了.
	private boolean isCalcSucess = false;
	
	// 初始化
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
	 * 连续心跳中上一次是否失败.
	 * @return
	 */
	public boolean isPreFaild() {
		return isPreFaild;
	}

	public void setPreFaild(boolean isPreFaild) {
		this.isPreFaild = isPreFaild;
	}

	/**
	 *  成功之后增量加1并且返回当前数量
	 * @return 返回的是增加之后当前的数量
	 */
	public int  sucessincrenment1() {
		return this.sucessInteger.incrementAndGet();
	}
	
	/**
	 *  失败数量加1并且返回
	 * @return 当前失败总数数量
	 */
	public int faildIncrenment1() {
		return this.failedInteger.incrementAndGet();
	}
	
	
	
	public AtomicInteger getTotalInteger() {
		return totalInteger;
	}

	/**
	 * 当前的总的心跳包的数量 
	 */
	public int increaseTotal1() {
		return totalInteger.incrementAndGet();
	}

	// 把计数器都清零
	public void clear() {
		this.failedInteger.set(0);
		this.sucessInteger.set(0);
		this.totalInteger.set(0);
		this.isPreFaild = false;
		this.isPreSucess = false;
	}
	
}
