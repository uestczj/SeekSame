package com.example.caizejian.seeksamehobbies;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by caizejian on 2017/5/12.
 */

public class ImageShow extends BmobObject {

    private BmobFile Images;

    public BmobFile getImages() {
        return Images;
    }

    public void setImages(BmobFile images) {
        Images = images;
    }
}
