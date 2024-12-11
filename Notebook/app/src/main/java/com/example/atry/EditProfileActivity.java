package com.example.atry;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeTOEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG ="tag";

    private EditText etNickName,etAccount,etSchool,etSign;

    private RadioButton rbBoy,rbGirl;

    private AppCompatSpinner spinnerCity;

    private String[] cities;

    private int selectedCityPosition;
    private String selectedCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        initView();
        initData();

        initEvent();
    }
    private void initView() {
        etAccount = findViewById(R.id.et_account_text);
        etNickName = findViewById(R.id.et_nick_name);
        etSchool = findViewById(R.id.et_school_text);
        etSign = findViewById(R.id.et_sign_text);

        rbBoy = findViewById(R.id.rb_boy);
        rbGirl = findViewById(R.id.rb_girl);
        spinnerCity = findViewById(R.id.sp_city);


    }


    private void initData() {
        cities = getResources().getStringArray(R.array.cities);

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


        etAccount.setText(account);
        etNickName.setText(nickName);
        etSchool.setText(school);
        etSign.setText(sign);

        if(TextUtils.equals("男",gender)){
            rbBoy.setChecked(true);
        }

        if(TextUtils.equals("女",gender)){
            rbGirl.setChecked(true);
        }

        for (int i = 0; i < cities.length; i++) {
            if(TextUtils.equals(cities[i],city)){
                selectedCityPosition = i;
                break;
            }
        }

        spinnerCity.setSelection(selectedCityPosition);


    }
    private void initEvent() {
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                selectedCityPosition = position;
                selectedCity = cities[position];
                Log.d(TAG,"onItemSelected:--------position--------"+position);
                Log.d(TAG,"onItemSelected:--------selectedCity--------"+selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void save(View view) {

        /** String account = spfRecord.getString("account"," ");
         String nickname = spfRecord.getString("nick_name"," ");
         String city = spfRecord.getString("city"," ");
         String gender = spfRecord.getString("gender"," ");
         String school = spfRecord.getString("school"," ");
         String home = spfRecord.getString("home"," ");
         String sign = spfRecord.getString("sign"," ");
         *
         * */

        String account = etAccount.getText().toString();
        String sign = etSign.getText().toString();
        String school = etSchool.getText().toString();
        String nickName = etNickName.getText().toString();

        String gender = "男";
        if(rbBoy.isChecked()){
            gender = "男";
        }
        if(rbGirl.isChecked()){
            gender = "女";
        }

        SharedPreferences spfRecord = getSharedPreferences("spfRecord",MODE_PRIVATE);
        SharedPreferences.Editor edit = spfRecord.edit();
        edit.putString("account",account);
        edit.putString("nick_name",nickName);
        edit.putString("school",school);
        edit.putString("sign",sign);
        edit.putString("city",selectedCity);
        edit.putString("gender",gender);
        edit.apply();

        this.finish();
    }
}