package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.db.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends MyActivity {

    private EditText userName;
    private EditText userPwd;
    private Boolean correct=false;
    private CheckBox rembPwd;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private  String name;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText)findViewById(R.id.edit_userName);
        userPwd = (EditText)findViewById(R.id.edit_userPwd);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rembPwd = (CheckBox)findViewById(R.id.rem_pwd);
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if(isRemember){
            String account = preferences.getString("account","");
            String password = preferences.getString("password","");
            userName.setText(account);
            userPwd.setText(password);
            rembPwd.setChecked(true);
        }
    }

    public void btn_login(View view){
        name = userName.getText().toString();
        pwd = userPwd.getText().toString();
        User user = new User();
        user.setUsername(name);
        user.setPassword(pwd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user,BmobException e) {
                if (e == null) {
                    editor = preferences.edit();
                    if (rembPwd.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", name);
                        editor.putString("password", pwd);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "登陆成功！"+user.getObjectId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
                    intent.putExtra("groupslist",STARTGROUPSLIST);
                    startActivity(intent);
                } else {
                    int ErrorCode = e.getErrorCode();
                    if (ErrorCode == 9016) {
                        Toast.makeText(LoginActivity.this, "无网络连接，请检查您的手机网络!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        userName.setText("");
                        userPwd.setText("");
                    }
                }
            }
        });
    }

    public void tv_register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        intent.putExtra("register_edit",STARTREGISTER);
        startActivity(intent);
    }


}
