package com.zaze.util.aaa.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description	: 接口访问bean
 * @author		: zaze
 * @version		: 2015年5月27日 下午2:32:21
 */
public class InterfaceBean implements Serializable {
	
	/** TODO */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, String> map;
	/** 操作码 */
	private String operateCode;
	/** 影响的方法 */
	private String effectFunction;
	private String functionName;
	private String interfaceUrl;
	/** 发给服务器, 服务器会把这个值再返回 */
	private String returnCode;
	private ArrayList<String> resList;
	
	public InterfaceBean() {
		super();
		resList = new ArrayList<String>();
		map = new HashMap<String, String>();
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		if(map != null) {
			this.map.clear();
			this.map.putAll(map);
		}
	}
	
	public void put(String key, String value) {
		this.map.put(key, value);
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getEffectFunction() {
		return effectFunction;
	}

	public void setEffectFunction(String effectFunction) {
		this.effectFunction = effectFunction;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public ArrayList<String> getResList() {
		return resList;
	}

	public void setResList(ArrayList<String> resList) {
		if(resList != null) {
			this.resList.clear();
			this.resList.addAll(resList);
		}
	}

	public void addToResList(String res) {
		if(!resList.contains(res)) {
			resList.add(res);
		}
	}
}
