package com.zaze.util.aaa.utils;

import android.content.Context;

import java.util.HashMap;

/**
 * Description	: 包含更新机制的任务继承此类
 * @author		: zaze
 * @version		: 2015年5月28日 下午2:17:44
 */
public abstract class UpdateMechanismTask extends SimpleExecuteTask {

//	private ResponseUpdateDBManager db;
	private HashMap<String, String> operateCodeMap;
	
	public UpdateMechanismTask(Context context) {
		super(context);
//		db = new ResponseUpdateDBManager(context);
		operateCodeMap = new HashMap<String, String>();
	}
	
	@Override
	protected String getInterfaceUrl() {
		// 更新机制服务器url
//		String url = Constants.URL + "/"+  Constants.System_Interfaces_Path + Constants.System_Interfaces_Webservices_File_Update;
		return "";
	}

	@Override
	protected void doSuccess(String method, Object result, Object code) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> resultMap = (HashMap<String, String>)result;
		String key = "sys_out_up_id";
		if(resultMap.containsKey(key)) {
			// 更新 或 插入upid 
			int value = Integer.parseInt(resultMap.get(key).toString().trim());
			String operateCode = operateCodeMap.get(method);
//			if(db.dataIsExist(operateCode) > 0) {
//				db.updateValue(operateCode, value);
//			} else {
//				db.insert(new TUpid(method, operateCode, value));
//			}
		}
		super.doSuccess(method, result, code);
	}

	@Override
	protected void requestInterface(InterfaceBean bean) {
		String operateCode = bean.getOperateCode();
		String effectFunction = bean.getEffectFunction();
		String functionName = bean.getFunctionName();
		System.out.println("operateCode : " + operateCode);
		System.out.println("functionName : " + functionName);
		System.out.println("effectFunction : " + effectFunction);
		// 操作码
		if(operateCode != null && !operateCode.equals("")) {
			operateCodeMap.put(functionName, operateCode);
			bean.put("sys_operate_code", operateCode);
			int upId = getUpId(operateCode);
			bean.put("sys_in_up_id", upId + "");
			bean.addToResList("sys_out_up_id");
		}
		// 影响的方法 
		if(effectFunction != null && !effectFunction.equals("")) {
			bean.put("sys_data_update_functions", effectFunction);
		}
		
		super.requestInterface(bean);
	}
	
	private int getUpId(String operateCode) {
//		return db.getValue(operateCode);
		return 0;
	}	
}