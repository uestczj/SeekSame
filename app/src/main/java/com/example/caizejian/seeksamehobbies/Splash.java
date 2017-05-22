package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.example.caizejian.seeksamehobbies.MyActivity.STARTGROUPSLIST;

public class Splash extends AppCompatActivity {


    private TextView tv_splash;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 判断程序是否是第一次运行

                    Intent intent = new Intent(Splash.this, GroupsActivity.class);
                    intent.putExtra("groupslist",STARTGROUPSLIST);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        // 延时2000ms
        handler.sendEmptyMessageDelayed(1, 2000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        // 设置字体
        Typeface fontType = Typeface.createFromAsset(Splash.this.getAssets(), "fonts/FONT.TTF");
        tv_splash.setTypeface(fontType);
    }
}
