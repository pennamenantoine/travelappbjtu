package com.example.antoine.retrofitest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class travel_plan extends AppCompatActivity {
    ImageView iv;
    String imagePath = "http://travelsouth.us/img/sample-itinerary-format-fieldstation-of-travel-proposal-sample.jpg";
    boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_plan);
        final Button downloadtpbtn = (Button) findViewById(R.id.my_travel_plans);
        final Button pdfplan = (Button) findViewById(R.id.pdf_plan);
        iv = (ImageView) findViewById(R.id.testCrop);

        downloadtpbtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 imageDownload image = new imageDownload(travel_plan.this, iv);
                                                 image.execute(imagePath);
                                                 final WebView mywebview = (WebView) findViewById(R.id.webview);
                                                 mywebview.loadUrl("http://travelsouth.us");
                                             }
                                         }
        );
        pdfplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(travel_plan.this, pdf_plan.class);
                startActivity(intent);
            }
        });
    }

    class imageDownload extends AsyncTask<String, Integer, Bitmap> {
        Context context;
        ImageView imageView;
        Bitmap bitmap;
        InputStream in = null;
        int responseCode = -1;

        public imageDownload(Context context, ImageView imageView) {
            this.context = context;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap data) {
            imageView.setImageBitmap(data);
            Toast.makeText(getBaseContext(), "Travel plan successfully downloaded!",
                    Toast.LENGTH_LONG).show();
        }
    }
}
