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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.adapter.PostsAdapter;
import com.example.caizejian.seeksamehobbies.db.Groups;
import com.example.caizejian.seeksamehobbies.db.Posts;
import com.example.caizejian.seeksamehobbies.db.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String name = intent.getStringExtra("group_name");
        String desc = intent.getStringExtra("group_desc");
        intentCode = intent.getIntExtra("postslist",-1);
        if(intentCode==STARTPOSTSLIST){
            setContentView(R.layout.activity_posts_list);
            groupName = (TextView)findViewById(R.id.text_group_name);
            groupDesc = (TextView)findViewById(R.id.text_group_desc);
            // userObjectId = intent.getStringExtra("userObjectId");
            groupName.setText(name);
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
        }



      //Toast.makeText(this,""+intent.getStringExtra("group_id"),Toast.LENGTH_SHORT).show();
        if(intentCode==STARTPOSTSLIST){
        initPosts();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
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
        }else if (intentCode == STARTMYPOSTSLIST){
            initMyPosts();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
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

    public void btn_add_posts(View view){
        Intent intent = new Intent(this,AddPostActivity.class);
        intent.putExtra("group_id",groupId);
        startActivity(intent);
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
                        Toast.makeText(PostsListActivity.this,"1  "+postsList.size(),Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(PostsListActivity.this,"successful",Toast.LENGTH_SHORT).show();
                }else {
                    e.printStackTrace();
                }
            }
        });
    }
}