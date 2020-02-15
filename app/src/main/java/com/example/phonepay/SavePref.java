package com.example.phonepay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {

    int mode = Activity.MODE_PRIVATE;
    Context ctx;
    private SharedPreferences prefs;

    public SavePref(Context ctx) {
        this.ctx = ctx;
        prefs = this.ctx.getSharedPreferences(PhonePayConstants.PHONEPAYPREF, mode);

    }

    public SavePref() {
        this.ctx = AppController.getInstance();
        prefs = this.ctx.getSharedPreferences(PhonePayConstants.PHONEPAYPREF, mode);

    }

    public void saveGameLevel(int level) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PhonePayConstants.SAVEGAMELEVEL, level);
        editor.commit();
    }


}
