package cn.ning.imagepicker.imagepicker.util;

import android.app.Activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.ning.imagepicker.imagepicker.activity.GalleryActivity;
import cn.ning.imagepicker.imagepicker.activity.ImageSourceDialogActivity;
import cn.ning.imagepicker.imagepicker.activity.PickImageActivity;
import cn.ning.imagepicker.imagepicker.activity.ShowAllPhotoActivity;

/**
 * 存放所有的list在最后退出时一起关闭
 */
public class Container {
	public static Map<String, Activity> activityMap = new HashMap<String, Activity>();
	public static ArrayList<String> AllChoiceImage ;
	public static int MAX = 9;

	/**
	 * finish 掉图片选择器所有activity（最后一个打开的activity最后一个关闭） ，清空容器， 释放强引用
	 * @param a
     */
	public static void finishAll(Activity a){
		Set<String> keySet = activityMap.keySet();

		for(Iterator<String> it = keySet.iterator(); it.hasNext() ;){
			String ket = it.next();
			if(!ket.equals(a.getClass().getSimpleName())){
				Activity activity = activityMap.get(ket);
				if(!activity.isDestroyed()){
					activity.finish();
				}
			}
		}
		//释放强引用
		if(!AllChoiceImage.isEmpty()){
			AllChoiceImage.clear();
		}
		if(!activityMap.isEmpty()){
			activityMap.clear();
		}
		
		ShowAllPhotoActivity.listener = null ;
		PickImageActivity.listener = null ;
		GalleryActivity.listener = null ;
		ImageSourceDialogActivity.listener = null ;
		a.finish();
	}
}
