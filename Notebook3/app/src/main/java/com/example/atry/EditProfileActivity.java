package com.example.atry;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.atry.user.UserDatabaseHelper;


public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG ="tag";

    private EditText etNickName,etAccount,etSchool,etSign;

    private RadioButton rbBoy,rbGirl;

    private AppCompatSpinner spinnerCity;

    private String[] cities;

    private int selectedCityPosition;
    private String selectedCity;
    private MediaPlayer mediaPlayer;
    private TextView tvMusic;
    private boolean isPlaying = false;
    private static final String PREFS_NAME = "AvatarPrefs";
    private static final String KEY_AVATAR = "selectedAvatar";
    private SharedPreferences sharedPreferences;
    private UserDatabaseHelper dbHelper;
    private String userId;
    private ImageView ivAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);
        dbHelper = new UserDatabaseHelper(this);
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "NO");

        if (userId == "NO") {
            // 如果 user_id 不存在，则跳转到登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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


        ivAvatar = findViewById(R.id.iv_avater);
        Button btChoose = findViewById(R.id.bt_choose);
        sharedPreferences = getSharedPreferences(PREFS_NAME, EditProfileActivity.MODE_PRIVATE);
        // 从 SharedPreferences 中读取头像信息并显示
        String selectedAvatar = sharedPreferences.getString(KEY_AVATAR, null);
        if (selectedAvatar != null) {
            int resourceId = getResources().getIdentifier(selectedAvatar, "mipmap", getPackageName());
            ivAvatar.setImageResource(resourceId);
        }
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarSelectionDialog();
            }
        });
    }


    private void initData() {
        cities = getResources().getStringArray(R.array.cities);
        // 从数据库加载用户信息
        Cursor cursor = dbHelper.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
            String account = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_ACCOUNT));
            String nickName = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_NICKNAME));
            String city = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_CITY));
            String gender = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_GENDER));
            String school = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_SCHOOL));
            String sign = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_SIGN));
            String avatar = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_AVATAR)); // 获取头像


            etAccount.setText(account);
            etNickName.setText(nickName);
            etSchool.setText(school);
            etSign.setText(sign);

            if (TextUtils.equals("男", gender)) {
                rbBoy.setChecked(true);
            } else if (TextUtils.equals("女", gender)) {
                rbGirl.setChecked(true);
            }

            for (int i = 0; i < cities.length; i++) {
                if (TextUtils.equals(cities[i], city)) {
                    selectedCityPosition = i;
                    break;
                }
            }
            spinnerCity.setSelection(selectedCityPosition);
            if (avatar != null) {
                int resourceId = getResources().getIdentifier(avatar, "mipmap", getPackageName());
                ivAvatar.setImageResource(resourceId);
            }
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
        String account = etAccount.getText().toString();
        String sign = etSign.getText().toString();
        String school = etSchool.getText().toString();
        String nickName = etNickName.getText().toString();

        String gender = "男";
        if (rbBoy.isChecked()) {
            gender = "男";
        }
        if (rbGirl.isChecked()) {
            gender = "女";
        }

        // 获取 SharedPreferences 中的头像文件名
        String avatar = sharedPreferences.getString(KEY_AVATAR, "img"); // 默认头像为 img

        // 检查 userId 是否有效
        if (userId.equals("NO")) {
            Toast.makeText(this, "用户未登录，无法保存数据", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建 ContentValues 对象，用于保存用户数据
        ContentValues values = new ContentValues();
        values.put(UserDatabaseHelper.COLUMN_ACCOUNT, account);
        values.put(UserDatabaseHelper.COLUMN_NICKNAME, nickName);
        values.put(UserDatabaseHelper.COLUMN_CITY, selectedCity);
        values.put(UserDatabaseHelper.COLUMN_GENDER, gender);
        values.put(UserDatabaseHelper.COLUMN_SCHOOL, school);
        values.put(UserDatabaseHelper.COLUMN_SIGN, sign);
        values.put(UserDatabaseHelper.COLUMN_AVATAR, avatar); // 保存头像资源名
        values.put(UserDatabaseHelper.USER_ID, userId);

        // 检查数据库中是否已存在该 userId
        Cursor cursor = dbHelper.getUserById(userId);
        if (cursor != null && cursor.moveToFirst()) {
            // 如果记录存在，执行更新操作
            int rowsAffected = dbHelper.getWritableDatabase().update(
                    UserDatabaseHelper.TABLE_USERS,
                    values,
                    UserDatabaseHelper.USER_ID + "=?",
                    new String[]{userId} // 使用 userId 作为更新条件
            );

            if (rowsAffected > 0) {
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            // 如果记录不存在，执行插入操作
            long newRowId = dbHelper.getWritableDatabase().insert(
                    UserDatabaseHelper.TABLE_USERS,
                    null,  // 插入数据时，不指定列，直接插入
                    values
            );

            if (newRowId != -1) {
                Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
            }
        }

        // 完成后关闭当前 Activity
        finish();
    }




    private void showAvatarSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_avatar_selection, null);
        builder.setView(dialogView);

        final ImageView avatar1 = dialogView.findViewById(R.id.avatar1);
        final ImageView avatar2 = dialogView.findViewById(R.id.avatar2);
        final ImageView avatar3 = dialogView.findViewById(R.id.avatar3);
        final ImageView avatar4 = dialogView.findViewById(R.id.avatar4);
        final ImageView avatar5 = dialogView.findViewById(R.id.avatar5);
        final ImageView avatar6 = dialogView.findViewById(R.id.avatar6);
        final ImageView avatar7 = dialogView.findViewById(R.id.avatar7);
        final ImageView avatar8 = dialogView.findViewById(R.id.avatar8);
        AlertDialog dialog = builder.create();
        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img");
                dialog.dismiss();
            }
        });

        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_1");
                dialog.dismiss();
            }
        });

        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_2");
                dialog.dismiss();
            }
        });

        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_3");
                dialog.dismiss();
            }
        });

        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_4");
                dialog.dismiss();
            }
        });

        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_5");
                dialog.dismiss();
            }
        });

        avatar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_6");
                dialog.dismiss();
            }
        });

        avatar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar("img_7");
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void selectAvatar(String avatarName) {
        int resourceId = getResources().getIdentifier(avatarName, "mipmap", getPackageName());
        ivAvatar.setImageResource(resourceId);

        // 将选择的头像存储在 SharedPreferences 中
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AVATAR, avatarName);
        editor.apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}