package cn.ning.imagepicker.imagepicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import cn.ning.imagepicker.R;
import cn.ning.imagepicker.imagepicker.util.BitmapCache;
import cn.ning.imagepicker.imagepicker.util.Container;
import cn.ning.imagepicker.imagepicker.util.ImageItem;


/**
 * 显示一个文件夹里面的所有图片时用的适配器
 */
public class AlbumGridViewAdapter extends BaseAdapter {
	final String TAG = getClass().getSimpleName();
	private Context mContext;
	private List<ImageItem> data;
//	private DisplayMetrics dm;
	BitmapCache cache;
	private OnCheckedChangedListener listener ;
	public AlbumGridViewAdapter(Context c, List<ImageItem> dataList, OnCheckedChangedListener listener) {
		mContext = c;
		this.listener = listener ;
		cache = new BitmapCache();
		this.data = dataList;
//		dm = new DisplayMetrics();
//		((Activity) mContext).getWindowManager().getDefaultDisplay()
//				.getMetrics(dm);
	}

	public interface OnCheckedChangedListener{
		public void onCheckedChanged(CheckBox cb, ToggleButton tb, boolean isChecked, ImageItem imageItem);
	}
	public int getCount() {
		if(data==null)
			return 0;
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
							  Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
	
	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;
		public CheckBox checked_cb;
		public TextView textView;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_camera_select_imageview, parent, false);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
			viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
			viewHolder.checked_cb = (CheckBox) convertView.findViewById(R.id.item_imagepicker_checked_cb);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (data != null && data.size() > position)
			path = data.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			viewHolder.imageView.setImageResource(R.drawable.plugin_camera_no_pictures);
		} else {
			final ImageItem item = data.get(position);
			viewHolder.imageView.setTag(item.imagePath);
			cache.displayBmp(viewHolder.imageView, item.thumbnailPath, item.imagePath,
					callback);
		}
		viewHolder.toggleButton.setTag(position);
		viewHolder.checked_cb.setTag(position);
		final CheckBox cb = viewHolder.checked_cb ;
		cb.setClickable(false);
		cb.setEnabled(false);
		final ToggleButton tb = viewHolder.toggleButton;
		tb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToggleButton tb = (ToggleButton)v;
				cb.setChecked(tb.isChecked());
				if(listener!=null){
					listener.onCheckedChanged(cb , tb, tb.isChecked() , data.get(position));
				}
			}
		});


		if (isChecked(data.get(position))) {
			viewHolder.toggleButton.setChecked(true);
			viewHolder.checked_cb.setChecked(true);
		} else {
			viewHolder.toggleButton.setChecked(false);
			viewHolder.checked_cb.setChecked(false);
		}
		return convertView;
	}

	private boolean isChecked(ImageItem ii){
		for(String s : Container.AllChoiceImage){
			if(s.equals(ii.imagePath)){
				return true;
			}
		}
		return false;
	}

	public void setListener(OnCheckedChangedListener listener) {
		this.listener = listener;
	}

}
