package cn.ning.imagepicker.imagepicker.util;

import android.view.Gravity;
import android.view.View;

/**
 * PopupWindow的出现位置
 * @author LIANDONG
 *
 */
public class PopupWindowLocaltion {
	public View parent = null;
	/**
	 *缺省值：gravity = Gravity.BOTTOM
	 */
	public  int gravity = Gravity.BOTTOM;
	/**
	 *缺省值：offsetX = 0 ;
	 */
	public  int offsetX = 0;
	/**
	 *缺省值：offsetY = 0 ;
	 */
	public  int offsetY = 0;
	/**
	 *缺省值：height = 400 ;
	 *
	 **/
	public int height = 400;
}
