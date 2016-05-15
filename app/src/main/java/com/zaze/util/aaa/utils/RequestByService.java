package com.zaze.util.aaa.utils;

import android.content.Context;

/**
 * Description  : 请求模式 调用service
 * @author 		: zaze
 * @version		: 2015-5-29 - 上午12:20:57
 */
public class RequestByService implements RequestInterfaceMode{
	
	private Context context;
	
	public RequestByService(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	public void request(InterfaceBean bean) {
//		Intent intent = new Intent(context, ResponseDownService.class);
//		intent.putExtra("bean", bean);
//		context.startService(intent);
	}
}
