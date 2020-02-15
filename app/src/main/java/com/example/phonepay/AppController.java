package com.example.phonepay;

import android.app.Application;

import com.example.phonepay.threading.BusinessExecutor;
import com.example.phonepay.threading.IBusinessExecutor;

public class AppController extends Application {

    private static AppController mInstance;
    private IBusinessExecutor businessExecutor;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        businessExecutor = BusinessExecutor.getInstance();

    }
}
