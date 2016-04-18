package com.example.xiangshengyuan.asynctask;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.view.NestedScrollingChild;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by xiangshengyuan on 16/4/14.
 */
public class NewsAdapter extends BaseAdapter  {

    private List<NewBean>mList;
    private LayoutInflater mInflater;
    private ImageLoader  mImageLoader;

    public NewsAdapter(Context context,List<NewBean> data){
        mList=data;
        mInflater=LayoutInflater.from(context);
        mImageLoader=new ImageLoader();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.item_layout,null);
            viewHolder.ivIcon=(ImageView)convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent=(TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        String url=mList.get(position).newsIconURL;
        viewHolder.ivIcon.setTag(url);
       // new ImageLoader().showImageByThread(viewHolder.ivIcon,
       //     mList.get(position).newsIconURL);
        mImageLoader.showImageByAsyncTask(viewHolder.ivIcon,url);
        viewHolder.tvTitle.setText(mList.get(position).newsTitle);
        viewHolder.tvContent.setText(mList.get(position).newsContent);

        return convertView;
    }
    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView ivIcon;

    }
}
