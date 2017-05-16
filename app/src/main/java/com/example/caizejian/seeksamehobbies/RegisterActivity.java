package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.db.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import layout.RegisterFragment;

public class RegisterActivity extends MyActivity {

    EditText userName;
    EditText userId;
    EditText userPwd;
    EditText confirmPwd;
    EditText userDescride;
    Boolean mResult = true;
    String pwd;
    String cPwd;
    String name;
    String mId;
    String mDesc;
    public Button button;
    int UserDataCode;
    Toolbar toolbar;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        linearLayout = new LinearLayout(RegisterActivity.this);

        final Intent intent = getIntent();
        UserDataCode = intent.getIntExtra("register_edit",-2);
        RegisterFragment registerFragment = (RegisterFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_test);
        View view = registerFragment.getView();

        userName = (EditText)view.findViewById(R.id.et_userName);
        userId = (EditText)view.findViewById(R.id.et_userId);
        userPwd = (EditText)view.findViewById(R.id.et_pass);
        confirmPwd = (EditText)view.findViewById(R.id.et_password);
        userDescride = (EditText)view.findViewById(R.id.et_desc);
        button = (Button)view.findViewById(R.id.button);


        if(UserDataCode == STARTEDITDATA){
            initView();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_edit_data();
                }
            });
        }else if(UserDataCode == STARTREGISTER){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_register();
                }
            });
        }
    }

    public void btn_register(){
        mResult=UserNameIsUsed();
        pwd = userPwd.getText().toString();
        cPwd = confirmPwd.getText().toString();
        name = userName.getText().toString();
        mId = userId.getText().toString();
        mDesc = userDescride.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mResult){
                            Toast.makeText(RegisterActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
                        }else{
                            if(pwd.isEmpty()){
                                Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                            }else if(!pwd.equals(cPwd)){
                                Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                            }else {
                                User mUser = new User();
                                mUser.setUsername(name);
                                mUser.setPassword(pwd);
                                mUser.setUserId(mId);
                                mUser.setUserDescribe(mDesc);
                                mUser.signUp(new SaveListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();

                                            e.printStackTrace();}}});
                            }
                        }
                    }
                });
            }
        }).start();


    }

    public void btn_edit_data()
    {
       // mResult=UserNameIsUsed();
        pwd = userPwd.getText().toString();
        cPwd = confirmPwd.getText().toString();
        name = userName.getText().toString();
        mId = userId.getText().toString();
        mDesc = userDescride.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        User mUser = BmobUser.getCurrentUser(User.class);
                            if(!pwd.equals(cPwd)){
                                Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                            }else if(! pwd.isEmpty()){
                                mUser.setPassword(pwd);
                                mUser.setUsername(name);
                                mUser.setUserId(mId);
                                mUser.setUserDescribe(mDesc);
                                mUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(RegisterActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(RegisterActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();}}});
                            }else {
                                mUser.setUsername(name);
                                mUser.setUserId(mId);
                                mUser.setUserDescribe(mDesc);
                                mUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(RegisterActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(RegisterActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();}}});
                            }
                    }
                });
            }
        }).start();
    }


    public void initView(){
        User user = BmobUser.getCurrentUser(User.class);
        userName.setText(user.getUsername());
        userId.setText(user.getUserId());
        userDescride.setText(user.getUserDescribe());
        button.setText("确认修改");
        toolbar.setTitle("编辑个人资料");
        userName.setFocusable(false);
        userId.setFocusable(false);

    }

    public Boolean UserNameIsUsed(){
                    name = userName.getText().toString();
                   BmobQuery<User> query = new BmobQuery<User>();
                   query.addWhereEqualTo("username",name);
                   query.findObjects(new FindListener<User>() {
                       @Override
                       public void done(List<User> list, BmobException e) {
                           if(e == null){
                               if(list.size()!=0){
                                   mResult = true;
                               }else{
                                   mResult = false;
                               }
                           }else {
                               e.printStackTrace();}}});
        return mResult;
    }

   private void replaceFragment(Fragment fragment){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_test,fragment);
        transaction.commit();
    }
}
