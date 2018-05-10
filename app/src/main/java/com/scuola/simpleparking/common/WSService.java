package com.scuola.simpleparking.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scuola.simpleparking.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MarcoS on 22/03/2018.
 */

public class WSService {

    private String TAG = WSService.class.getSimpleName();
    private int result = Activity.RESULT_CANCELED;
    public final String URL_REQUEST =  "http://172.16.13.119/service.php?mode=0";



    private static WSService mIstance;

    private MainActivity mActivity = null;

    public static WSService getInstance()
    {
        if(mIstance == null)
        {
            mIstance = new WSService();
        }
        return  mIstance;
    }

    public void GetRequest(AppCompatActivity activity)
    {
        mActivity = (MainActivity) activity;


        GetDataTask task = new GetDataTask();

        String param = URL_REQUEST;
        task.execute(param);
    }


    private class GetDataTask extends AsyncTask<String,Integer,String>
    {


        @Override
        protected String doInBackground(String... strings) {
            OutputStream outputStream;
            BufferedWriter writer;
            String result=null;


            try{
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("Accept", "application/json");
                conn.setReadTimeout(2000);
                conn.setConnectTimeout(2000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(false);
                // outputStream = conn.getOutputStream();
                //  writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //   writer.flush();
                //   writer.close();
                // outputStream.close();
                String line;

                int vr= conn.getResponseCode();


                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    String res = "";
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null) {
                        res += line;
                    }
                    result = res;
                } else {
                    final String message = String.valueOf(conn.getResponseCode());
                    Log.e(TAG, message);

                    new Runnable() {
                        @Override
                        public void run() {

                        }
                    }.run();
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

            }

            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            try{
                if(result != null)
                {
                    JsonParse.parseJsonMap(result);

                    new Runnable() {
                        @Override
                        public void run() {
                        }
                    }.run();

                }



            }catch (Exception e)

            {

                Log.e(TAG, e.getMessage());

            }
        }


    }


}
