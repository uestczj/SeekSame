package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.db.Groups;
import com.example.caizejian.seeksamehobbies.db.Posts;
import com.example.caizejian.seeksamehobbies.db.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddPostActivity extends MyActivity {

    private EditText mTitle;
    private EditText mContent;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
      //  Bmob.initialize(this,"11ff78e4bfc361f495f3d1e475b32ec2");
        Intent intent = getIntent();
       // Toast.makeText(this,""+intent.getStringExtra("group_id"),Toast.LENGTH_SHORT).show();
        mId = intent.getStringExtra("group_id");
        mTitle = (EditText)findViewById(R.id.edit_title);
        mContent = (EditText)findViewById(R.id.edit_content);
    }

    public void btn_add_post(View view){
        User user = BmobUser.getCurrentUser(User.class);
        Posts posts = new Posts();
        posts.setTitle(mTitle.getText().toString());
        posts.setContent(mContent.getText().toString());
        posts.setAuthor(user);
        Groups groups = new Groups();
        groups.setObjectId(mId);
        posts.setGroups(groups);
        posts.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Toast.makeText(AddPostActivity.this,"successful",Toast.LENGTH_SHORT).show();
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
}
