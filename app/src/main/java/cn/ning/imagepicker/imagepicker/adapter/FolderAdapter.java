package cn.ning.imagepicker.imagepicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ning.imagepicker.R;
import cn.ning.imagepicker.imagepicker.util.BitmapCache;
import cn.ning.imagepicker.imagepicker.util.ImageBucket;
import cn.ning.imagepicker.imagepicker.util.ImageItem;


/**
 * 是显示所有包含图片的文件夹的适配器
 */
public class FolderAdapter extends BaseAdapter {

	private Context mContext;
	private Intent mIntent;
	private DisplayMetrics dm;
	private List<ImageBucket> data ;
	BitmapCache cache;
	final String TAG = getClass().getSimpleName();
	
	public interface OnAlbumSeclected{
		public void onAlbumSeclected(View v, ImageBucket imageBucket);
	}
	private OnAlbumSeclected onAlbumSeclected ;
	
	public FolderAdapter(Context c , List<ImageBucket> data , OnAlbumSeclected onAlbumSeclected ) {
		cache = new BitmapCache();
		this.onAlbumSeclected  = onAlbumSeclected ;
		init(c);
		this.data = data ;
	}

	// 初始化
	public void init(Context c ) {
		mContext = c;
		mIntent = ((Activity) mContext).getIntent();
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	

	@Override
	public int getCount() {
		if(data==null)
			return 0 ;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
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

	private class ViewHolder {
		//
		public LinearLayout gotoDetails;
		// 封面
		public ImageView imageView;
//		public ImageView choose_back;
		// 文件夹名称
		public TextView folderName;
		// 文件夹里面的图片数量
		public TextView fileNum;
	}
	ViewHolder holder = null;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image_picker_dir, null);
			holder = new ViewHolder();
			holder.gotoDetails =  (LinearLayout) convertView
					.findViewById(R.id.item_image_ll);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_image_ico_iv);
			holder.folderName = (TextView) convertView.findViewById(R.id.item_image_title_tv);
			holder.fileNum = (TextView) convertView.findViewById(R.id.item_image_count_tv);
			holder.imageView.setAdjustViewBounds(true);
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String path;
		if (data.get(position).imageList != null) {
			
			//封面图片路径
			path = data.get(position).imageList.get(0).imagePath;
			holder.folderName.setText(data.get(position).bucketName);
			
			holder.fileNum.setText("(" +data.get(position).count+")");
			
		} else
			path = "android_hybrid_camera_default";
		if (path.contains("android_hybrid_camera_default"))
			holder.imageView.setImageResource(R.drawable.plugin_camera_no_pictures);
		else {
			final ImageItem item = data.get(position).imageList.get(0);
			holder.imageView.setTag(item.imagePath);
			cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath,
					callback);
		}
		// 添加监听
		holder.gotoDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onAlbumSeclected!=null){
					onAlbumSeclected.onAlbumSeclected(v, data.get(position));
				}
				
			}
		});
		
		return convertView;
	}

	// 为每一个文件夹构建的监听器

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	public List<ImageBucket> getData() {
		return data;
	}

	public void setData(List<ImageBucket> data) {
		this.data = data;
	}

	public OnAlbumSeclected getOnAlbumSeclected() {
		return onAlbumSeclected;
	}

	public void setOnAlbumSeclected(OnAlbumSeclected onAlbumSeclected) {
		this.onAlbumSeclected = onAlbumSeclected;
	}

}
