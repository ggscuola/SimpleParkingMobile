package com.scuola.simpleparking.common;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.scuola.simpleparking.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class JsonParse {

    public final static String COLUMN_POSTO = "posto";
    public final static String COLUMN_STATO = "stato";
    public final static String COLUMN_PIANO = "piano";
    public final static String COLUMN_CODICE = "codice_prenotazione";

    public synchronized static ArrayList<Mappa> parseJsonMap(String result) throws JSONException {
        String response = null;
        JSONArray list = null;


        ArrayList<Mappa> mArray = new ArrayList<>();


        try {
            list = new JSONArray(result);


            for (int x = 0; x < list.length(); x++) {
                JSONObject li = list.getJSONObject(x);
                int posto = li.getInt(COLUMN_POSTO);
                int stato = li.getInt(COLUMN_STATO);
                int piano = li.getInt(COLUMN_PIANO);

                Mappa map = new Mappa(posto, stato, piano);
                mArray.add(map);
            }


        } catch (Exception e) {
            Log.e(TAG, "insertJsonDataCodifica:" + e.getMessage());
            response = e.getMessage();


            throw new JSONException(e.getMessage());
        }

        return mArray;
    }


    public synchronized static ArrayList<String> parseJsonCodPrenotazione(String result, AppCompatActivity activity) throws JSONException {

        ArrayList<String> response = new ArrayList<>();
        JSONObject reader = null;


        try {
            reader = new JSONObject(result);


            String codice = reader.getString(COLUMN_CODICE);
            int posto = reader.getInt(COLUMN_POSTO);
            int piano = reader.getInt(COLUMN_PIANO);

            //Posizione 0
            response.add(codice);
            //Posizione 1
            response.add(String.valueOf(posto));
            //Posizione 2
            response.add(String.valueOf(piano));

            //Salvo le info di prenotazione
            UserRepository.SetInfoPrenotazione(response, activity);


        } catch (Exception e) {

            throw new JSONException(e.getMessage());
        }



        return response;
    }



}



