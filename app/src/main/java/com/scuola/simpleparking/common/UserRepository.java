package com.scuola.simpleparking.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.scuola.simpleparking.R;

public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();


    public static void SetTarga(String targa, Context context) throws Exception{

        try {
            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);

            editor.edit().putString(context.getResources().getString(R.string.TARGA), targa).apply();

            editor.edit().apply();


        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

    }




    public static String GetTarga(Context context) throws Exception {
        //Recupera le impostazioni
        String targa = null;
        try {

            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);
            targa = editor.getString(context.getResources().getString(R.string.TARGA), null);

            return targa;

        } catch (Exception e) {

             throw new Exception(e.getMessage());

        }

    }

}
