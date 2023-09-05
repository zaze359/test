package com.zaze.demo.feature.storage.provider;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.zaze.common.base.AbsActivity;
import com.zaze.core.database.model.UserEntity;
import com.zaze.core.database.sqlite.UserDao;
import com.zaze.demo.feature.storage.R;
import com.zaze.utils.CursorHelper;
import com.zaze.utils.ext.CommonExtKt;
import com.zaze.utils.ext.JsonExtKt;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-03-28 04:00 1.0
 */
public class ProviderActivity extends AbsActivity {
    TextView contentTv;
    private ContentResolver resolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_activity);
        resolver = this.getContentResolver();
        contentTv = findViewById(R.id.content_tv);
        Uri uri = ZazeProvider.CONTENT_URI;
        findViewById(R.id.query_btn).setOnClickListener(v -> {
//           Uri queryUri = Uri.parse("content://" + ZazeProvider.AUTHORITY + "/" + ZazeProvider.PATH_SINGLE);
            Cursor cursor = resolver.query(uri,
                    new String[]{UserDao.Properties.ID, UserDao.Properties.USER_ID, UserDao.Properties.USER_NAME},
                    null, null, null);
            List<UserEntity> list = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    list.add(new UserEntity(
                            CursorHelper.getInt(cursor, UserDao.Properties.ID),
                            CursorHelper.getLong(cursor, UserDao.Properties.USER_ID),
                            CursorHelper.getString(cursor, UserDao.Properties.USER_NAME)
                    ));
                }
                cursor.close();
            }
            updateContent(JsonExtKt.toJsonString(list));
        });
        findViewById(R.id.insert_btn).setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            // values.put(UserDao.Properties.ID, 1);
            values.put(UserDao.Properties.USER_NAME, "Tom");
            values.put(UserDao.Properties.USER_ID, 21);
            Uri newUri = resolver.insert(uri, values);
            updateContent("insert: " + newUri);
        });
        findViewById(R.id.update_btn).setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(UserDao.Properties.USER_NAME, "Tommmm");
            values.put(UserDao.Properties.USER_ID, 99);
            int num = resolver.update(uri, values, UserDao.Properties.ID + " = ?", new String[]{"1"});
            updateContent("update num: " + num);

        });

        findViewById(R.id.delete_btn).setOnClickListener(v -> {
            int num = resolver.delete(uri, UserDao.Properties.ID + " > ?", new String[]{"1"});
            updateContent("delete num: " + num);

        });

    }

    private void updateContent(String content) {
        ZLog.d(ZTag.TAG_PROVIDER, content);
        contentTv.setText(content);
    }
}
