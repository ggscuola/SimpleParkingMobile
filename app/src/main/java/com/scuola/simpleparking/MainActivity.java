package com.scuola.simpleparking;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scuola.simpleparking.common.UIUpdater;
import com.scuola.simpleparking.common.UserRepository;
import com.scuola.simpleparking.common.WSService;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private UIUpdater mUIUpdater;

    private FloatingActionButton mForceRefresh;
    public ImageButton mPosto1,mPosto2,mPosto3,mPosto4,mPosto5,mPosto6,mPosto7,mPosto8,mPosto9,mPosto10,mPosto11,mPosto12;
    public TextView mPostiDisponibili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String targa = null;
        String codicePrenotazione = null;

        try{

             targa = UserRepository.GetTarga(this);

             if(targa == null){
                 Intent intent = new Intent(this, LoginActivity.class);
                 startActivity(intent);
                 finish();
             }



        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


        getReferences();

        mUIUpdater = new UIUpdater(new Runnable() {
            @Override
            public void run() {
                WSService ws = WSService.getInstance();
                ws.GetRequest(MainActivity.this);
            }
        });

        mForceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WSService ws = WSService.getInstance();
                ws.GetRequest((MainActivity)v.getContext());

            }
        });



        /* GESTIONE EVENTI ON CLICK DEI SINGOLI POSTI */


        //Posto 1 Piano 1
        mPosto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(1,1);
            }
        });


        //Posto 2 Piano 1
        mPosto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(2,1);
            }
        });


        //Posto 3 Piano 1
        mPosto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(3,1);
            }
        });


        //Posto 4 Piano 1
        mPosto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(4,1);
            }
        });


        //Posto 5 Piano 1
        mPosto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(5,1);
            }
        });


        //Posto 6 Piano 1
        mPosto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                BookNow(6,1);
            }
        });


        //Posto 1 Piano 2
        mPosto7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(1,2);
            }
        });


        //Posto 2 Piano 2
        mPosto8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(2,2);
            }
        });


        //Posto 3 Piano 2
        mPosto9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(3,2);
            }
        });


        //Posto 4 Piano 2
        mPosto10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(4,2);
            }
        });


        //Posto 5 Piano 2
        mPosto11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(5,2);
            }
        });


        //Posto 6 Piano 2
        mPosto12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BookNow(6,2);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.resetTarga) {
            // do something here
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start updates
        mUIUpdater.startUpdates();
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Stop updates
        mUIUpdater.stopUpdates();
    }


    private void BookNow(int posto, int piano){

        WSService ws = WSService.getInstance();
        ws.BookingRequest(this, posto, piano);
    }

    private void getReferences(){

        mForceRefresh = findViewById(R.id.force_refresh);
        mPostiDisponibili = findViewById(R.id.posti_disponibili);
        mPosto1 = findViewById(R.id.posto_1);
        mPosto2 = findViewById(R.id.posto_2);
        mPosto3 = findViewById(R.id.posto_3);
        mPosto4 = findViewById(R.id.posto_4);
        mPosto5 = findViewById(R.id.posto_5);
        mPosto6 = findViewById(R.id.posto_6);
        mPosto7 = findViewById(R.id.posto_7);
        mPosto8 = findViewById(R.id.posto_8);
        mPosto9 = findViewById(R.id.posto_9);
        mPosto10 = findViewById(R.id.posto_10);
        mPosto11 = findViewById(R.id.posto_11);
        mPosto12 = findViewById(R.id.posto_12);

    }


}
