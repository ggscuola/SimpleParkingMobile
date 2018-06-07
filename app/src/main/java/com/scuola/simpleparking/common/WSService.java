package com.scuola.simpleparking.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.scuola.simpleparking.CloseBookingActivity;
import com.scuola.simpleparking.LoginActivity;
import com.scuola.simpleparking.MainActivity;
import com.scuola.simpleparking.R;
import com.scuola.simpleparking.SplashActivity;

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

    private ProgressDialogJC mProgressJC;


    private static WSService mIstance;

    private MainActivity mActivity = null;
    private SplashActivity mActivitySplash = null;
    private CloseBookingActivity mActivityClose = null;

    public static WSService getInstance() {
        if (mIstance == null) {
            mIstance = new WSService();
        }
        return mIstance;
    }

    public void GetRequest(AppCompatActivity activity) {
        mActivity = (MainActivity) activity;


        GetDataTask task = new GetDataTask();

        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            String ip = UserRepository.GetIp(mActivity);
            uriBuilder.authority(ip);
            uriBuilder.scheme("http");
            uriBuilder.path("service.php");
            uriBuilder.appendQueryParameter("mode", "0");

            mProgressJC = new ProgressDialogJC(mActivity);
            mProgressJC.setMessage("Sincronizzazione in corso...");
            mProgressJC.setSpinnerType(2);
            mProgressJC.show();

            task.execute(uriBuilder.toString());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }


    public void BookingRequest(AppCompatActivity activity, int posto, int piano) {
        mActivity = (MainActivity) activity;

        BookingTask task = new BookingTask();

        mProgressJC = new ProgressDialogJC(mActivity);
        mProgressJC.setMessage("Prenotazione in corso...");
        mProgressJC.setSpinnerType(2);
        mProgressJC.show();
        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            String ip = UserRepository.GetIp(mActivity);
            uriBuilder.authority(ip);
            uriBuilder.scheme("http");
            uriBuilder.path("service.php");
            uriBuilder.appendQueryParameter("mode", "2");
            String targa = null;


            targa = UserRepository.GetTarga(mActivity);
            uriBuilder.appendQueryParameter("targa", targa);
            uriBuilder.appendQueryParameter("piano", String.valueOf(piano));
            uriBuilder.appendQueryParameter("posto", String.valueOf(posto));

            String url = uriBuilder.toString();

            task.execute(url);

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }

    }


    public void CloseBookingRequest(AppCompatActivity activity) {
        mActivityClose = (CloseBookingActivity) activity;

        CloseBookingTask task = new CloseBookingTask();

        mProgressJC = new ProgressDialogJC(mActivityClose);
        mProgressJC.setMessage("Chiusura in corso...");
        mProgressJC.setSpinnerType(2);
        mProgressJC.show();
        try {
            Uri.Builder uriBuilder = new Uri.Builder();
            String ip = UserRepository.GetIp(mActivityClose);
            uriBuilder.authority(ip);

            uriBuilder.scheme("http");
            uriBuilder.path("service.php");
            uriBuilder.appendQueryParameter("mode", "3");
            String targa = null;
            String codice = null;


            targa = UserRepository.GetTarga(mActivityClose);
            uriBuilder.appendQueryParameter("targa", targa);

            codice = UserRepository.GetCodicePrenotazione(mActivityClose);
            uriBuilder.appendQueryParameter("codice", codice);

            String url = uriBuilder.toString();

            task.execute(url);

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }

    }


    public void CheckBooking(AppCompatActivity activity, String targa) {

        mActivitySplash = (SplashActivity) activity;

        CheckBookingTask task = new CheckBookingTask();

        Uri.Builder uriBuilder = new Uri.Builder();
        //uriBuilder.authority("172.16.13.119");

        try {
            String ip = UserRepository.GetIp(mActivitySplash);
            uriBuilder.authority(ip);
            uriBuilder.scheme("http");
            uriBuilder.path("service.php");
            uriBuilder.appendQueryParameter("mode", "1");


            uriBuilder.appendQueryParameter("targa", targa);

            String url = uriBuilder.toString();

            task.execute(url);

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());
        }

    }


    @SuppressLint("StaticFieldLeak")
    private class CheckBookingTask extends AsyncTask<String, Integer, String> {


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


                if (result != null) {

                    try {
                        //Parserizzo codice di prenotazione
                        ArrayList<String> codicePrenotazione = JsonParse.parseJsonCodPrenotazione(result, mActivitySplash);

                        Intent intent = null;
                        if (codicePrenotazione.size() > 0) {

                            if (codicePrenotazione.get(0) != null) {

                                intent = new Intent(mActivitySplash, CloseBookingActivity.class);

                            }
                        } else {

                            intent = new Intent(mActivitySplash, MainActivity.class);
                        }

                        mActivitySplash.startActivity(intent);
                        mActivitySplash.finish();

                    } catch (Exception e) {

                        Log.e(TAG, e.getMessage());
                    }


                } else {

                    Toast.makeText(mActivitySplash, "Errore: verificare la connessione con la Raspberry", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mActivitySplash, LoginActivity.class);
                    mActivitySplash.startActivity(intent);
                    mActivitySplash.finish();

                }
            } catch (Exception e)

            {

                Log.e(TAG, e.getMessage());

            }
        }


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

                            mProgressJC.dismissWithSuccess("Sincronizzato!");
                            mActivity.mPostiDisponibili.setText(String.valueOf(postiDipsonibili));
                        }
                    }.run();

                } else {

                    mProgressJC.dismissWithFailure("0 resulti!");
                    Toast.makeText(mActivity, "Errore: verificare la connessione con la Raspberry", Toast.LENGTH_LONG).show();

                }


            } catch (Exception e)

            {
                mProgressJC.dismissWithFailure("Errore sincronizzazione!");

                Log.e(TAG, e.getMessage());

            }
        }


    }


    @SuppressLint("StaticFieldLeak")
    private class BookingTask extends AsyncTask<String, Integer, String> {


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


                if (result != null) {

                    try {
                        //Parserizzo codice di prenotazione
                        ArrayList<String> codicePrenotazione = JsonParse.parseJsonCodPrenotazione(result, mActivity);

                        if (codicePrenotazione.size() > 0) {
                            mProgressJC.dismissWithSuccess("Prenotato!");

                            if (codicePrenotazione.get(0) != null) {

                                Intent intent = new Intent(mActivity, CloseBookingActivity.class);
                                mActivity.startActivity(intent);
                                mActivity.finish();
                            }
                        } else {

                            mProgressJC.dismissWithFailure("Posto già prenotato!");

                        }


                    } catch (Exception e) {

                        mProgressJC.dismissWithFailure("Errore!");
                        Log.e(TAG, e.getMessage());
                    }


                } else {

                    mProgressJC.dismissWithFailure("0 resulti!");
                    Toast.makeText(mActivity, "Errore: verificare la connessione con la Raspberry", Toast.LENGTH_LONG).show();

                }


            } catch (Exception e)

            {
                mProgressJC.dismissWithFailure("Errore sincronizzazione!");

                Log.e(TAG, e.getMessage());

            }
        }


    }


    @SuppressLint("StaticFieldLeak")
    private class CloseBookingTask extends AsyncTask<String, Integer, String> {


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

                mProgressJC.dismissWithSuccess("Pronotazione chiusa!");
                UserRepository.SetInfoPrenotazione(null, mActivityClose);

                new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(mActivityClose, MainActivity.class);
                        mActivityClose.startActivity(intent);
                        mActivityClose.finish();
                    }
                }.run();


            } catch (Exception e) {

                mProgressJC.dismissWithFailure("Errore sincronizzazione!");

                Log.e(TAG, e.getMessage());
            }


        }


    }


}
