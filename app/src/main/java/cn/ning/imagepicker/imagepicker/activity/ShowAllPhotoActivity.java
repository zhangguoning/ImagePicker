package cn.ning.imagepicker.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import cn.ning.imagepicker.R;
import cn.ning.imagepicker.imagepicker.adapter.AlbumGridViewAdapter;
import cn.ning.imagepicker.imagepicker.callback.CallBack;
import cn.ning.imagepicker.imagepicker.util.AlbumHelper;
import cn.ning.imagepicker.imagepicker.util.Container;
import cn.ning.imagepicker.imagepicker.util.ImageItem;


/**
 * 显示一个文件夹里面的所有图片时的界面
 */
public class ShowAllPhotoActivity extends Activity implements AlbumGridViewAdapter.OnCheckedChangedListener,OnClickListener {
	private GridView gridView;
	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private TextView show_photo_sure_tv;
	// 预览按钮
	private TextView show_photo_preview_tv;
	// 返回按钮
	private ImageView show_photo_back_iv;
	// 取消按钮
	private TextView show_photo_cancel_tv;
	// 标题
	private TextView show_photo_title_tv;
	private Intent intent;
	private List<ImageItem> datalist ;
	private String bucketId;
	public static CallBack.ImagePickerListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_show_all_photo);
		findViewById();
		this.intent = getIntent();
		String folderName = intent.getStringExtra("folderName");
		bucketId = intent.getStringExtra("bucketId");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		show_photo_title_tv.setText(folderName);
		show_photo_cancel_tv.setOnClickListener(this);
		show_photo_back_iv.setOnClickListener(this);
		show_photo_preview_tv.setOnClickListener(new PreviewListener());
		show_photo_sure_tv.setOnClickListener(this);
		init();
		Toast.makeText(getApplicationContext(), "已选取："+ Container.AllChoiceImage.size()+"张,还可以选取 "+(Container.MAX- Container.AllChoiceImage.size())+"张", Toast.LENGTH_SHORT).show();
		Container.activityMap.put(this.getClass().getSimpleName(), this);
	}

	private void findViewById(){
		show_photo_back_iv = (ImageView) findViewById(R.id.show_photo_back_iv);
		show_photo_cancel_tv = (TextView) findViewById(R.id.show_photo_cancel_tv);
		show_photo_preview_tv = (TextView) findViewById(R.id.show_photo_preview_tv);
		show_photo_sure_tv = (TextView) findViewById(R.id.show_photo_sure_tv);
		show_photo_title_tv = (TextView) findViewById(R.id.show_photo_title_tv);
	}

	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Container.AllChoiceImage.size() > 0) {
				intent.putExtra("position", "2");
				intent.setClass(ShowAllPhotoActivity.this, GalleryActivity.class);
				startActivity(intent);
				ShowAllPhotoActivity.this.finish();
			}
		}

	}

	private void init() {
		AlbumHelper helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		progressBar = (ProgressBar) findViewById(R.id.showallphoto_progressbar);
		progressBar.setVisibility(View.GONE);
		gridView = (GridView) findViewById(R.id.showallphoto_myGrid);//FIXME :
		datalist = helper.getImageBucketById(false, bucketId).imageList;
		gridImageAdapter = new AlbumGridViewAdapter(this,datalist, this);
		gridView.setAdapter(gridImageAdapter);

	}



	@Override
	protected void onRestart() {
		show_photo_sure_tv.setText("确定("+(Container.AllChoiceImage.size())+")");
//		isShowOkBt();
		super.onRestart();
	}

	@Override
	public void onCheckedChanged(CheckBox cb , ToggleButton tb, boolean isChecked , ImageItem imageItem ) {

		int checkedNum = Container.AllChoiceImage.size();
		if(checkedNum< Container.MAX){
			if(isChecked){
				Container.AllChoiceImage.add(imageItem.getImagePath());
			}else{
				Container.AllChoiceImage.remove(imageItem.getImagePath());
			}
		}else{
			if(isChecked){
				cb.setChecked(false);
				tb.setChecked(false);
				Toast.makeText(getApplicationContext(), "已达到最大数量,不可再选", Toast.LENGTH_LONG).show();
			}else{
				Container.AllChoiceImage.remove(imageItem.getImagePath());
			}
		}
		checkedNum = Container.AllChoiceImage.size();
		show_photo_sure_tv.setText("确定("+checkedNum+")");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.show_photo_back_iv://后退按钮
				this.finish();
				break;

			case R.id.show_photo_cancel_tv://取消按钮
				listener.onCancel();
				Container.finishAll(ShowAllPhotoActivity.this);
				break;
			case R.id.show_photo_sure_tv://确定按钮

				listener.onPicked(Container.AllChoiceImage);
				Container.finishAll(ShowAllPhotoActivity.this);
				break ;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		show_photo_sure_tv.setText("确定("+(Container.AllChoiceImage.size())+")");
		gridImageAdapter.notifyDataSetChanged();
	}
}
