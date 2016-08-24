package com.zz.library.util.aaa.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class BaseTask extends Executor implements TaskCallback {
	/** 上下文 */
	protected Context context;
	/** 调用接口的方式 */
	protected RequestInterfaceMode requestMode;
	// 暂时设置个回调
	protected TaskCallback callBack;
	public TaskCallback getCallBack() {
		return callBack;
	}
	public void setCallBack(TaskCallback callBack) {
		this.callBack = callBack;
	}
	
	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			BaseTask.this.handleMessage(msg);
			super.handleMessage(msg);
		}
		
	};
	
	// -------------------------------
	public BaseTask(Context context) {
		super();
		this.context = context;
		requestMode = resetRequestInterface();
		handler = new Handler();
	}
	
	/**
	 * Description  : 执行任务
	 * @author 		: zaze
	 * @version		: 2015-5-29 - 上午12:49:46
	 * @param functionName
	 * @param obj
	 */
	public abstract <T> void executeTask(String functionName, T... obj);
	
	/**
	 * Description  : 这里才是真正的调用接口
	 * @author 		: zaze
	 * @version		: 2015-5-29 - 上午12:49:34
	 * @param bean
	 */
	protected abstract void requestInterface(InterfaceBean bean);
	
	/**
	 * Description  : 重置修改调用的方式
	 * @author 		: zaze
	 * @version		: 2015-5-29 - 上午12:28:18
	 */
	protected abstract RequestInterfaceMode resetRequestInterface();
	
	/**
	 * Description	: 获取接口url
	 * @return
	 * @author		: zaze
	 * @version 	: 2015年5月29日 上午11:08:01
	 */
	protected abstract String getInterfaceUrl();
	
	/**
	 * Description	: TODO
	 * @param msg
	 * @author		: zaze
	 * @version 	: 2015年6月1日 下午2:36:50
	 */
	protected abstract void handleMessage(Message msg);
	
	public RequestInterfaceMode getRequestMode() {
		return requestMode;
	}
}
