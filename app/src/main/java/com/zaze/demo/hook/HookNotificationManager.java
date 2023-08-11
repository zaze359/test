package com.zaze.demo.hook;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-09-03 - 14:45
 */
public class HookNotificationManager {

    private static final String TAG = "HookNotificationManager";

    public static NotificationManager hookNotificationManager(final Context context) throws Exception {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Method getService = NotificationManager.class.getDeclaredMethod("getService");
        getService.setAccessible(true);
        final Object sOriginService = getService.invoke(notificationManager);
        Class iNotiMngClz = Class.forName("android.app.INotificationManager");
        // 第二步：得到我们的动态代理对象，获取到的是 INotificationManager 接口的实现。
        Object proxyNotificationManager = Proxy.newProxyInstance(context.getClass().getClassLoader(), new Class[]{iNotiMngClz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d("", "invoke(). method:" + method);
                String name = method.getName();
                Log.d(TAG, "invoke: name=" + name);
                if (args != null && args.length > 0) {
                    for (Object arg : args) {
                        if ("com.zaze.demo".equals(arg)) {
                            arg = "android";
                        }
                        Log.d(TAG, "invoke: arg=" + arg);
                    }
                }
                Toast.makeText(context.getApplicationContext(), "检测到有人发通知了", Toast.LENGTH_SHORT).show();
                // 操作交由 sOriginService 处理，不拦截通知
                return method.invoke(sOriginService, args);
                // 拦截通知，什么也不做
                //                    return null;
                // 或者是根据通知的 Tag 和 ID 进行筛选
            }
        });
        // 第三步：偷梁换柱，使用 proxyNotiMng 替换系统的 sService
        Field sServiceField = NotificationManager.class.getDeclaredField("sService");
        sServiceField.setAccessible(true);
        sServiceField.set(notificationManager, proxyNotificationManager);
//        Field contextField = NotificationManager.class.getDeclaredField("mContext");
//        contextField.set(notificationManager, context);
        return notificationManager;
    }
}
