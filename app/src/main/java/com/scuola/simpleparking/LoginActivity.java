package com.scuola.simpleparking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scuola.simpleparking.common.ProgressDialogJC;
import com.scuola.simpleparking.common.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
@SuppressLint("Registered")
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            ":"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mTargaView;
    private EditText mIpEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mTargaView = (AutoCompleteTextView) findViewById(R.id.email);
        String targa = null;
        String ip = null;

        try{
            //Provo a vedere se ho gia salvato la targa
            targa = UserRepository.GetTarga(this);
            ip = UserRepository.GetIp(this);

        }catch (Exception e){

            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(targa != null){
            mTargaView.setText(targa);
        }

        mIpEditText = (EditText) findViewById(R.id.ip);

        if(ip != null){
            mIpEditText.setText(ip);
        }

        mIpEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }




    /**


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mTargaView.setError(null);
        mIpEditText.setError(null);

        // Store values at the time of the login attempt.
        String targa = mTargaView.getText().toString();
        String ip = mIpEditText.getText().toString();

        if(targa.length() <= 7){

            boolean cancel = false;
            View focusView = null;


            // Check for a valid email address.
            if (TextUtils.isEmpty(targa)) {
                mTargaView.setError(getString(R.string.error_field_required));
                focusView = mTargaView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                try{
                    UserRepository.SetTarga(targa, LoginActivity.this);
                    UserRepository.SetIp(ip, LoginActivity.this);

                    //creo nuova istanza di tipo intent e gli dico da dove a dove andare
                    Intent intent =new Intent (LoginActivity.this, MainActivity.class);
                    // faccio partitre l'intent appena creata
                    startActivity(intent);
                    finish();
                }catch (Exception e){

                    Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
                }


        }


        }else{
            mTargaView.setError("La targa deve essere compresa tra 1 e 7 caratteri");

        }
    }


}

