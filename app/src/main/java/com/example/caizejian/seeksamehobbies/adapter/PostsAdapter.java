package com.example.caizejian.seeksamehobbies.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.caizejian.seeksamehobbies.R;
import com.example.caizejian.seeksamehobbies.db.Posts;
import com.example.caizejian.seeksamehobbies.db.User;

import java.util.List;

/**
 * Created by caizejian on 2017/3/29.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private Context mContext;
    private List<Posts> mPostsList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title;
        TextView content;
        TextView createTime;
        ImageView imageView;

        TextView author;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
          //  title = (TextView)view.findViewById(R.id.text_title);

            imageView = (ImageView)view.findViewById(R.id.iv_img);
            author = (TextView)view.findViewById(R.id.tv_author);
            createTime = (TextView)view.findViewById(R.id.tv_created_time);
            content = (TextView)view.findViewById(R.id.tv_content);
        }
    }

    public PostsAdapter(List<Posts>postsList){
        mPostsList = postsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.posts_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Posts posts = mPostsList.get(position);
      //  holder.title.setText(posts.getTitle());
      //  holder.author.setText(posts.getAuthor().getName());
        holder.content.setText(posts.getContent());
        holder.createTime.setText(posts.getCreatedAt());
        Glide.with(mContext).load(posts.getPic().getFileUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount(){
        return mPostsList.size();
    }
}