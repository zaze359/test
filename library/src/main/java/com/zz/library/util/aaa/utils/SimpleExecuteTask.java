package com.zz.library.util.aaa.utils;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.zz.library.util.TipUtil;

import java.util.HashMap;

/**
 * Description	: 简单执行逻辑功能的任务(设置为service 调用接口) 继承此类
 * @author		: zaze
 * @version		: 2015年5月28日 下午2:17:05
 */
public abstract class SimpleExecuteTask extends BaseTask {
	protected final int HANDLE_TOAST = 101;
	protected HashMap<String, String> dealTaskFunction = new HashMap<String, String>();

	public SimpleExecuteTask(Context context) {
		super(context);
	}

	@Override
	protected String getInterfaceUrl() {
//		String url = Constants.URL + "/" +  Constants.System_Interfaces_Path + Constants.System_Interfaces_Webservices_File;
		return "";
	}

	/**
	 * Description  : 调用接口的方式设置为service
	 * @author 		: zaze
	 * @version		: 2015-5-29 - 上午12:46:57
	 * (non-Javadoc)
	 */
	@Override
	protected RequestInterfaceMode resetRequestInterface() {
		return new RequestByService(context);
	}

	@Override
	public <T> void executeTask(String functionName, T... args) {
		// open net 开网此处 或  写一个子类?
//		TaskBean taskBean = new TaskBean(this, functionName, args);
//		TaskManager.getInstance().putToTaskQueue(taskBean);
//		TaskManager.getInstance().excuteIntervalTask();
		try {
			executeMethod(this, functionName, args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
//		excuteFailed(functionName, "开网失败", "");
	}

	@Override
	public void excuteSuccess(final String method, final Object result, final Object code) {
		if(result instanceof HashMap<?, ?>) {
			System.out.println("excuteSuccess - method : " + method);
			// 以后优化线程管理
			new Thread(new Runnable() {
				@Override
				public void run() {
					doSuccess(method, result, code);
					handler.post(new Runnable() {
						@Override
						public void run() {
							if(callBack != null) {
								callBack.excuteSuccess(method, result, code);
							}
						}
					});
				}
			}).start();
		}
	}
	
	@Override
	public void excuteFailed(final String method, final Object msg, final Object code) {
		if(msg instanceof String) {
			System.out.println("excuteFailed - method : " + method + ", msg : " + msg);
			// 以后优化线程管理
			new Thread(new Runnable() {
				@Override
				public void run() {
					doFailed(method, msg, code);
					handler.post(new Runnable() {
						@Override
						public void run() {
							if(callBack != null) {
								callBack.excuteSuccess(method, msg, code);
							}
						}
					});
				}
			}).start();
		}
	}

	/**
	 * Description	: 处理成功
	 * @param method
	 * @param result
	 * @param code
	 * @author		: zaze
	 * @version 	: 2015年5月29日 下午3:24:52
	 */
	protected void doSuccess(String method, Object result, Object code) {
//		LogDevelopmentKit.v("doSuccess : " + method + ", result : " + result + ", code : " + code);
		@SuppressWarnings("unchecked")
		HashMap<String, String> resultMap = (HashMap<String, String>)result;
		if(resultMap.containsKey(method)) {
			String message = (String) resultMap.get(method);
			if(message == null) {
				// 设置为空 调用含String参数的方法
				message = "";
			}
			if(dealTaskFunction.containsKey(method)) {
				try {
					if(code == null || code.equals("")) {
						executeMethod(this, dealTaskFunction.get(method), message);
					} else {
						executeMethod(this, dealTaskFunction.get(method), message, code);
					}
				} catch (NoSuchMethodException e) {
					if(code != null) {
						Log.i(dealTaskFunction.get(method) + "", "may be you need a function contains parameter of code : " + code);
					}
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Description	: 处理失败
	 * @param method
	 * @param msg
	 * @param code
	 * @author		: zaze
	 * @version 	: 2015年5月29日 下午3:24:46
	 */
	protected abstract void doFailed(String method, Object msg, Object code);
	
	/**
	 * Description	: 请求调用接口并添加到管理类中
	 * @author		: zaze 
	 * @version 	: 2015年6月2日 上午9:16:07
	 * @param bean
	 * (non-Javadoc)
	 */
	@Override
	protected void requestInterface(InterfaceBean bean) {
		TaskBean taskBean = new TaskBean(this, bean);
		taskBean.setStatus(1);
		boolean excute = TaskManager.getInstance().putToTaskQueue(taskBean);
		if(excute && requestMode != null)
			requestMode.request(bean);
	}
	
	/**
	 * Description	: 构建基础的map
	 * @return
	 * @author		: zaze
	 * @version 	: 2015年5月29日 上午9:06:08
	 */
	protected HashMap<String, String> buildMap() {
		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("sys_in_machine_code", Constants.ex_machine_code + "");
//		map.put("sys_app_package", Constants.packageName + "");//包名
//		map.put("us_id", Constants.usId + "");
//		map.put("us_uid", Constants.usUid + "");
//		map.put("us_session_id", Constants.isCompress ? SessionMd5.getMd5Session() : Constants.usSessionId);
		return map;
	}
	
	/**
	 * Description	: 设置好了基础的map,functionnName,interfaceUrl(有默认值),ResList
	 * @param functionName
	 * @return
	 * @author		: zaze
	 * @version 	: 2015年5月28日 下午2:28:11
	 */
	protected InterfaceBean buildInterfaceBean(String functionName) {
		InterfaceBean bean = new InterfaceBean();
		bean.setMap(buildMap());
		bean.setFunctionName(functionName);
		bean.addToResList(functionName);
		String url = getInterfaceUrl();
		if(url == null) {
			// 抛异常?
			url = "";
		}
		bean.setInterfaceUrl(url);
		return bean;
	}

	/**
	 * Description	: 声明处理数据的方法
	 * @param key
	 * @param value
	 * @author		: zaze
	 * @version 	: 2015年5月28日 上午10:12:08
	 */
	protected void declareDealFunction(String key, String value) {
		dealTaskFunction.put(key, value);
	}

	/**
	 * Description	: 声明处理数据的方法
	 * @param key
	 * @author		: zaze
	 * @version 	: 2015年5月28日 上午10:12:08
	 */
	protected void removeDealFunction(String key) {
		dealTaskFunction.remove(key);
	}
	// ----- 处理handle

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case HANDLE_TOAST:
			if(msg.obj != null && msg.obj instanceof String) {
				TipUtil.toastDisplay("" + (String)msg.obj, context);
			}
			break;
		default:
			break;
		}
	}
}
