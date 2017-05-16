package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.adapter.GroupsAdapter;
import com.example.caizejian.seeksamehobbies.db.Groups;
import com.example.caizejian.seeksamehobbies.db.User;
import com.example.caizejian.seeksamehobbies.loader.GlideImageLoader;
import com.youth.banner.Banner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GroupsActivity extends MyActivity {

    private List<Groups> groupsList = new ArrayList<>();
    private GroupsAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private int groupslistCode;
    List<ImageShow> imageShowList = new ArrayList<>();
    Banner banner;
    LinearLayout frameLayout;
    LinearLayout linearLayout;
    TextView nav_view;
    TextView nav_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        frameLayout = (LinearLayout)findViewById(R.id.frameLayout);
        linearLayout = (LinearLayout)findViewById(R.id.lineLayout1);
        final Intent intent = getIntent();
        groupslistCode = intent.getIntExtra("groupslist",-2);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_groupslist);

        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.draw_layout_groupslists);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        initImagesList();
        View anotherHeader = LayoutInflater.from(this).inflate(R.layout.header, null);
        banner = (Banner) anotherHeader.findViewById(R.id.banner);
        banner.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,   App.H / 3));
        frameLayout.addView(banner);
        navigationView.setCheckedItem(R.id.nav_myposts);
     //   View view = navigationView.inflateHeaderView(R.layout.nav_header);

        View view = navigationView.getHeaderView(0);
        nav_view = (TextView)view.findViewById(R.id.nav_userId);
        nav_name = (TextView)view.findViewById(R.id.nav_username);
        initNavUserMessage();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_myposts:
                        Intent intent = new Intent(GroupsActivity.this,PostsListActivity.class);
                        intent.putExtra("postslist",STARTMYPOSTSLIST);
                        startActivity(intent);
                    break;
                    case R.id.nav_mygroups:
                        Intent intent1 = new Intent(GroupsActivity.this,GroupsActivity.class);
                        intent1.putExtra("groupslist",STARTMYGROUPSLIST);
                        startActivity(intent1);
                        break;
                    case R.id.nav_editdata:
                        Intent intent2 = new Intent(GroupsActivity.this,RegisterActivity.class);
                        intent2.putExtra("register_edit",STARTEDITDATA);
                        startActivity(intent2);
                        break;
                    case R.id.nav_logout:
                        Intent intent3 = new Intent(GroupsActivity.this,LoginActivity.class);
                        startActivity(intent3);
                        Toast.makeText(GroupsActivity.this,"logout",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        if(groupslistCode == STARTGROUPSLIST){
            initGroups();
        }else {
            initMyGroups();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
                        GridLayoutManager layoutManager = new GridLayoutManager(GroupsActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new GroupsAdapter(groupsList);
                        recyclerView.setAdapter(adapter);
                        String[] url = new String[5];
                        int i = 0;
                        for(ImageShow imageShow : imageShowList)
                        {

                            url[i] = imageShow.getImages().getFileUrl();
                            i++;
                        }
                        List list = Arrays.asList(url);
                        List arrayList = new ArrayList(list);
                        banner.setImages(arrayList)
                                .setImageLoader(new GlideImageLoader())
                                .start();

                    }
                });

            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    public void initNavUserMessage(){
        User user = BmobUser.getCurrentUser(User.class);
        nav_view.setText(user.getUserId());
        nav_name.setText(user.getUsername());
    }

    public void initGroups(){
        groupsList.clear();
        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BmobQuery<Groups> query = new BmobQuery<Groups>();
        query.addWhereGreaterThan("createdAt",new BmobDate(date));
        query.findObjects(new FindListener<Groups>() {
            @Override
            public void done(List<Groups> list, BmobException e) {
                if(e==null) {
                    for (Groups groups : list) {
                        groupsList.add(groups);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
    public void initMyGroups(){
        groupsList.clear();
        BmobQuery<Groups> query = new BmobQuery<Groups>();
        User user = BmobUser.getCurrentUser(User.class);
        query.addWhereEqualTo("memOfUser",user);
        query.findObjects(new FindListener<Groups>() {
            @Override
            public void done(List<Groups> list, BmobException e) {
                if(e == null){
                    for(Groups groups : list){
                        groupsList.add(groups);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initImagesList(){
        imageShowList.clear();
        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try{
            data = sdf.parse(start);
        }catch (ParseException e){
            e.printStackTrace();
        }
        BmobQuery<ImageShow> query = new BmobQuery<ImageShow>();
        query.addWhereGreaterThan("createdAt",new BmobDate(data));
        query.findObjects(new FindListener<ImageShow>() {
            @Override
            public void done(List<ImageShow> list, BmobException e) {
                if(e == null){
                    for(ImageShow imageShow : list) {
                        imageShowList.add(imageShow);
                    }
                }else {
                    e.printStackTrace();
                }}
        });
    }

}
