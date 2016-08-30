package cn.ning.imagepicker.imagepicker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ning.imagepicker.R;
import cn.ning.imagepicker.imagepicker.callback.CallBack;
import cn.ning.imagepicker.imagepicker.util.Container;
import cn.ning.imagepicker.imagepicker.util.ImagePickerConstants;
import cn.ning.imagepicker.imagepicker.util.PicUtil;

/**
 * Created by ning on 2016/7/11.
 * Summary :
 */
public class ImageSourceDialogActivity extends Activity implements View.OnClickListener{

    private Button pop_iamgepick_camera_but , pop_iamgepick_photo_but ,
                   pop_iamgepick_cancel_but ;
    private LinearLayout pop_iamgepick_ll;
    private RelativeLayout root_rl;
    private Context context;
    private boolean isMultiple;
    private String newPhotoName;
    private Uri takePhotoUri ;
    private String picturePath;
    public static CallBack.ImagePickerListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = R.style.in_out_anmi;
        window.setAttributes(lp);

        setContentView(R.layout.activity_pted_pickimager);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById();
        initData();
        initView();
        Container.activityMap.put(this.getClass().getSimpleName(), this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.i("ImageSourceDialogActivity()","onSaveInstanceState()");
        outState.putString("picturePath",picturePath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.i("ImageSourceDialogActivity()","onSaveInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            String picturePath = savedInstanceState.getString("picturePath", null);
            if(TextUtils.isEmpty(picturePath)){
                if(listener!=null){
                    listener.onCancel();
                }
            }else{
                Container.AllChoiceImage.add(picturePath);
                if (listener!=null){
                    listener.onPicked(Container.AllChoiceImage);
                }
            }
            Container.finishAll(ImageSourceDialogActivity.this);
        }


    }

    private void findViewById(){

        pop_iamgepick_camera_but = (Button) this
                .findViewById(R.id.pop_iamgepick_camera_but);
        pop_iamgepick_photo_but = (Button) this
                .findViewById(R.id.pop_iamgepick_photo_but);
        pop_iamgepick_cancel_but = (Button) this
                .findViewById(R.id.pop_iamgepick_cancel_but);
        pop_iamgepick_ll = (LinearLayout) this.findViewById(R.id.pop_iamgepick_ll);
        root_rl = (RelativeLayout) this.findViewById(R.id.root_rl);
    }

    private void initData(){
        isMultiple = getIntent().getBooleanExtra("isMultiple",false);
        context = this ;
    }

    private void initView(){
        pop_iamgepick_camera_but.setOnClickListener(this);
        pop_iamgepick_photo_but.setOnClickListener(this);
        pop_iamgepick_cancel_but.setOnClickListener(this);
        root_rl.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_iamgepick_camera_but:

                photograph();
//                this.finish();
                break;

            case R.id.pop_iamgepick_photo_but:
                if(isMultiple){
                    Intent intent = new Intent(context, PickImageActivity.class);
                    context.startActivity(intent);
                    this.finish();
                }else{
                    selectImageFromAlbum();
                }
                break;

            case R.id.pop_iamgepick_cancel_but:

                this.finish();
                break;

            case R.id.root_rl:
                this.finish();
                break;
        }

    }
    /**
     * 拍照
     */
    private void photograph(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        SimpleDateFormat PIC_NAME_FMT = new SimpleDateFormat("yyMMddHHmmss");
        Date now = new Date();
        newPhotoName = PIC_NAME_FMT.format(now);
        takePhotoUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), newPhotoName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
        startActivityForResult(intent, ImagePickerConstants.SENDOUT_PHOTOSHOW);
    }

    /**
     * 从相册选取图片
     */
    private void selectImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ImagePickerConstants.IMAGE_UNSPECIFIED);
        startActivityForResult(intent, ImagePickerConstants.SENDOUT_PHOTORESOULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("ImageSourceDialogActivity()","onActivityResult() requestCode = " + requestCode);
        if(requestCode == ImagePickerConstants.SENDOUT_PHOTOSHOW){// 拍照返回
            picturePath = Environment.getExternalStorageDirectory() + "/" + newPhotoName;
            if(!new File(picturePath).exists()){
                if(listener!=null){
                    listener.onCancel();
                }
                Container.finishAll(ImageSourceDialogActivity.this);
                return;
            }
            // FIXME : 保存到相册
            if (data != null) { // 可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT,
                // uri);
                // 返回有缩略图
                if (data.hasExtra("data")) {
                    final Bitmap bitmap = data.getParcelableExtra("data");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 得到bitmap后的操作
                            PicUtil.saveImageToGallery(context,bitmap);
                        }
                    }).start();
                }
            } else {
                // 由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                // 通过目标uri，找到图片
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 得到bitmap后的操作
                            Bitmap bmp;
                            try {
                                bmp = getThumbnail(takePhotoUri, 500);
                                PicUtil.saveImageToGallery(context,bmp);
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if(picturePath!=null && picturePath.length()>0){
                Container.AllChoiceImage.add(picturePath);
            }
            if(listener!=null){
                listener.onPicked(Container.AllChoiceImage);
            }
            Container.finishAll(ImageSourceDialogActivity.this);
        }
        if(requestCode == ImagePickerConstants.SENDOUT_PHOTORESOULT){
            if (data == null || data.getData() == null) {

                if(listener!=null){
                    listener.onCancel();
                }
                Container.finishAll(ImageSourceDialogActivity.this);
            }
            picturePath = imageFitfer(data);
            if(TextUtils.isEmpty(picturePath)){
                if(listener!=null){
                    listener.onCancel();
                }
                Container.finishAll(ImageSourceDialogActivity.this);
            }else if(picturePath.length() <= 1){
                Toast.makeText(context, "本地找不到该图片,请确认您选择的图片在本地存放", Toast.LENGTH_LONG).show();
                if(listener!=null){
                    listener.onCancel();
                }
                Container.finishAll(ImageSourceDialogActivity.this);

            }else{
                Container.AllChoiceImage.add(picturePath);
                if(listener!=null){
                    listener.onPicked(Container.AllChoiceImage);
                }
                Container.finishAll(ImageSourceDialogActivity.this);
            }
        }
    }
    public Bitmap getThumbnail(Uri uri, int size) throws FileNotFoundException,
            IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1)
                || (onlyBoundsOptions.outHeight == -1))
            return null;
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
                : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }
    private int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }
    private String imageFitfer(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Log.d(this.getClass().getSimpleName(), uri.getPath());
                String scheme = uri.getScheme();
                String filePath = "";
                if ("content".equals(scheme)) {// android 4.4以上版本处理方式
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                            && DocumentsContract.isDocumentUri(context,
                            uri)) {
                        String wholeID = DocumentsContract
                                .getDocumentId(uri);
                        String id = wholeID.split(":")[1];
                        String[] column = {MediaStore.Images.Media.DATA};
                        String sel = MediaStore.Images.Media._ID + "=?";
                        Cursor cursor = context
                                .getContentResolver()
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel,
                                        new String[]{id}, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndex = cursor
                                    .getColumnIndex(column[0]);
                            filePath = cursor.getString(columnIndex);

                            cursor.close();
                            return filePath;
                        } else {
                            return null;
                        }
                    } else {// android 4.4以下版本处理方式
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri,
                                filePathColumn, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int columnIndex = cursor
                                    .getColumnIndex(filePathColumn[0]);
                            filePath = cursor.getString(columnIndex);
                            Log.d(this.getClass().getSimpleName(), "filePath" + filePath);
                            cursor.close();
                            return filePath;
                        } else {
                            return null;
                        }
                    }
                } else if ("file".equals(scheme)) {// 小米云相册处理方式
                    return uri.getPath();
                } else {
                    return null;
                }

            } else {
                return null;
            }

        }
        return null;
    }
}


