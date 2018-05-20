package com.scuola.simpleparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scuola.simpleparking.R;
import com.scuola.simpleparking.common.UserRepository;
import com.scuola.simpleparking.common.WSService;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity{



    private static final String TAG_LOG = SplashActivity.class.getName();
    private static final long MIN_WAIT_INTERVAL = 1500L;
    private static final long MAX_WAIT_INTERVAL = 3000L;
    private static final int GO_AHEAD_WHAT = 1;
    private static final String IS_DONE_KEY = "com.jassoncampana.crm.key.IS_DONE_KEY";
    private static final String START_TIME_KEY = "com.jassoncampana.crm.key.START_TIME_KEY";

    private long mStartTime = -1L;

    //stato passaggio all'attivita seguente
    private boolean mIsDone;

    private UiHandler mHandler;






    //creo una classe statica questo mi permette di non avere alcun riferimento logico con la classe esterna (Handler)
    private static class UiHandler extends Handler {

        // rendo il mio riferimento debole (weak)
        private WeakReference<SplashActivity> mActivityRef;


        //costruttore della mia classe UiHandler
        public UiHandler(final SplashActivity srcActivity){

            this.mActivityRef = new WeakReference<SplashActivity>(srcActivity);
        }


        //oggetto in grado di elaborare dei comandi a seguito della ricezione di un messaggi
        @Override
        public void handleMessage(Message msg) {


            //mi prendo l'attivita principale dello splash
            final SplashActivity srcActivity = this.mActivityRef.get();

            //verifico se la mia activity è null
            if(srcActivity == null){

                Log.d(TAG_LOG,"Reference to SPlashActivity lost!");
                return;
            }

            //verifco il what del messaggio che dovrebbe essere 1 (GO_AHEAD_WHAT)
            switch (msg.what){

                case GO_AHEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis() - srcActivity.mStartTime;

                    //verifico se è passato abbastanza tempo e se se il passaggio all'attivita seguente
                    //non sia gia avvenuto
                    if(elapsedTime >= MIN_WAIT_INTERVAL && !srcActivity.mIsDone){
                        srcActivity.mIsDone = true;
                        srcActivity.goAhead();
                    }
                    break;
            }
        }

    }






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //mi serve per nascondere la barra di navigazione in basso
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);


        //diventa null solo in caso di ripristino
        if(savedInstanceState != null){
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
        }


        /*inizializzo handler tramite costruttore e gli passo l'attivita principale dello Splash
        questo mi permette di gestire i messaggi in coda ed evitare il memory leak portandomi dietro
        il riferimento al mio handler nell'attivita a seguire (MainActivity)*/
        mHandler = new UiHandler(this);


        //ricerco la mia immagine tramite id
        final ImageView logoSplash = (ImageView) findViewById(R.id.splash_imageview);


        //aggiungo evento Ontouch che si puo verificare dopo il tempo minimo 1.5s
        logoSplash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                long elapsedTime = SystemClock.uptimeMillis() - mStartTime;

                if(elapsedTime >= MIN_WAIT_INTERVAL && !mIsDone){
                    mIsDone = true;
                    goAhead();

                }
                else{

                    Log.d(TAG_LOG, "Troppo veloce!");
                }

                return true;
            }
        });


    }






    //questo evento si verifica quando parte l'applicazione
    @Override
    protected void onStart() {
        super.onStart();


        //verifica se non è gia stato assegnato un valore a mStartTime nel metodo OnStart()
        if(mStartTime == -1L){

            //istante prima della visualizzazione
            mStartTime = SystemClock.uptimeMillis();
        }


        //ottengo messaggio dall'handler
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);

        //tramite handler invio il messaggio
        mHandler.sendMessageAtTime(goAheadMessage,mStartTime + MAX_WAIT_INTERVAL);
        Log.d(TAG_LOG,"Handler message sent!");

    }







    /*questo evento si verifica quando cambia un elemento visuale per salvare lo stato
   e non ripartire dall'inizio, ma continuare, ad esempio il timer continua e non riparte*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*un Bundle è un contenitore nel quale salvare le informazioni che ci servono in futuro (put)
        tramite una chiave e un valore*/


        outState.putBoolean(IS_DONE_KEY, mIsDone);
        outState.putLong(START_TIME_KEY,mStartTime);

    }




    //ripristino lo stato prima del cambiamento di qualche elemento visuale, come da Portrait a Landscape
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        this.mIsDone = savedInstanceState.getBoolean(IS_DONE_KEY);
    }






    //faccio l'intent esplicito della attivita principale
    private void goAhead(){


        try{
            String targa = UserRepository.GetTarga(this);

            //Verifico se è gia stato prenotato un posto con la mia targa (es. da portale Web)
            WSService ws = WSService.getInstance();
            ws.CheckBooking(SplashActivity.this, targa);

        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
