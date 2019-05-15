package com.example.lzmthird;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ViewActivity extends Activity {
    private ArrayList<ImageInfo>image_List;
    private  int pos;
    private ImageView imageView;
    private GestureDetector gestureDetector;
    private  String currentImgSrc;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,0,R.string.menu_wall);
        menu.add(0,1,1,R.string.menu_detail);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Bitmap bm = BitmapFactory.decodeFile(image_List.get(pos).getFile_path());
                try {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(ViewActivity.this);
                    wallpaperManager.setBitmap(bm);
                    Toast.makeText(ViewActivity.this, "壁纸设置成功！", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewActivity.this, "壁纸设置失败", Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                StringBuilder msgBuilder = new StringBuilder();
                msgBuilder.append("文件名：" + image_List.get(pos).getFile_name() + "\n");
                msgBuilder.append("修改日期：" + image_List.get(pos).getFile_date() + "\n");
                msgBuilder.append("文件路径：" + image_List.get(pos).getFile_path() + "\n");
                DetailDialog detailDialog = new DetailDialog(this, msgBuilder.toString());
                detailDialog.show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //显示位置
        imageView=(ImageView)findViewById(R.id.img_view);
        //获得传递过来的的图片位置和图片列表
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        pos=bundle.getInt("pos");
        image_List=(ArrayList<ImageInfo>)bundle.getSerializable("image_list");
        //当前图片
         currentImgSrc=image_List.get(pos).getFile_path();
        imageView.setImageBitmap(BitmapFactory.decodeFile(currentImgSrc));
        //注册上下文菜单
        registerForContextMenu(imageView);



        //定义手势管理器
        gestureDetector=new GestureDetector(ViewActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                if(motionEvent.getX()-motionEvent1.getX()>100)
                {
                    if(pos>0)
                    {
                        pos--;
                    }
                    if(pos<=0)
                    {
                        pos=0;
                        Toast.makeText(ViewActivity.this,"已经是第一张",Toast.LENGTH_LONG).show();

                    }
                     currentImgSrc=image_List.get(pos).getFile_path();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(currentImgSrc));
                    Log.e("Fling","TO LEFT");
                }
                //从左往右
                else if(motionEvent.getX()-motionEvent1.getX()<-100)
                {
                    if(pos<image_List.size())
                    {
                        pos++;
                    }
                    if(pos>image_List.size())
                    {
                        pos=image_List.size()-1;
                        Toast.makeText(ViewActivity.this,"已经是最后一张了",Toast.LENGTH_LONG).show();

                    }
                     currentImgSrc=image_List.get(pos).getFile_path();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(currentImgSrc));
                    Log.e("Fling","TO Right");
                }
                return true;
            }
        });

    }
}
