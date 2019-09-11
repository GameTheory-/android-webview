package com.webviewexample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Objects;

public class MyWebView extends AppCompatActivity {
    // Declare our web view.
    private WebView webview;

    // We can pass URLs from any other class to this string.
    static String url;

    // Optional progress bar for page loading progress.
    private ProgressBar progress;

    // A request code for our storage write request.
    private static final int WRITE_STORAGE = 1;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Optional progress bar for page loading progress.
        progress = findViewById(R.id.progressBar);

        // Attach our web view to our layout.
        webview = findViewById(R.id.webView);
        // Needed to interact with elements on most web pages.
        webview.getSettings().setJavaScriptEnabled(true);
        // This enables pinch to zoom.
        webview.getSettings().setBuiltInZoomControls(true);
        // Do not display the zoom controls on the screen.
        webview.getSettings().setDisplayZoomControls(false);
        // Loads a page to fit in the screen (zoomed out).
        webview.getSettings().setLoadWithOverviewMode(true);
        // Do not constrain the viewport to the web view dimensions.
        webview.getSettings().setUseWideViewPort(true);
        // Attach our web view to our WebViewClient.
        webview.setWebViewClient(new MyWebViewClient());
        // We call our url string variable to load.
        webview.loadUrl(url);

        // Adds a DownloadListener to our web view. This allows us to click on
        // download links and have items download to our downloads directory.
        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
                String cookie = CookieManager.getInstance().getCookie(url);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                request.addRequestHeader("Cookie", cookie);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // Check for storage write permission before attempting to download.
                if (getPermission()) {
                    Objects.requireNonNull(dm).enqueue(request);
                }
            }
        });

    }

    // Check if storage write permission has been granted. If not, request it.
    private boolean getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // True if permission granted exists.
                return true;
            } else {
                // We ask for storage write permission if it doesn't exist.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                return false;
            }
        } else {
            // True for SDK < 23
            return true;
        }
    }

    // Check if user granted or denied storage write permission when requested and
    // run code for either situation. This method is optional, but useful.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Attach our web view to our WebViewClient.
    private class MyWebViewClient extends WebViewClient {
        // This method makes sure that any links we click will open within
        // our app instead of launching in the default web browser.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        // Since this method is called every time a new page starts to load, it's
        // perfect to run our Progress Bar.
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        // Called whenever a page has finished loading. Here we can end our
        // page loading progress bar and take it out of view.
        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

    }

    // If we have clicked through multiple pages, this allows the back button
    // to take us back through each page.
    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
