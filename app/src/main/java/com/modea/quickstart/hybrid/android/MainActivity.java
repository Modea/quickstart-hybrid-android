package com.modea.quickstart.hybrid.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//import org.apache.commons.io.IOUtils;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.removeTitleBar();
        this.removeNotificationBar();

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        this.setupWebView(webView);
        this.loadWebView(webView);
    }

    private void removeTitleBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void removeNotificationBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupWebView(WebView webView) {
        this.enableChromeDebuggingOnWebView();
        this.configureWebSettingsOnWebView(webView);
        this.addJavascriptInterfaceToWebView(webView);
        this.handleJavascriptAlertsByWebView(webView);
    }

    private void enableChromeDebuggingOnWebView() {
        WebView.setWebContentsDebuggingEnabled(true);
    }

    private void configureWebSettingsOnWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
    }

    private void addJavascriptInterfaceToWebView(WebView webView) {
        JavascriptInterface javaScriptInterface = new JavascriptInterface(this);
        webView.addJavascriptInterface(javaScriptInterface, "Android");
    }

    private void handleJavascriptAlertsByWebView(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();

                return true;
            }

            ;
        });
    }

    private void loadWebView(WebView webView) {
        String html = this.getIndexHtmlFileContents();
        webView.loadDataWithBaseURL("file:///android_asset/website/", html, "text/html", "UTF-8", null);
    }

    private String getIndexHtmlFileContents() {
        StringBuilder htmlStringBuilder = new StringBuilder();

        try (InputStream inputStream = this.getAssets().open("website/index.html")) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    String htmlLine;
                    while ((htmlLine = bufferedReader.readLine()) != null) {
                        htmlStringBuilder.append(htmlLine);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Failed to read website/index.html", e);
        }

        return htmlStringBuilder.toString();
    }
}
