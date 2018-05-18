package com.scuola.simpleparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.scuola.simpleparking.common.UserRepository;

public class CloseBookingActivity extends AppCompatActivity {

    private final static String TAG = CloseBookingActivity.class.getSimpleName();

    private TextView mCodicePrenotazione;
    private TextView mPostoPrenotato;
    private TextView mPianoPrenotato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_booking);


        mCodicePrenotazione = findViewById(R.id.codice_prenotazione);
        mPostoPrenotato = findViewById(R.id.posto_prenotato);
        mPianoPrenotato = findViewById(R.id.piano_prenotato);

        String codicePrenotazione = null;
        String postoPrenotazione = null;
        String pianoPrenotazione = null;

        try{

            codicePrenotazione = UserRepository.GetCodicePrenotazione(this);
            mCodicePrenotazione.setText(codicePrenotazione);

            postoPrenotazione = UserRepository.GetPostoPrenotato(this);
            mPostoPrenotato.setText(postoPrenotazione);

            pianoPrenotazione = UserRepository.GetPianoPrenotato(this);
            mPianoPrenotato.setText(pianoPrenotazione);

        }catch (Exception e){

            Log.e(TAG, e.getMessage());
        }

    }
}
