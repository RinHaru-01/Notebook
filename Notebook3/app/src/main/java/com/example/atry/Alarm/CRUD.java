package com.example.atry.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] columns = {
            PlanDatabase.ID,
            PlanDatabase.TITLE,
            PlanDatabase.CONTENT,
            PlanDatabase.TIME,
            PlanDatabase.USER_ID,
    };

    public CRUD(Context context){
        dbHandler = new PlanDatabase(context);
    }

    public void open(){
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        dbHandler.close();
    }

    public Plan addPlan(Plan plan, String userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlanDatabase.TITLE, plan.getTitle());
        contentValues.put(PlanDatabase.CONTENT, plan.getContent());
        contentValues.put(PlanDatabase.TIME, plan.getTime());
        contentValues.put(PlanDatabase.USER_ID, userId); // 关联用户 ID

        long insertId = db.insert(PlanDatabase.TABLE_NAME, null, contentValues);
        plan.setId(insertId);
        return plan;
    }


    public List<Plan> getAllPlans(String userId) {
        Cursor cursor = db.query(PlanDatabase.TABLE_NAME, columns,
                PlanDatabase.USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, null);

        List<Plan> plans = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Plan plan = new Plan();
                plan.setId(cursor.getLong(cursor.getColumnIndex(PlanDatabase.ID)));
                plan.setTitle(cursor.getString(cursor.getColumnIndex(PlanDatabase.TITLE)));
                plan.setContent(cursor.getString(cursor.getColumnIndex(PlanDatabase.CONTENT)));
                plan.setTime(cursor.getString(cursor.getColumnIndex(PlanDatabase.TIME)));
                plans.add(plan);
            }
        }
        cursor.close();
        return plans;
    }

    public int updatePlan(Plan plan, String userId) {
        ContentValues values = new ContentValues();
        values.put(PlanDatabase.TITLE, plan.getTitle());
        values.put(PlanDatabase.CONTENT, plan.getContent());
        values.put(PlanDatabase.TIME, plan.getTime());

        // 更新指定用户的计划
        return db.update(PlanDatabase.TABLE_NAME, values,
                PlanDatabase.ID + "=? AND " + PlanDatabase.USER_ID + "=?",
                new String[]{String.valueOf(plan.getId()), String.valueOf(userId)});
    }

    public void removePlan(Plan plan, String userId) {
        db.delete(PlanDatabase.TABLE_NAME,
                PlanDatabase.ID + "=? AND " + PlanDatabase.USER_ID + "=?",
                new String[]{String.valueOf(plan.getId()), String.valueOf(userId)});
    }
}
