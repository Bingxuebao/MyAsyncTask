package com.example.xiangshengyuan.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.PortUnreachableException;
import java.net.URL;

/**
 * Created by xiangshengyuan on 16/4/14.
 */
public class ImageLoader {

    private ImageView mImageView;

    private String murl;
    //创建cache缓存内存
    private LruCache<String,Bitmap>mCaches;

    public ImageLoader(){
        //获取当前可用内存大小
        int maxMemory=(int)Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/4;//设定缓存内存为可用内存的1/4
       //初始化缓存内存
        mCaches=new LruCache<String,Bitmap>(cacheSize){

            @Override
            protected int sizeOf(String key,Bitmap value){
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }
    //存入两个参数，key、value
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCaches.put(url,bitmap);
        }
    }

    //从缓存中获取数据，get方法
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ((mImageView.getTag().equals(murl))){
                 mImageView.setImageBitmap((Bitmap)msg.obj);
            }
            handleMessage(msg);
        }
    };

    //通过Thread方法加载图片
    public void showImageByThread(ImageView imageView, final String url){
        mImageView=imageView;
        murl=url;
        new Thread(){

            @Override
            public void run(){
                super.run();
                Bitmap bitmap=getBitmapFromURL(url);
               //可以使用现有的message，提高message使用效率
                Message message=Message.obtain();
                message.obj=bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            is=new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //通过AsyncTask加载图片
    public void showImageByAsyncTask(ImageView imageView,String url){

        Bitmap bitmap=getBitmapFromCache(url);
        if(bitmap==null){
            new NewsAsyncTask(imageView,url).execute(url);//通过AsyncTask下载图片
        }else
            imageView.setImageBitmap(bitmap);//直接在内存中获取图片
    }


    private class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(ImageView imageView,String url){
            mUrl=url;
            mImageView=imageView;
        }
        @Override
        protected Bitmap doInBackground(String...params){
            String url=params[0];

            Bitmap bitmap=getBitmapFromURL(params[0]);//从网络获取图片

            if (bitmap!=null){
                addBitmapToCache(url,bitmap);//将不在缓存中的图片加入缓存
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl)){
                mImageView.setImageBitmap(bitmap);
            }
            mImageView.setImageBitmap(bitmap);
        }
    }
}
