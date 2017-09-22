package com.zaze.utils;

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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-10-10 - 14:10
 */
public class ZActivityUtil {

    // --------------------------------------------------
    public static void startActivity(Fragment fragment, Class<?> target) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, null), 1);
    }

    public static void startActivity(Fragment fragment, Class<?> target, Intent intent) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, intent), 1);
    }

    // --------------------------------------------------
    public static void startActivity(Context context, Intent intent) {
        startActivity(context, null, intent);
    }

    public static void startActivity(Context context, Class<?> target) {
        startActivity(context, target, false);
    }

    public static void startActivity(Context context, Class<?> target, Intent intent) {
        startActivity(context, target, intent, false);
    }

    public static void startActivity(Context context, Class<?> target, boolean isFinish) {
        startActivity(context, target, null, isFinish);
    }

    public static void startActivity(Context context, Class<?> target, Intent intent, boolean isFinish) {
        context.startActivity(getIntent(context, target, intent));
        if (isFinish && context instanceof Activity) {
            finish((Activity) context);
        }
    }

    // --------------------------------------------------
    @SafeVarargs
    public static void makeSceneTransitionAnimation(Activity activity, Class<?> target, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        startActivityForAnim(activity, target, optionsCompat);
    }

    public static void makeCustomAnimation(Activity activity, Class<?> target, int enterResId, int exitResId) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(activity, enterResId, exitResId);
        startActivityForAnim(activity, target, optionsCompat);
    }

    public static void makeScaleUpAnimation(Activity activity, Class<?> target, View source,
                                            int startX, int startY, int startWidth, int startHeight) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight);
        startActivityForAnim(activity, target, optionsCompat);
    }

    public static void makeSceneTransitionAnimation(Activity activity, Class<?> target, View source,
                                                    Bitmap thumbnail, int startX, int startY) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY);
        startActivityForAnim(activity, target, optionsCompat);
    }

    public static void startActivityForAnim(Activity activity, Class<?> target, @NonNull ActivityOptionsCompat optionsCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Intent intent = new Intent(activity, target);
            activity.startActivity(intent, optionsCompat.toBundle());
        } else {
            startActivity(activity, target);
        }
    }

    // --------------------------------------------------
    // start activity for result
    // --------------------------------------------------
    public static void startActivityForResult(
            Context context, Class<?> target, int code) {
        startActivityForResult(context, target, null, code, false);
    }

    public static void startActivityForResult(
            Context context, Intent intent, int code) {
        startActivityForResult(context, null, intent, code, false);
    }

    public static void startActivityForResult(
            Context context, Class<?> target, Intent intent, int code) {
        startActivityForResult(context, target, intent, code, false);
    }

    public static void startActivityForResult(
            Context context, Class<?> target, Intent intent, int code, boolean isFinish) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(getIntent(context, target, intent), code);
            if (isFinish) {
                finish(activity);
            }
        }
    }

    public static void startActivityForResult(
            Fragment fragment, Class<?> target, int code) {
        startActivityForResult(fragment, target, null, code, false);
    }

    public static void startActivityForResult(
            Fragment fragment, Intent intent, int code) {
        startActivityForResult(fragment, null, intent, code, false);
    }

    public static void startActivityForResult(
            Fragment fragment, Class<?> target, Intent intent, int code) {
        startActivityForResult(fragment, target, intent, code, false);
    }

    public static void startActivityForResult(
            Fragment fragment, Class<?> target, Intent intent, int code, boolean isFinish) {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), target, intent), code);
        if (isFinish) {
            finish(fragment.getActivity());
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------
    public static void finish(Context context) {
        if (context instanceof Activity) {
            finish((Activity) context);
        }
    }

    public static void finish(Context context, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code);
            activity.finish();
        }
    }

    public static void finish(Context context, Intent data, int code) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.setResult(code, data);
            activity.finish();
        }
    }

    private static void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    // --------------------------------------------------
    // --------------------------------------------------

    /**
     * @param context
     * @param target
     * @param intent
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