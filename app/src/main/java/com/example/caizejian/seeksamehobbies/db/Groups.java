package com.example.caizejian.seeksamehobbies.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by caizejian on 2017/3/28.
 */

public class Groups extends BmobObject {

    private String groupName;
    private BmobFile groupImage;
    private String desc;
    private int numOfPost;
    private int numOfUser;
    private BmobRelation memOfUser;
    private BmobFile groupBGImage;

    public BmobFile getGroupBGImage() {
        return groupBGImage;
    }

    public void setGroupBGImage(BmobFile groupBGImage) {
        this.groupBGImage = groupBGImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public BmobFile getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(BmobFile groupImage) {
        this.groupImage = groupImage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumOfPost() {
        return numOfPost;
    }

    public void setNumOfPost(int numOfPost) {
        this.numOfPost = numOfPost;
    }

    public int getNumOfUser() {
        return numOfUser;
    }

    public void setNumOfUser(int numOfUser) {
        this.numOfUser = numOfUser;
    }

    public BmobRelation getMemOfUser() {
        return memOfUser;
    }

    public void setMemOfUser(BmobRelation memOfUser) {
        this.memOfUser = memOfUser;
    }
}
