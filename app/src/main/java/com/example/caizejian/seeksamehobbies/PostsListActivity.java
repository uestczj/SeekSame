package com.example.caizejian.seeksamehobbies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.caizejian.seeksamehobbies.adapter.PostsAdapter;
import com.example.caizejian.seeksamehobbies.db.Groups;
import com.example.caizejian.seeksamehobbies.db.Posts;
import com.example.caizejian.seeksamehobbies.db.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import zhangyf.vir56k.androidimageblur.BlurUtil;

public class PostsListActivity extends MyActivity {

    private TextView groupName;
    private TextView groupDesc;
    private Button addPosrts;
    private Button addGroup;
    private String groupId;
    private List<Posts>postsList=new ArrayList<>();
    private PostsAdapter adapter;
    private   String userObjectId;
    private int intentCode;
    private DrawerLayout mDrawerLayout;
    private LinearLayout layout;
    private ImageView imageView;
    private Button button;
    private List<Groups>groupsList = new ArrayList<>();
    private List<User>userList = new ArrayList<>();
    private int IsAddInGroup = 0;
    private Bitmap bitmap;
    private boolean IsInitButton = true;
    private String group_name;
    private String use_id;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        group_name = intent.getStringExtra("group_name");
        String desc = intent.getStringExtra("group_desc");
        String img_url = intent.getStringExtra("group_bg_image");
        String img_url1 = intent.getStringExtra("group_image");
        use_id = User.getCurrentUser().getObjectId();
        intentCode = intent.getIntExtra("postslist",-1);
        if(intentCode==STARTPOSTSLIST){
            setContentView(R.layout.activity_posts_list);
            layout = (LinearLayout)findViewById(R.id.second_linear_layout);
            layout.setMinimumHeight(App.H/3);
            imageView = (ImageView)findViewById(R.id.groups_image1);
           // swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swip_refersh_posts_list);
          //  swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
          /*  swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initPostList();
                }
            });*/
            Glide.with(PostsListActivity.this).load(img_url1).into(imageView);
            new DownloadImageTask().execute(img_url);
            groupName = (TextView)findViewById(R.id.text_group_name);
            groupDesc = (TextView)findViewById(R.id.text_group_desc);
            button = (Button)findViewById(R.id.btn_add_in_group);
            groupName.setText(group_name);
            groupDesc.setText(desc);
            groupId = intent.getStringExtra("group_id");
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_mpostslist);
            setSupportActionBar(toolbar);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.draw_layout_mpostslist);
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_views);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }
            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
            String getText = pre.getString(group_name+"test"+use_id,null);
            int getCode = pre.getInt(group_name+"int"+use_id,-1);
            IsAddInGroup = getCode;
            if(IsAddInGroup == 1){
                button.setText("取消关注");
            }
           

            FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab_add_posts);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,"发帖",Snackbar.LENGTH_LONG)
                            .setAction("我要发帖", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PostsListActivity.this,AddPostActivity.class);
                                    intent.putExtra("group_id",groupId);
                                    startActivity(intent);
                                }
                            }).show();

                }
            });
            navigationView.setCheckedItem(R.id.nav_myposts);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_myposts:
                            Intent intent = new Intent(PostsListActivity.this,PostsListActivity.class);
                            intent.putExtra("postslist",STARTMYPOSTSLIST);
                            startActivity(intent);
                            break;
                        case R.id.nav_mygroups:
                            Intent intent1 = new Intent(PostsListActivity.this,GroupsActivity.class);
                            intent1.putExtra("groupslist",STARTMYGROUPSLIST);
                            startActivity(intent1);
                            break;
                        case R.id.nav_editdata:
                            Toast.makeText(PostsListActivity.this,"editdata",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_logout:
                            Toast.makeText(PostsListActivity.this,"logout",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
            initPostList();

        }else if(intentCode == STARTMYPOSTSLIST){
            setContentView(R.layout.my_posts_list);
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_mypostlist);
            setSupportActionBar(toolbar);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.draw_layout_mpostslist);
            NavigationView navigationView = (NavigationView)findViewById(R.id.nav_viewss);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            navigationView.setCheckedItem(R.id.nav_myposts);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_myposts:
                            Intent intent = new Intent(PostsListActivity.this,PostsListActivity.class);
                            intent.putExtra("postslist",STARTMYPOSTSLIST);
                            startActivity(intent);
                            break;
                        case R.id.nav_mygroups:
                            Intent intent1 = new Intent(PostsListActivity.this,GroupsActivity.class);
                            intent1.putExtra("groupslist",STARTMYGROUPSLIST);
                            startActivity(intent1);
                            break;
                        case R.id.nav_editdata:
                            Toast.makeText(PostsListActivity.this,"editdata",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_logout:
                            Toast.makeText(PostsListActivity.this,"logout",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
            initMyPostList();
        }

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

    public void initPosts(){
        BmobQuery<Posts>query = new BmobQuery<Posts>();
        Groups groups = new Groups();
        groups.setObjectId(groupId);
        query.addWhereEqualTo("groups",groups);
        query.findObjects(new FindListener<Posts>() {
            @Override
            public void done(List<Posts> list, BmobException e) {
                if(e == null){
                    for(Posts posts : list){
                        postsList.add(posts);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initMyPosts(){
        BmobQuery<Posts>query = new BmobQuery<Posts>();
        final User user = BmobUser.getCurrentUser(User.class);
        query.addWhereEqualTo("author",user);
        query.findObjects(new FindListener<Posts>() {
            @Override
            public void done(List<Posts> list, BmobException e) {
                if(e==null){
                    for(Posts posts :list) {
                        postsList.add(posts);
                        Toast.makeText(PostsListActivity.this,"查询"+user.getUsername()+"成功！",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void btn_add_in_group(View view){
        if(IsAddInGroup == 0)
        {
        button.setText("取消关注");
        User user = BmobUser.getCurrentUser(User.class);
        Groups groups = new Groups();
        groups.setObjectId(groupId);
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        groups.setMemOfUser(relation);
        groups.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","成功："+e.getMessage());
                }else {
                    e.printStackTrace();
                }
            }
        });
            IsAddInGroup=1;
        }else {
            button.setText("关注");
            Groups groups = new Groups();
            groups.setObjectId(groupId);
            User user = BmobUser.getCurrentUser(User.class);
            BmobRelation relation = new BmobRelation();
            relation.remove(user);
            groups.setMemOfUser(relation);
            groups.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Log.i("bmob","成功："+e.getMessage());
                    }else{
                        Log.i("bmob","失败："+e.getMessage());
                    }
                }
            });
            IsAddInGroup = 0;
        }
     /*   SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PostsListActivity.this).edit();
        editor.putInt("IsAddInGroup",IsAddInGroup);
        editor.apply();*/
        String text = "test";
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PostsListActivity.this).edit();
        editor.putString(group_name+"test"+use_id,text);
        editor.putInt(group_name+"int"+use_id,IsAddInGroup);
        editor.apply();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        protected void onPostExecute(Drawable result) {

            drawableToBitamp(result);
            Bitmap newImg = BlurUtil.doBlur(bitmap, 3, 2);
            bitmap.recycle();
            layout.setBackground(new BitmapDrawable(getResources(), newImg));
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);

        } catch (IOException e) {
            Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
            Log.d("skythinking", "null drawable");
        } else {
            Log.d("skythinking", "not null drawable");
        }
        return drawable;
    }

    private void isAddInGroup()
    {
        userList.clear();
        BmobQuery<User>query = new BmobQuery<User>();
        Groups groups = new Groups();
        groups.setObjectId(groupId);
        query.addWhereRelatedTo("memOfUser", new BmobPointer(groups));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    for(User user : list){
                        userList.add(user);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
    private void drawableToBitamp(Drawable drawable)
     {
                BitmapDrawable bd = (BitmapDrawable) drawable;
                bitmap = bd.getBitmap();
     }
    private void showBlurBackground() {
        Bitmap img1;
        try {
            img1 = BitmapFactory.decodeStream(getResources().getAssets().open("img2.jpg"));
            //缩放并显示
            Bitmap newImg = BlurUtil.doBlur(img1, 3, 2);
            img1.recycle();

            // rootView.setBackground(new BitmapDrawable(getResources(), newImg));
            layout.setBackground(new BitmapDrawable(getResources(), newImg));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initPostList(){
        isAddInGroup();
        initPosts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(800);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(IsInitButton)
                        {
                            User user = BmobUser.getCurrentUser(User.class);
                            for(User user1 : userList){
                                if(user1.getUsername().matches(user.getUsername())){
                                    IsAddInGroup = 1;
                                    break;
                                }
                            }
                            if(IsAddInGroup == 1){
                                button.setText("取消关注");
                            }}
                        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view_posts_list);
                        GridLayoutManager layoutManager = new GridLayoutManager(PostsListActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new PostsAdapter(postsList);
                        recyclerView.setAdapter(adapter);
                     //   swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initMyPostList(){
        initMyPosts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(800);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view_posts_list);
                        GridLayoutManager layoutManager = new GridLayoutManager(PostsListActivity.this,1);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new PostsAdapter(postsList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}
