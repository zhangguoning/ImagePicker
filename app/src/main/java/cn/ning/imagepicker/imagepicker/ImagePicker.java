package cn.ning.imagepicker.imagepicker;

import android.content.Context;
import android.content.Intent;


import java.util.ArrayList;
import java.util.List;

import cn.ning.imagepicker.imagepicker.activity.GalleryActivity;
import cn.ning.imagepicker.imagepicker.activity.ImageSourceDialogActivity;
import cn.ning.imagepicker.imagepicker.activity.PickImageActivity;
import cn.ning.imagepicker.imagepicker.activity.ShowAllPhotoActivity;
import cn.ning.imagepicker.imagepicker.callback.CallBack;
import cn.ning.imagepicker.imagepicker.util.Container;

/**
 * Created by ning on 2016/7/11.
 * Summary :
 */
public class ImagePicker {

    private Context context;
    private boolean isMultiple; //是否为选取多张图片模式
    private List<String> existImage; // 已经选取的图片地址
    private int max  = -1; //最大可选取的图片数量，-1为没有上限
    private CallBack.ImagePickerListener listener;

    public ImagePicker(Context context, boolean isMultiple , int max , ArrayList<String> existImage , CallBack.ImagePickerListener listener){
        this.context = context ;
        this.isMultiple = isMultiple;
        Container.MAX = max ;
        this.listener = listener ;
        PickImageActivity.listener = listener ;
        ShowAllPhotoActivity.listener = listener ;
        GalleryActivity.listener = listener ;
        ImageSourceDialogActivity.listener = listener ;
        if(existImage == null ){
            Container.AllChoiceImage = new ArrayList<>();
        }else{
            Container.AllChoiceImage = existImage;
        }
    }

    public void showImagePickerDialog(){
        Intent intent = new Intent(context,ImageSourceDialogActivity.class);
        intent.putExtra("isMultiple",isMultiple);
        context.startActivity(intent);
    }
}
