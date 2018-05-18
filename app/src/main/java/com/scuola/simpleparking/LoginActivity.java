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
            "AB123CD:"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mTargaView;
    private EditText mPasswordView;
    private ProgressDialogJC mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mTargaView = (AutoCompleteTextView) findViewById(R.id.email);
        mTargaView.setText("AB123CD");

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = new ProgressDialogJC(this);
    }




    /**


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mTargaView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String targa = mTargaView.getText().toString();
        String password = mPasswordView.getText().toString();

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

           mProgressView.setMessage("Registrazione in corso...");
           mProgressView.setSpinnerType(2);
           mProgressView.show();
           mAuthTask = new UserLoginTask(targa, password);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }


        @Override
        protected String doInBackground(Void... params) {

            String response = null;

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return e.getMessage();
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");

                if(pieces.length == 1){
                    response = pieces[0];
                    return response;
                }

                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    response = pieces[0] + ":" + pieces[1];
                    return response;
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(final String response) {
            mAuthTask = null;
            mProgressView.dismiss();

            if (response != null) {

                try{
                    UserRepository.SetTarga(response, LoginActivity.this);

                    //creo nuova istanza di tipo intent e gli dico da dove a dove andare
                    Intent intent =new Intent (LoginActivity.this, MainActivity.class);
                    // faccio partitre l'intent appena creata
                    startActivity(intent);
                    finish();

                }catch (Exception e){

                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgressView.dismissWithFailure("Richiesta di registrazione cancellata!");
        }
    }
}

