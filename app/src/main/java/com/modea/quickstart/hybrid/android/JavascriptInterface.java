package com.modea.quickstart.hybrid.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class JavascriptInterface {
    private final Context context;

    JavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void sayHelloWorld() {
        final JavascriptInterface thisJavascriptInterface = this;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(thisJavascriptInterface.context, "Hello World", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
