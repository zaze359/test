package com.zaze.util.aaa.utils;


public interface TaskCallback {
	public void excuteSuccess(String method, Object result, Object code);
	public void excuteFailed(String method, Object msg, Object code);
}
