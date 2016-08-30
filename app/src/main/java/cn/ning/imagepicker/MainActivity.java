package cn.ning.imagepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import java.util.ArrayList;

import cn.ning.imagepicker.imagepicker.ImagePicker;
import cn.ning.imagepicker.imagepicker.callback.CallBack;

public class MainActivity extends AppCompatActivity implements CallBack.ImagePickerListener{

    private Button button ;
    private CheckBox checkbox ;
    private GridView gridView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) this.findViewById(R.id.button);
        checkbox = (CheckBox) this.findViewById(R.id.checkbox);
        gridView = (GridView) this.findViewById(R.id.gridView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isMultiple = checkbox.isChecked() ;
                addPic(isMultiple);
            }
        });
    }

    private void addPic(boolean isMultiple){//是否未多选模式？  是：调用自己的多选选择器， 否：调用系统的图片选择器
        ImagePicker manager = new ImagePicker(this,isMultiple, 9,null,this);
        manager.showImagePickerDialog();
    }

    @Override
    public void onPicked(ArrayList<String> allChoice) {
        //这个回调方法中选择完成后会返回所有选择的图片的路径
        //your code.....
    }

    @Override
    public void onCancel() {
        //取消选择
        //your code.....
    }
}
