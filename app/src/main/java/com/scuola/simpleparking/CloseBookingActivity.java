package com.scuola.simpleparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scuola.simpleparking.common.UserRepository;
import com.scuola.simpleparking.common.WSService;

public class CloseBookingActivity extends AppCompatActivity {

    private final static String TAG = CloseBookingActivity.class.getSimpleName();

    private TextView mCodicePrenotazione;
    private TextView mPostoPrenotato;
    private TextView mPianoPrenotato;
    private Button mCloseBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_booking);


        mCodicePrenotazione = findViewById(R.id.codice_prenotazione);
        mPostoPrenotato = findViewById(R.id.posto_prenotato);
        mPianoPrenotato = findViewById(R.id.piano_prenotato);
        mCloseBooking = findViewById(R.id.chiudi_prenotazione);

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


            mCloseBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WSService ws = WSService.getInstance();
                    ws.CloseBookingRequest(CloseBookingActivity.this);
                }
            });

        }catch (Exception e){

            Log.e(TAG, e.getMessage());
        }

    }
}
