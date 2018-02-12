package com.zaze.demo.component.font;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-02-07 - 15:14
 */
public class FontUtil {

    public static void setDefaultFontFromAsset(Context context, String staticTypefaceFieldName, String fontAssetName) {
        replaceFont(staticTypefaceFieldName, Typeface.createFromAsset(context.getAssets(), fontAssetName));
    }

    public static void setDefaultFontFormSystem(String staticTypefaceFieldName, String fontAssetName) {
        File file = new File("system/fonts/" + fontAssetName);
        if (file.exists()) {
            replaceFont(staticTypefaceFieldName, Typeface.createFromFile(file));
        }
    }

    public static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void a(Context context) {
    }
}
