package com.webviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // This button will pass the Google website url to MyWebView.class
    Button gbtn = findViewById(R.id.googleButton);
    gbtn.setOnClickListener(view -> {
      MyWebView.url = "https://www.google.com/";
      Intent intent = new Intent(MainActivity.this, MyWebView.class);
      startActivity(intent);
    });

    // This button will pass the techStop website url to MyWebView.class
    Button tsbtn = findViewById(R.id.tsButton);
    tsbtn.setOnClickListener(view -> {
      MyWebView.url = "https://techstop.github.io/";
      Intent intent = new Intent(MainActivity.this, MyWebView.class);
      startActivity(intent);
    });
  }
}
