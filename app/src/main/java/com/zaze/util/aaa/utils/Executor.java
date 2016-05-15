package com.zaze.util.aaa.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public abstract class Executor {
	
	protected boolean debugLog = true;
	
	/**
	 * Description  : 反射执行方法
	 * @author 		: zaze
	 * @version		: 2015-5-27 - 下午9:39:45
	 * @param functionName 方法名
	 * @param args	对应方法传入的参数
	 * @throws NoSuchMethodException 
	 */
	public <T> void executeMethod(Object self, String functionName, T... args) throws NoSuchMethodException {
		if(self == null) {
			return;
		}
		if(debugLog)
			System.out.println("executeMethod functionName-- " + functionName);
		try {
			Class<?>[] clazzs = null;
			if(args != null) {
				clazzs = new Class[args.length];
				for (int i = 0; i < args.length; i++) {
					clazzs[i] = args[i].getClass();
					clazzs[i] = dealPrimitive(clazzs[i]);
					if(clazzs[i].isPrimitive()) {
						clazzs[i] = dealPrimitive(clazzs[i]);
					}
					if(debugLog)
						System.out.println("executeMethod 参数 clazz[" + i + "] " + clazzs[i]);
				}
			}
			Method method = self.getClass().getDeclaredMethod(functionName, clazzs);
			method.invoke(self, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException(functionName);
		}
	}
	
	/**
	 * Description  : 处理8种基础类型的反射
	 * @author 		: zaze
	 * @version		: 2015-5-27 - 下午10:00:01
	 * @param clazzs
	 * @return
	 */
	private Class<?> dealPrimitive(Class<?> clazzs) {
		if(Integer.class.equals(clazzs)) {
			return int.class;
		} else {
			//...
		}
		return clazzs;
	}
}
