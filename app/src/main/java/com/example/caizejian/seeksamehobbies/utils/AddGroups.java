package com.example.caizejian.seeksamehobbies.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caizejian.seeksamehobbies.R;
import com.example.caizejian.seeksamehobbies.db.Groups;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by caizejian on 2017/3/29.
 */

public class AddGroups extends AppCompatActivity {

    EditText name;
    EditText desc;
    private static final  int IMAGE_REQUEST_CODE = 1;
    private static final  int RESULT_REQUEST_CODE = 2;
    private File tempFile = null;
    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    private Boolean IsUpload=true;
    private Intent data;
    private BmobFile bmobFile;
    private Bitmap bitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Bmob.initialize(this,"11ff78e4bfc361f495f3d1e475b32ec2");

    }
    public void button(View view){

        setImageToView(bmobFile);

    }

    public void button1(View view){

        toPicture();
    }

    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case IMAGE_REQUEST_CODE: // 相册数据
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case RESULT_REQUEST_CODE: // 有可能点击舍弃
                if (data != null) {
                    // 拿到图片设置, 然后需要删除tempFile
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                        bmobFile = new BmobFile(bitmapToFile(bitmap));
                    }
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogUtils.e("JAVA", "裁剪uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // 裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        // 发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    private void setImageToView(BmobFile bmobFile) {
        final BmobFile bmobFile1 = bmobFile;
        if (bmobFile1 != null) {
            bmobFile1.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        String mName = name.getText().toString();
                        String mDesc = desc.getText().toString();
                        Groups groups = new Groups();
                        groups.setGroupName(mName);
                        groups.setDesc(mDesc);
                        groups.setGroupImage(bmobFile1);
                        groups.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Toast.makeText(AddGroups.this,"successful",Toast.LENGTH_SHORT).show();
                                    name.setText("");
                                    desc.setText("");
                                }else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public File bitmapToFile(Bitmap bitmap) {
        tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                bos.flush();
                bos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }
}
