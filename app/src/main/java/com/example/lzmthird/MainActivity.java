package com.example.lzmthird;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private List<ImageInfo> imgList=new ArrayList<ImageInfo>();

    private ImageListAdapter imageListAdapter;
    private ListView mListView;
    private String ImageName;
    private int currentSel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //加载图片列表
        loadImageList();
        //图片列表适配器,返回  view=layoutInflater.inflate(R.layout.list_item,null);
        imageListAdapter=new ImageListAdapter(MainActivity.this,imgList);
        //在activity_main.xml文件中对id为image_list的视图添加适配器
        //1.先读图片
        //2.注册适配器
        //3.将适配器放入列表框
        mListView=findViewById(R.id.image_list);
        //imageListAdapter就是返回的view,也就是list_item中显示的项
        mListView.setAdapter(imageListAdapter);

        //视图注册上下文菜单，相当于PC端右键会弹出文本
        registerForContextMenu(mListView);

        //列表项长按监听事件
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //根据适配器位置查找文件名
                ImageName = (String) imageListAdapter.getItem(i);
                //返沪当前位置
                currentSel=i;
                //长按后显示文本
                mListView.showContextMenu();
                return true;
            }
        });

        //监听点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentSel=i;
                String imgPath=imgList.get(i).getFile_path();
                if(imgPath.length()>0)
                {
                    //点击图片跳转到ViewActivity
                    Intent intent=new Intent();
                    Bundle bl=new Bundle();
                    //将照片的位置和字节流序列化后的照片的key-value值传递给ViewActivity
                    bl.putInt("pos",i);
                    bl.putSerializable("image_list", (Serializable) imgList);
                    intent.putExtras(bl);
                    intent.setClass(MainActivity.this,ViewActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    //重载：改写长按弹出的菜单内容
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,0,R.string.menu_wall);
        menu.add(0,1,1,R.string.menu_detail);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                Bitmap bm= BitmapFactory.decodeFile(imgList.get(currentSel).getFile_path());
                try{
                    WallpaperManager wallpaperManager=WallpaperManager.getInstance(MainActivity.this);
                    wallpaperManager.setBitmap(bm);
                    Toast.makeText(MainActivity.this,"壁纸设置成功！",Toast.LENGTH_LONG).show();
                }

                catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"壁纸设置失败",Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                StringBuilder msgBuilder=new StringBuilder();
                msgBuilder.append("文件名："+imgList.get(currentSel).getFile_name()+"\n");
                msgBuilder.append("修改日期："+imgList.get(currentSel).getFile_date()+"\n");
                msgBuilder.append("文件路径："+imgList.get(currentSel).getFile_path()+"\n");
                DetailDialog detailDialog=new DetailDialog(this,msgBuilder.toString());
                detailDialog.show();
                break;
                default:
                    break;

        }
        return super.onContextItemSelected(item);
    }

    //对菜单选项关于和退出事件监听
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue=super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return retValue;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item_about)
        {
            StringBuilder msgBuilder=new StringBuilder();
            msgBuilder.append("ImageViewer V1.0.0\n");
            msgBuilder.append("作者：8000116240廖泽铭\n");
            msgBuilder.append("南昌大学软件学院168班\n");
            DetailDialog detailDialog=new DetailDialog(this,msgBuilder.toString());
            detailDialog.show();
        }
        if(item.getItemId()==R.id.item_exit)
        {
            String title="提示";
            new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.str_warning).setMessage("确定退出吗？")
                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImageList() {
        //取得内存卡的绝对路径
        String internalSdcard= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        //取得文件
         File directory =new File(internalSdcard);
        File[] files=directory.listFiles();
        //依次读出文件
        if(files.length>0)
        {
            for(int i=0;i<files.length;i++)
            {
                if(files[i].exists())
                {
                    //得到文件的后缀名
                    Uri selectedUri=Uri.fromFile(files[i]);
                    String fileExtension= MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
                    String mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    if(mimeType=="image/jpeg"||mimeType=="image/png"||mimeType=="image/bmp"||mimeType=="image/jpg")
                    {
                        //实例化ImageInfo类
                        ImageInfo info=new ImageInfo();
                        info.setFile_name(files[i].getName());
                        info.setFile_path(files[i].getPath());
                        Date lastModified=new Date(files[i].lastModified());
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
                        info.setFile_date(format.format(lastModified));
                        imgList.add(info);


                    }
                }
            }
        }
    }


}
