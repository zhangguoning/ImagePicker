package cn.ning.imagepicker.imagepicker.callback;

import java.util.ArrayList;

/**
 * Created by ning on 2016/7/11.
 * Summary :
 */
public class CallBack {

    /**
     * 提供给外部使用，点击完成选择或取消选择时调用
     */
    public interface ImagePickerListener{
        /**
         *
         * @param allChoice 返回已经选取的所有图片，该对象可能会包含0个对象，但自身不会为空
         */
        void onPicked(ArrayList<String> allChoice);
        void onCancel();
    }

}
