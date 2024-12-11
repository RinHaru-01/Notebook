package com.example.atry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserCRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    public UserCRUD(Context context) {
        dbHandler = new NoteDatabase(context);
    }

    public void open() {
        db = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    public boolean registerUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    public int authenticateUser(String username, String password) {
        Cursor cursor = db.query("users", new String[]{"user_id"},
                "username=? AND password=?", new String[]{username, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("user_id")); // 返回用户 ID
        }
        return -1; // 用户验证失败
    }
}
