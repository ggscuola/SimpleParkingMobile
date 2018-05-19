package com.scuola.simpleparking.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.scuola.simpleparking.R;

import java.util.ArrayList;

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



    public static String GetCodicePrenotazione(Context context) throws Exception {

        String codicePrenotazione = null;
        try {

            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);
            codicePrenotazione = editor.getString(context.getResources().getString(R.string.CODICE_PRENOTAZIONE), null);

            return codicePrenotazione;

        } catch (Exception e) {

            throw new Exception(e.getMessage());

        }

    }


    public static void SetInfoPrenotazione(ArrayList<String> codice, Context context) throws Exception{

        try {
            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);

            if(codice != null){

                editor.edit().putString(context.getResources().getString(R.string.CODICE_PRENOTAZIONE), codice.get(0)).apply();
                editor.edit().putString(context.getResources().getString(R.string.POSTO_PRENOTATO), codice.get(1)).apply();
                editor.edit().putString(context.getResources().getString(R.string.PIANO_PRENOTATO), codice.get(2)).apply();
            }else{
                editor.edit().putString(context.getResources().getString(R.string.CODICE_PRENOTAZIONE), null).apply();
                editor.edit().putString(context.getResources().getString(R.string.POSTO_PRENOTATO), null).apply();
                editor.edit().putString(context.getResources().getString(R.string.PIANO_PRENOTATO), null).apply();
            }


            editor.edit().apply();


        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }

    }


    public static String GetPianoPrenotato(Context context) throws Exception {

        String pianoPrenotato = null;
        try {

            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);
            pianoPrenotato = editor.getString(context.getResources().getString(R.string.PIANO_PRENOTATO), null);

            return pianoPrenotato;

        } catch (Exception e) {

            throw new Exception(e.getMessage());

        }

    }


    public static String GetPostoPrenotato(Context context) throws Exception {

        String postoPrenotato = null;
        try {

            SharedPreferences editor = context.getSharedPreferences(context.getResources().getString(R.string.SET), Context.MODE_PRIVATE);
            postoPrenotato = editor.getString(context.getResources().getString(R.string.POSTO_PRENOTATO), null);

            return postoPrenotato;

        } catch (Exception e) {

            throw new Exception(e.getMessage());

        }

    }
}
