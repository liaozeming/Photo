package com.example.lzmthird;



import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

//从主函数传递过来是
//列表显示项 mListView，全部信息，现在要在list_item用ViewHolder来一项项显示
class ViewHolder
{
    public ImageView imgThumb;
    public TextView tv_imgFilename;
    public  TextView tv_imgFilePath;
    public  TextView tv_imgDateModified;

    View itemView;
    public ViewHolder(View itemView)
    {
        if (itemView==null)
        {
            throw new IllegalArgumentException("itemView 不能为空");
        }
        this.itemView=itemView;
        imgThumb=itemView.findViewById(R.id.img_thumb);
        tv_imgFilename=itemView.findViewById(R.id.img_filename);
        tv_imgDateModified=itemView.findViewById(R.id.img_datemodified);
        tv_imgFilePath=itemView.findViewById(R.id.img_filepath);

    }
}

public class ImageListAdapter extends BaseAdapter {
   private  List<ImageInfo> imgList;
    private LayoutInflater layoutInflater;
    private Context context;
    private  int currentPos;
    private  ViewHolder holder=null;
    public ImageListAdapter(Context context, List<ImageInfo> imageInfoList)
    {
        this.context=context;
        this.imgList=imageInfoList;
        //LayoutInflater的作用类似于findViewById()。不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        //在MainActivity的布局文件中显示
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        //返回照片的数量
        return imgList.size();
    }

    @Override
    //返回第i张照片的文件名
    public Object getItem(int i) {
        return imgList.get(i).getFile_name();
    }

    @Override
    public long getItemId(int i) {
//返回照片位置
        return i;
    }
//    移除照片
    public  void remove(int index)
    {
        imgList.remove(index);
    }

    public void refreshDataSet()
    {
        notifyDataSetChanged();
    }

    //找到存放图片和文本信息的控件，然后把控件包装成viewholder对象，再在空间位置显示包装对象
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            //LayoutInflater类的inflate方法适用于所有需要进行布局填充的场景，
            // 是Android中专门进行布局填充的方法，Android中其他需要
            //使用布局填充的地方，都会调用本方法，而不是View类中的inflate方法。该方法不是静态方法，
            // 需要先创建LayoutInflater类的对象才能调用
            //在MainaActivity中填充list_item
            view=layoutInflater.inflate(R.layout.list_item,null);

            //封装view
            holder=new ViewHolder(view);

            //把一个标签与一个控件关联，一个标签可以在层次图中用来标记一个控件，
            //在层次图中并不唯一。标签也可以在控件中存储数据，不需要存储成另外的数据格式。
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
//        在控件设置信息
        holder.imgThumb.setImageBitmap(BitmapFactory.decodeFile(imgList.get(i).getFile_path()));
        holder.tv_imgFilename.setText(imgList.get(i).getFile_name());
        holder.tv_imgFilePath.setText(imgList.get(i).getFile_path());
        holder.tv_imgDateModified.setText(imgList.get(i).getFile_date());
        return view;
    }
}
