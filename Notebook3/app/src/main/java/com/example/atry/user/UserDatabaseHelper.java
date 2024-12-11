package com.example.atry.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDatabase.db";
    private static final int DATABASE_VERSION = 2; // 更新数据库版本

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_SCHOOL = "school";
    public static final String COLUMN_HOME = "home";
    public static final String COLUMN_SIGN = "sign";
    public static final String USER_ID = "user_id";
    public static final String COLUMN_AVATAR = "avatar"; // 新增头像字段

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_ACCOUNT + " TEXT, " +
                COLUMN_NICKNAME + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_SCHOOL + " TEXT, " +
                COLUMN_SIGN + " TEXT, " +
                COLUMN_AVATAR + " TEXT, " +  // 新增头像字段
                USER_ID + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public Cursor getUserById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public void updateUserAvatar(String userId, String avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVATAR, avatar); // 更新头像字段
        db.update(TABLE_USERS, values, USER_ID + "=?", new String[]{userId});
    }
}
