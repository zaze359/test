package com.zaze.aarrepo.utils.helper;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-09-02 - 09:13
 */
public class OnClickHelper {

    private static long clickTime = 0;

    public interface SingleDoListener {
        void doIt(Object... objects);
    }


    public static void setSingleDoListener(SingleDoListener singleDoListener, Object... objects) {
        if (isCanClick()) {
            singleDoListener.doIt(objects);
            updateClickTime();
        }
    }


    /**
     * @param view
     * @param onClickListener
     */
    public static void setOnClickListener(View view, final View.OnClickListener onClickListener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanClick()) {
                    v.setEnabled(false);
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                    updateClickTime();
                    v.setEnabled(true);
                }
            }
        });
    }


    /**
     * @param listView
     * @param onItemClickListener
     */
    public static void setOnItemClickListener(ListView listView, final AdapterView.OnItemClickListener onItemClickListener) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isCanClick()) {
                    view.setEnabled(false);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(parent, view, position, id);
                    }
                    updateClickTime();
                    view.setEnabled(true);
                }
            }
        });
    }

    private static boolean isCanClick() {
        return System.currentTimeMillis() - clickTime > 400L;
    }

    private static void updateClickTime() {
        clickTime = System.currentTimeMillis();
    }
}
