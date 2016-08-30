package cn.ning.imagepicker.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ning.imagepicker.R;
import cn.ning.imagepicker.imagepicker.adapter.FolderAdapter;
import cn.ning.imagepicker.imagepicker.callback.CallBack;
import cn.ning.imagepicker.imagepicker.util.AlbumHelper;
import cn.ning.imagepicker.imagepicker.util.Container;
import cn.ning.imagepicker.imagepicker.util.ImageBucket;


/**
 * 显示所有相册列表
 * @author LIANDONG
 *
 */
public class PickImageActivity extends Activity implements OnClickListener,
		FolderAdapter.OnAlbumSeclected {
	//顶部菜单
	private TextView pickimage_title_tv ;
	private TextView pickimage_cacel_tv ;

	private ListView all_album_lv ;//所有相册列表
	private FolderAdapter adapter ;
	public static Bitmap bimap ;
	public static CallBack.ImagePickerListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickimage);
		findViewById();
		initData();
		initView();
		Container.activityMap.put(this.getClass().getSimpleName(), this);
	}

	private void findViewById(){
		all_album_lv = (ListView) this.findViewById(R.id.all_album_lv);
		pickimage_title_tv = (TextView) this.findViewById(R.id.pickimage_title_tv);
		pickimage_cacel_tv = (TextView) this.findViewById(R.id.pickimage_cacel_tv);
	}

	private void initView(){
		pickimage_cacel_tv.setOnClickListener(this);
	}

	private void initData(){
		AlbumHelper helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		List<ImageBucket> contentList = helper.getImagesBucketList(false);
		adapter = new FolderAdapter(this,contentList , this );
		all_album_lv.setAdapter(adapter);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.pickimage_title_tv:
				pickimage_title_tv.setText("照片");
				break;

			case R.id.pickimage_cacel_tv:
				listener.onCancel();
				Container.finishAll(PickImageActivity.this);
				break;
		}
	}


	@Override
	public void onAlbumSeclected(View v, ImageBucket imageBucket) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("folderName", imageBucket.bucketName);
		intent.putExtra("bucketId", imageBucket.bucketId);
		intent.setClass(this, ShowAllPhotoActivity.class);
		this.startActivity(intent);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
