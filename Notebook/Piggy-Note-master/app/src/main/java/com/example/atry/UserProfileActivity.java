package com.example.atry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ResourceBundle;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvNickName,tvAccount,tvGender,tvCity,tvHome,tvSchool,tvSign;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);




        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }



    private void initData() {

        getDataFromSpf();
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

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

}