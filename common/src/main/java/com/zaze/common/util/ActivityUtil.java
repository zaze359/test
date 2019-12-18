package com.zaze.common.util;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-10 - 14:13
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.zaze.utils.AppUtil;
import com.zaze.utils.ToastUtil;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-10 - 14:10
 */
public class ActivityUtil {

    // --------------------------------------------------

    /**
     * startActivity
     *
     * @param fragment fragment
     * @param target   target
     */
    public static void startActivity(Fragment fragment, Class<?> target) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, null), 1);
    }

    /**
     * startActivity
     *
     * @param fragment fragment
     * @param target   target
     * @param intent   intent
     */
    public static void startActivity(Fragment fragment, Class<?> target, Intent intent) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, intent), 1);
    }

    // --------------------------------------------------

    /**
     * startActivity
     *
     * @param context context
     * @param intent  intent
     */
    public static void startActivity(Context context, Intent intent) {
        startActivity(context, null, intent);
    }

    /**
     * startActivity
     *
     * @param context context
     * @param target  target
     */
    public static void startActivity(Context context, Class<?> target) {
        startActivity(context, target, false);
    }

    /**
     * startActivity
     *
     * @param context context
     * @param target  target
     * @param intent  intent
     */
    public static void startActivity(Context context, Class<?> target, Intent intent) {
        startActivity(context, target, intent, false);
    }

    /**
     * startActivity
     *
     * @param context  context
     * @param target   target
     * @param isFinish isFinish
     */
    public static void startActivity(Context context, Class<?> target, boolean isFinish) {
        startActivity(context, target, null, isFinish);
    }

    /**
     * startActivity
     *
     * @param context  context
     * @param target   target
     * @param intent   intent
     * @param isFinish isFinish
     */
    public static void startActivity(Context context, Class<?> target, Intent intent, boolean isFinish) {
        context.startActivity(getIntent(context, target, intent));
        if (isFinish && context instanceof Activity) {
            finish((Activity) context);
        }
    }

    // --------------------------------------------------

    /**
     * 平滑的将一个控件平移的过渡到第二个activity
     *
     * @param activity          activity
     * @param target            target
     * @param sharedElement     sharedElement
     * @param sharedElementName sharedElementName
     */
    public static void makeSceneTransitionAnimation(Activity activity, Class<?> target, View sharedElement, String sharedElementName) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName);
        startActivityForAnim(activity, target, optionsCompat);
    }

    /**
     * 平滑的将多个控件平移的过渡到第二个activity
     *
     * @param activity       activity
     * @param target         target
     * @param sharedElements sharedElements
     */
    @SafeVarargs
    public static void makeSceneTransitionAnimation(Activity activity, Class<?> target, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        startActivityForAnim(activity, target, optionsCompat);
    }

    /**
     * 平移
     * 跟overridePendingTransition效果是一样的
     *
     * @param activity   activity
     * @param target     target
     * @param enterResId enterResId
     * @param exitResId  exitResId
     */
    public static void makeCustomAnimation(Activity activity, Class<?> target, int enterResId, int exitResId) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(activity, enterResId, exitResId);
        startActivityForAnim(activity, target, optionsCompat);
    }

    /**
     * 将一个控件平滑的放大过渡到第二个activity，一般用于相册的具体照片的查看
     *
     * @param activity    activity
     * @param target      target
     * @param source      source
     * @param startX      startX
     * @param startY      startY
     * @param startWidth  startWidth
     * @param startHeight startHeight
     */
    public static void makeScaleUpAnimation(Activity activity, Class<?> target, View source,
                                            int startX, int startY, int startWidth, int startHeight) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight);
        startActivityForAnim(activity, target, optionsCompat);
    }

    /**
     * makeThumbnailScaleUpAnimation
     *
     * @param activity  activity
     * @param target    target
     * @param source    source
     * @param thumbnail thumbnail
     * @param startX    startX
     * @param startY    startY
     */
    public static void makeThumbnailScaleUpAnimation(Activity activity, Class<?> target, View source,
                                                     Bitmap thumbnail, int startX, int startY) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY);
        startActivityForAnim(activity, target, optionsCompat);
    }

    /**
     * 执行动画
     *
     * @param activity      activity
     * @param target        target
     * @param optionsCompat optionsCompat
     */
    public static void startActivityForAnim(Activity activity, Class<?> target, @NonNull ActivityOptionsCompat optionsCompat) {
        startActivityForAnim(activity, target, optionsCompat, null);
    }

    /**
     * 执行动画
     *
     * @param activity      activity
     * @param target        target
     * @param optionsCompat optionsCompat
     * @param bundle        bundle
     */
    public static void startActivityForAnim(Activity activity, Class<?> target, @NonNull ActivityOptionsCompat optionsCompat, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Intent intent = new Intent(activity, target);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else {
            startActivity(activity, target);
        }
    }

    // --------------------------------------------------
    // start activity for result
    // --------------------------------------------------

    /**
     * startActivityForResult
     *
     * @param context context
     * @param target  target
     * @param code    code
     */
    public static void startActivityForResult(Context context, Class<?> target, int code) {
        startActivityForResult(context, target, null, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param context context
     * @param intent  intent
     * @param code    code
     */
    public static void startActivityForResult(Context context, Intent intent, int code) {
        startActivityForResult(context, null, intent, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param context context
     * @param target  target
     * @param intent  intent
     * @param code    code
     */
    public static void startActivityForResult(Context context, Class<?> target, Intent intent, int code) {
        startActivityForResult(context, target, intent, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param context  context
     * @param target   target
     * @param intent   intent
     * @param code     code
     * @param isFinish isFinish
     */
    public static void startActivityForResult(Context context, Class<?> target, Intent intent, int code, boolean isFinish) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(getIntent(context, target, intent), code);
            if (isFinish) {
                finish(activity);
            }
        }
    }

    /**
     * startActivityForResult
     *
     * @param fragment fragment
     * @param target   target
     * @param code     code
     */
    public static void startActivityForResult(Fragment fragment, Class<?> target, int code) {
        startActivityForResult(fragment, target, null, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param fragment fragment
     * @param intent   intent
     * @param code     code
     */
    public static void startActivityForResult(Fragment fragment, Intent intent, int code) {
        startActivityForResult(fragment, null, intent, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param fragment fragment
     * @param target   target
     * @param intent   intent
     * @param code     code
     */
    public static void startActivityForResult(Fragment fragment, Class<?> target, Intent intent, int code) {
        startActivityForResult(fragment, target, intent, code, false);
    }

    /**
     * startActivityForResult
     *
     * @param fragment fragment
     * @param target   target
     * @param intent   intent
     * @param code     code
     * @param isFinish isFinish
     */
    public static void startActivityForResult(Fragment fragment, Class<?> target, Intent intent, int code, boolean isFinish) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, intent), code);
        if (isFinish) {
            finish(fragment.getActivity());
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * finish
     *
     * @param context context
     */
    public static void finish(Context context) {
        if (context instanceof Activity) {
            finish((Activity) context);
        }
    }

    /**
     * finish
     *
     * @param context context
     * @param code    code
     */
    public static void finish(Context context, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code);
            activity.finish();
        }
    }

    /**
     * finish
     *
     * @param context context
     * @param data    data
     * @param code    code
     */
    public static void finish(Context context, Intent data, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code, data);
            activity.finish();
        }
    }

    /**
     * finish
     *
     * @param activity activity
     */
    private static void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

//    @JvmStatic
//    @JvmOverloads
//    fun startApplicationSimple(context: Context, packageName: String, bundle: Bundle? = null) {
//        if (!isInstalled(context, packageName)) {
//            ZTipUtil.toast(context, "($packageName)未安装!")
//            return
//        }
//        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
//        if (intent == null) {
//            ZTipUtil.toast(context, "($packageName)不可打开!")
//        } else {
//            if (bundle != null) {
//                intent.putExtras(bundle)
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
//            ZActivityUtil.startActivity(context, intent)
//        }
//    }

    /**
     * startApplicationSimple
     *
     * @param context     context
     * @param packageName packageName
     * @param bundle      bundle
     */
    public static void startApplicationSimple(Context context, String packageName, Bundle bundle) {
        if (!AppUtil.isInstalled(context, packageName)) {
            ToastUtil.toast(context, packageName + " 未安装!");
            return;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            ToastUtil.toast(context, packageName + " 不可打开!");
        } else {
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(context, intent);
        }
    }
    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * 获取intent
     *
     * @param context context
     * @param target  target
     * @param intent  intent
     * @return 获取intent
     */
    private static Intent getIntent(Context context, Class<?> target, Intent intent) {
        if (intent == null) {
            intent = new Intent();
        }
        if (target != null) {
            intent.setClass(context, target);
        }
        return intent;
    }

}