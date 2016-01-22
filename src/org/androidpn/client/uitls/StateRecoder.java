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

	private AtomicInteger sucessInteger;
	private AtomicInteger failedInteger;// 测量heart长度时候使用的计数器
	// 上一次心跳包是否成功
	private boolean isPreSucess = false;
	// 进来第一次肯定是没有失败的.上一次心跳包是否失败
	private boolean isPreFaild = false;

	// 初始化
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

	// 成功之后增量加1并且返回当前数量
	public int  sucessincrenment1() {
		return this.sucessInteger.incrementAndGet();
	}
	
	// 失败数量加1并且返回
	public int faildIncrenment1() {
		return this.failedInteger.incrementAndGet();
	}
	
	// 把计数器都清零
	public void clear() {
		this.failedInteger.set(0);
		this.sucessInteger.set(0);
		this.isPreFaild = false;
		this.isPreSucess = false;
	}
	
}
