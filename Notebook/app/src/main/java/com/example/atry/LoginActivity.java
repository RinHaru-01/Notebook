package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.atry.MainActivity;
import com.example.atry.R;
import com.example.atry.UserDbHelper;
import com.example.atry.Userinfo;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private CheckBox checkbox;
    private boolean is_login;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// 获取mSharedPreferences 实例
        mSharedPreferences =getSharedPreferences("user_info",MODE_PRIVATE);

        initView();

        setListener();

        initData();
    }

    private void initData() {
        is_login =mSharedPreferences.getBoolean("is_login",false);
        String username=mSharedPreferences.getString("username","");
        String password=mSharedPreferences.getString("password","");

        if(is_login){
            et_username.setText(username);
            et_password.setText(password);
            checkbox.setChecked(true);
        }
    }

    private void initView() {
        et_username =findViewById(R.id.et_username);
        et_password =findViewById(R.id.et_password);
        checkbox =findViewById(R.id.checkbox);
    }

    private void setListener() {

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_login=isChecked;

            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =et_username.getText().toString();
                String password =et_password.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"请输入用户名或密码",Toast.LENGTH_SHORT).show();
                }else {
                    Userinfo userinfo = UserDbHelper.getInstance(LoginActivity.this).login(username);
                    if(userinfo!=null){
                        if(userinfo.getPassword().equals(password)&&userinfo.getUsername().equals(username)){
                            SharedPreferences.Editor edit =mSharedPreferences.edit();
                            edit.putString("username",username);
                            edit.putString("password",password);
                            edit.putBoolean("is_login",is_login);
                            edit.commit();

                            //登录成功
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //



                        }else {
                            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"该账号暂未注册",Toast.LENGTH_SHORT).show();

                    }
                }
            }

        });
    }
}