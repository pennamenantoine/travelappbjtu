package com.example.antoine.retrofitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import static com.example.antoine.retrofitest.R.layout.activity_login;

public class Login extends Activity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(activity_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(Login.this, travel_plan.class);
                startActivity(intent);
            }
            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

        final EditText pseudoEt = (EditText) findViewById(R.id.Pseudo);
        final EditText passwordEt = (EditText) findViewById(R.id.Password);

        final Button connectButton = (Button) findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn()) {
                    Intent intent = new Intent(Login.this, travel_plan.class);
                    startActivity(intent);
                }
                SharedPreferences prefs = getSharedPreferences("CoolPreferences", 0);
                String mpseudotocheck = prefs.getString("pseudo", "");
                String mpswdtocheck = prefs.getString("pswd", "");
                final String pseudo = pseudoEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                if (pseudo.equals(mpseudotocheck) && password.equals(mpswdtocheck)) {
                    Intent intent = new Intent(Login.this, travel_plan.class);
                    startActivity(intent);
                }
                if(!pseudo.equals(mpseudotocheck) && !password.equals(mpswdtocheck) && !isLoggedIn()){
                    Toast.makeText(getBaseContext(), "Incorrect Pseudo or password please try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
