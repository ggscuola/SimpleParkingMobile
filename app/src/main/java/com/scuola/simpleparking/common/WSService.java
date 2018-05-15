package com.scuola.simpleparking.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.scuola.simpleparking.MainActivity;
import com.scuola.simpleparking.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MarcoS on 22/03/2018.
 */

public class WSService {

    private String TAG = WSService.class.getSimpleName();
    private int result = Activity.RESULT_CANCELED;
    public final String URL_REQUEST = "http://172.16.13.119/service.php?mode=0";
    private ProgressDialog mProgress;


    private static WSService mIstance;

    private MainActivity mActivity = null;

    public static WSService getInstance() {
        if (mIstance == null) {
            mIstance = new WSService();
        }
        return mIstance;
    }

    public void GetRequest(AppCompatActivity activity) {
        mActivity = (MainActivity) activity;


        GetDataTask task = new GetDataTask();

        mProgress = ProgressDialog.show(mActivity, "Sincronizzazione",
                "Attendere...", true);
        task.execute(URL_REQUEST);
    }


    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            OutputStream outputStream;
            BufferedWriter writer;
            String result = null;


            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.addRequestProperty("Accept", "application/json");
                conn.setReadTimeout(2000);
                conn.setConnectTimeout(2000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(false);

                String line;

                int response = conn.getResponseCode();


                if (response == HttpURLConnection.HTTP_OK) {

                    StringBuilder res = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((line = bufferedReader.readLine()) != null) {
                        res.append(line);
                    }
                    result = res.toString();
                } else {
                    final String message = String.valueOf(conn.getResponseCode());
                    Log.e(TAG, message);

                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

            }

            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
            try {

                mProgress.dismiss();

                if (result != null) {
                    final ArrayList<Mappa> mappa = JsonParse.parseJsonMap(result);

                    new Runnable() {
                        @Override
                        public void run() {


                            int postiDipsonibili = 12;

                            for (Mappa map : mappa) {

                                if (map.Piano == 2) {
                                    map.Posto = map.Posto + 6;
                                }

                                if (map.Stato == 1) {
                                    postiDipsonibili--;
                                }

                                switch (map.Posto) {
                                    case 1:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto1.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto1.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 2:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto2.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto2.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 3:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto3.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto3.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 4:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto4.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto4.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 5:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto5.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto5.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 6:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto6.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto6.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 7:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto7.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto7.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 8:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto8.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto8.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 9:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto9.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto9.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 10:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto10.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto10.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 11:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto11.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto11.setImageResource(R.mipmap.libero);

                                        }
                                        break;
                                    case 12:
                                        if (map.Stato == 1) {
                                            mActivity.mPosto12.setImageResource(R.mipmap.occupato);
                                        } else {
                                            mActivity.mPosto12.setImageResource(R.mipmap.libero);

                                        }
                                        break;

                                }


                            }

                            mActivity.mPostiDisponibili.setText(String.valueOf(postiDipsonibili));
                            Toast.makeText(mActivity, "Sincronizzato", Toast.LENGTH_SHORT).show();
                        }
                    }.run();

                }


            } catch (Exception e)

            {

                Log.e(TAG, e.getMessage());

            }
        }


    }


}
