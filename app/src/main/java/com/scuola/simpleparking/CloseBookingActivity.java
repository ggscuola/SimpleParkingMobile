package com.scuola.simpleparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.scuola.simpleparking.common.UserRepository;

public class CloseBookingActivity extends AppCompatActivity {

    private final static String TAG = CloseBookingActivity.class.getSimpleName();

    private TextView mCodicePrenotazione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_booking);


        mCodicePrenotazione = findViewById(R.id.codice_prenotazione);

        String codicePrenotazione = null;

        try{

            codicePrenotazione = UserRepository.GetCodicePrenotazione(this);
            mCodicePrenotazione.setText(codicePrenotazione);

        }catch (Exception e){

            Log.e(TAG, e.getMessage());
        }

    }
}
