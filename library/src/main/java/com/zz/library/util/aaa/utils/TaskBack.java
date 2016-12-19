package com.zz.library.util.aaa.utils;


public interface TaskBack {
	public void excuteSuccess(String method, Object result, Object code);
	public void excuteFailed(String method, Object msg, Object code);
}
