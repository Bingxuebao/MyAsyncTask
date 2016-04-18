package com.example.xiangshengyuan.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private static String URL="http://www.imooc.com/api/teacher?type=4&num=30";//网址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView=(ListView)findViewById(R.id.lv_main);
        new NewsAsyncTask().execute(URL);

    }

    class NewsAsyncTask extends AsyncTask<String,Void,List<NewBean>>{

        @Override
        protected List<NewBean> doInBackground(String ...params){
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewBean> newBeen) {
            super.onPostExecute(newBeen);
            NewsAdapter adapter=new NewsAdapter(MainActivity.this,newBeen);
            mListView.setAdapter(adapter);
        }
    }
    /**
     * 将url对应的json格式数据转化为我们所封装的Newbean对象
     * @param url
     * @return
     */
    private List<NewBean>getJsonData(String url){

        List<NewBean> newsBeanList=new ArrayList<>();

        try {
            String jsonString=readStream(new URL(url).openStream());
            JSONObject jsonObject;
            NewBean newBean;

            try {
                jsonObject=new JSONObject(jsonString);
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++){
                    jsonObject=jsonArray.getJSONObject(i);
                    newBean=new NewBean();
                   //分别获取NewBean对象各项对应数据，每循环一次新建一个对象同时添加到List中
                    newBean.newsIconURL=jsonObject.getString("picSmall");
                    newBean.newsTitle=jsonObject.getString("name");
                    newBean.newsContent=jsonObject.getString("description");
                    newsBeanList.add(newBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("xys",jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsBeanList;
    }

    /**
     * 通过流解析网页返回的数据
     * @param is
     * @return result
     */
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String line="";
            isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            while ((line=br.readLine())!=null){
                result+=line;
               // result=result+line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException f){
            f.printStackTrace();
        }
        return result;
    }


}
