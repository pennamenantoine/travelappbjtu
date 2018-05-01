package com.example.antoine.retrofitest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antoine.retrofitest.data.model.Post;
import com.example.antoine.retrofitest.data.remote.APIService;
import com.example.antoine.retrofitest.data.remote.ApiUtils;

import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mResponseTv;
    private APIService mAPIService;
    private static final String TAG = "register_form";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText titleEt = (EditText) findViewById(R.id.et_title);
        final EditText bodyEt = (EditText) findViewById(R.id.et_body);
        final EditText pswdEt = (EditText) findViewById(R.id.et_pswd);
        final EditText confirm_pswdEt = (EditText) findViewById(R.id.confirm_pswd);
        Button submitBtn = (Button) findViewById(R.id.btn_submit);
        mResponseTv = (TextView) findViewById(R.id.tv_response);

        mAPIService = ApiUtils.getAPIService();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEt.getText().toString().trim();
                String body = bodyEt.getText().toString().trim();
                String pswd = pswdEt.getText().toString().trim();
                String confirm_pswd = confirm_pswdEt.getText().toString().trim();
                if (pswd.equals(confirm_pswd)) {
                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body) && !TextUtils.isEmpty(pswd)) {
                        sendPost(title, body, pswd);
                    }
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        SharedPreferences settings = getSharedPreferences("CoolPreferences", 0);
                        String pseudotocheck = settings.getString("pseudo", "");
                        String emailtocheck = settings.getString("email", "");
                        String pswdtocheck = settings.getString("pswd", "");
                        intent.putExtra("pseudo", pseudotocheck);
                        intent.putExtra("email", emailtocheck);
                        intent.putExtra("pswd", pswdtocheck);
                        startActivity(intent);
                }
                else {
                    Toast.makeText(getBaseContext(), "Password and confirm password must match",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendPost(String title, String body, String pswd) {
        mAPIService.savePost(title, body, pswd, 1).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()) {
                    showResponse(response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
    public void showResponse(String response) {
        if (mResponseTv.getVisibility() == View.GONE) {
        //   mResponseTv.setVisibility(View.VISIBLE);
        }
        mResponseTv.setText(response);
        parseResponse(response);

    }

    public void parseResponse(String response) {

        String[] arrayString = response.split(",");

        String pseudotocheck = arrayString[0];
        String emailtocheck = arrayString[1];
        String pswdtocheck = arrayString[2];

        pseudotocheck = pseudotocheck.substring(pseudotocheck.indexOf("Post{title=") + 11, pseudotocheck.length());
        emailtocheck = emailtocheck.substring(emailtocheck.indexOf("body=") + 5, emailtocheck.length());
        pswdtocheck = pswdtocheck.substring(pswdtocheck.indexOf("pswd=") + 5, pswdtocheck.length());
        pseudotocheck = pseudotocheck.replaceAll("'", "");
        emailtocheck = emailtocheck.replaceAll("'", "");
        pswdtocheck = pswdtocheck.replaceAll("'", "");

        SharedPreferences settings = getSharedPreferences("CoolPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pseudo", pseudotocheck);
        editor.putString("email", emailtocheck);
        editor.putString("pswd", pswdtocheck);
        editor.commit();
    }
}
