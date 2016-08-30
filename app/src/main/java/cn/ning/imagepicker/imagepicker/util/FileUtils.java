package cn.ning.imagepicker.imagepicker.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.ning.imagepicker.R;

public class FileUtils {
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_LJ/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); 
			else if (file.isDirectory())
				deleteDir(); 
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
	/**
	 * 把ic_launcher考到程序的目录下
	 * @param context
	 * @param path 文件路径
	 * @param dbPathAndName
	 */
	public static void copyLauncherIconToSD(Context context, String path, String dbPathAndName) {
		InputStream is = null;
		FileOutputStream fos = null;
		File file;
		try {
//			String path = android.os.Environment.getExternalStorageDirectory()
//					.getPath();
//			String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
//			path = path + "/iconimage";
//
//			String dbPathAndName = path + "/" + "iconimage.png";

			file = new File(path);

			if (file.exists() == false)
			{

				file.mkdir();
			}

			File dbFile = new File(dbPathAndName);
			if (!dbFile.exists()) {
				is = context.getResources().openRawResource(
						R.raw.ic_launcher);
				fos = new FileOutputStream(dbFile);

				byte[] buffer = new byte[8 * 1024];// 8K
				while (is.read(buffer) > 0)// >
				{
					fos.write(buffer);
				}
			}

		} catch (Exception e) {

		} finally {
			try {
				if (is != null) {
					is.close();
				}

				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}

}
