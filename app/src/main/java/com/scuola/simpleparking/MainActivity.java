package com.scuola.simpleparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scuola.simpleparking.common.WSService;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private ImageView mLogo;
    public ImageButton mPosto1,mPosto2,mPosto3,mPosto4,mPosto5,mPosto6,mPosto7,mPosto8,mPosto9,mPosto10,mPosto11,mPosto12;
    public TextView mPostiDisponibili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReferences();

        WSService ws = WSService.getInstance();
        ws.GetRequest(this);

        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WSService ws = WSService.getInstance();
                ws.GetRequest((MainActivity)v.getContext());

            }
        });
    }

    private void getReferences(){

        mLogo = findViewById(R.id.image_home);
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
