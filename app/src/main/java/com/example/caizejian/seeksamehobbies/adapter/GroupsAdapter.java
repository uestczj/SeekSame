package com.example.caizejian.seeksamehobbies.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.caizejian.seeksamehobbies.PostsListActivity;
import com.example.caizejian.seeksamehobbies.R;
import com.example.caizejian.seeksamehobbies.db.Groups;

import java.util.List;

import static com.example.caizejian.seeksamehobbies.MyActivity.STARTPOSTSLIST;

/**
 * Created by caizejian on 2017/3/29.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private Context mContext;

    private List<Groups>mGroupsList;



    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView groupsImage;
        TextView groupsName;
        View groupView;
        TextView groupNumOfMember;
        TextView groupNumOfPost;



        public ViewHolder(View view){
            super(view);
            groupView = view;
            cardView = (CardView)view;
            groupsName = (TextView)view.findViewById(R.id.text_groups_bigName);
            groupsImage = (ImageView)view.findViewById(R.id.groups_image);
            groupNumOfMember = (TextView)view.findViewById(R.id.num_of_member1);
            groupNumOfPost = (TextView)view.findViewById(R.id.num_of_post1);


        }
    }

    public GroupsAdapter(List<Groups> groupsList ){
        mGroupsList = groupsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }


        final View view = LayoutInflater.from(mContext).inflate(R.layout.groups_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Bitmap bitmap1;
                Groups groups = mGroupsList.get(position);

                final   Intent intent = new Intent(view.getContext(), PostsListActivity.class);
                intent.putExtra("group_name",groups.getGroupName());
                intent.putExtra("group_desc", groups.getDesc());
                intent.putExtra("group_id",groups.getObjectId());
                intent.putExtra("postslist", STARTPOSTSLIST);
                intent.putExtra("group_bg_image",groups.getGroupBGImage().getFileUrl());
                intent.putExtra("group_image",groups.getGroupImage().getFileUrl());
                intent.putExtra("group_num_of_member",groups.getNumOfUser());
                intent.putExtra("group_num_of_post",groups.getNumOfPost());
                view.getContext().startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Groups groups = mGroupsList.get(position);
        holder.groupsName.setText(groups.getGroupName());
        holder.groupNumOfMember.setText(""+groups.getNumOfUser());
        holder.groupNumOfPost.setText(""+groups.getNumOfPost());
        Glide.with(mContext).load(groups.getGroupImage().getFileUrl()).into(holder.groupsImage);
    }

    @Override
    public int getItemCount(){
        return mGroupsList.size();
    }
}
