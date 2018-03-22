package com.scuola.simpleparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scuola.simpleparking.common.WSService;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private EditText mInput;
    private TextView mHello1;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReferences();

        mHello1.setText(getString(R.string.app_name));


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(MainActivity.this, SecondaActivity.class);
                //startActivity(intent);

                WSService ws = WSService.getInstance();

                ws.GetRequest((MainActivity)v.getContext());
                //Log.i(TAG, "Errore");
                //Toast.makeText(v.getContext(), "Ciao Marco", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getReferences(){

        mHello1 = findViewById(R.id.hello1);
        mButton = findViewById(R.id.button);

    }
}
