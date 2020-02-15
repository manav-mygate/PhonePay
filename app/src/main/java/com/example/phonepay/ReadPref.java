package com.example.phonepay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ReadPref {

    int mode = Activity.MODE_PRIVATE;
    String TAG = "ReadPref";
    Context ctx;
    //  String res = "";
    private SharedPreferences prefs;

    public ReadPref(Context ctx) {
        this.ctx = ctx;
        prefs = this.ctx.getSharedPreferences(PhonePayConstants.PHONEPAYPREF, mode);
    }

    public ReadPref() {
        this.ctx = AppController.getInstance();
        prefs = this.ctx.getSharedPreferences(PhonePayConstants.PHONEPAYPREF, mode);
    }


    public int getGameLevel() {
        try {
            return prefs.getInt(PhonePayConstants.SAVEGAMELEVEL, 0);
        } catch (ClassCastException e) {
            return 0;
        }
    }

}