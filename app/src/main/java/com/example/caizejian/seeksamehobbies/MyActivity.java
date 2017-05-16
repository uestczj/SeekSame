package com.example.caizejian.seeksamehobbies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by caizejian on 2017/3/30.
 */

public class MyActivity extends AppCompatActivity {
    public final static int STARTPOSTSLIST = 1;
    public final static int STARTMYPOSTSLIST = 2;
    public final static int STARTGROUPSLIST = 3;
    public final static int STARTMYGROUPSLIST = 4;
    public final static int STARTEDITDATA = 5;
    public final static int STARTREGISTER = 6;




    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Bmob.initialize(this,"11ff78e4bfc361f495f3d1e475b32ec2");
    }
}
