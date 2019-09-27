package com.webviewexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  // This button will pass the Google website url to MyWebView.class
  public void googleButton(View view) {
    MyWebView.url = "https://www.google.com/";
    Intent intent = new Intent(this, MyWebView.class);
    startActivity(intent);
  }

  // This button will pass the techStop website url to MyWebView.class
  public void itgButton(View view) {
    MyWebView.url = "https://techstop.github.io/";
    Intent intent = new Intent(this, MyWebView.class);
    startActivity(intent);
  }

}
