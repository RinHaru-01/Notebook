package com.example.atry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.atry.user.UserDatabaseHelper;

import java.util.ResourceBundle;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvNickName,tvAccount,tvGender,tvCity,tvHome,tvSchool,tvSign;

    private String userId;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        initView();

        // 获取本地保存的 user_id
        SharedPreferences spf = getSharedPreferences("user_info", MODE_PRIVATE);
        userId = spf.getString("user_id", "NO");
        if (userId == "NO") {
            // 如果 user_id 不存在，则跳转到登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }



    private void initData() {
// 从数据库中加载用户数据
        Toast.makeText(UserProfileActivity.this,userId,Toast.LENGTH_SHORT).show();
        Cursor cursor = dbHelper.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String account = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_ACCOUNT));
            String nickName = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_NICKNAME));
            String city = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_CITY));
            String gender = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_GENDER));
            String school = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_SCHOOL));
            String home = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_HOME));
            String sign = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_SIGN));

            tvAccount.setText(account);
            tvNickName.setText(nickName);
            tvCity.setText(city);
            tvGender.setText(gender);
            tvSchool.setText(school);
            tvHome.setText(home);
            tvSign.setText(sign);

            cursor.close();
        }
    }

    private void getDataFromSpf() {

        SharedPreferences spfRecord = getSharedPreferences("spfRecord",MODE_PRIVATE);
        String account = spfRecord.getString("account"," ");
        String nickName = spfRecord.getString("nick_name"," ");
        String city = spfRecord.getString("city"," ");
        String gender = spfRecord.getString("gender"," ");
        String school = spfRecord.getString("school"," ");
        String home = spfRecord.getString("home"," ");
        String sign = spfRecord.getString("sign"," ");


        tvAccount.setText(account);
        tvNickName.setText(nickName);
        tvHome.setText(city);
        tvSchool.setText(school);
        tvSign.setText(sign);
        tvCity.setText(city);
        tvGender.setText(gender);

    }

    private void initView() {
        tvAccount = findViewById(R.id.tv_account_text);
        tvNickName = findViewById(R.id.tv_nick_name);
        tvHome = findViewById(R.id.tv_home_text);
        tvSchool = findViewById(R.id.tv_school_text);
        tvSign = findViewById(R.id.tv_sign_text);
        tvCity = findViewById(R.id.tv_city);
        tvGender = findViewById(R.id.tv_gender);



    }

    public void toEdit(View view) {
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {

        SharedPreferences spf = getSharedPreferences("spfRecord",MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putBoolean("isLogin",false);
        edit.apply();
        SharedPreferences temp = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = temp.edit();
        editor.putString("user_id", "no");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

}