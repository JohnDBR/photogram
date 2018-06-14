package com.john.platzigram.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.john.platzigram.R;

//METHODS TO MAKE HTTP REQUEST ASYNCTASK AND VOLLEY
//METHODS TO GET IMAGES WITH ASYNC GLIDE, PICASSO, NETWORKIMAGEVIEW, UNIVERSAL IMAGE LOADER
//LOCAL DATA PERSISTENT WITH SHARED PREFERENCES
//BROADCAST RECEIVER FOR THE NOTIFICATIONS

//THREADING AVANZADO RX JAVA
//ANDROID ARSENAL TO SEARCH HEAVY STUFF

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void goContainerActivity(View view) {
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }

    public void goPlatziWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.platzi.com")));
    }
}
