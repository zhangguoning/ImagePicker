# ImagePicker 图片选择器
![](https://github.com/zhangguoning/imagePicker/raw/master/imagepicker.gif)
<br/>
一个封装好的图片选择器模块，可以使用多选模式和单选模式两种，单选模式调用系统的图片选择功能，
多选模式则调用自己定义的选择功能，再次选择时可以选择是否记录前面已选择过的图片。
```java
//isMultiple  是否未为多选模式？  是：调用自己的多选选择器， 否：调用系统的图片选择器
        int max = 9 ; //最多可以选择多少张
        ArrayList<String> existImages = new ArrayList<>(); //已经选择过的图片集合
        ImagePicker manager = new ImagePicker(this, isMultiple, max, existImages, new CallBack.ImagePickerListener() {
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
        });
        manager.showImagePickerDialog();
```

        
        
