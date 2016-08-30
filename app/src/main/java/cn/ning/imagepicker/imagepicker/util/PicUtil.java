package cn.ning.imagepicker.imagepicker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by selena on 2016/7/9.
 */
public class PicUtil {
    public static void saveImageToGallery(final Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(),
                "Qctt");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ImagePickerConstants.isDownLoadPic = false;
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
        ImagePickerConstants.isDownLoadPic = false;
//        ThreadUtils.executeOnMainThread(new Runnable() {
//            @Override
//            public void run() {
//                QcttGlobal.showToast(context,"已保存到相册/Qctt文件夹下");
//            }
//        });

    }
}
