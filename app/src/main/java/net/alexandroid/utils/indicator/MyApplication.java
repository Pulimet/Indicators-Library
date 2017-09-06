package net.alexandroid.utils.indicator;

import android.app.Application;

import net.alexandroid.shpref.MyLog;
import net.alexandroid.shpref.ShPref;

/**
 * Created on 9/5/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShPref.init(this, ShPref.APPLY);
        MyLog.setTag("ZAQ");
        MyLog.showLogs(true);
    }
}
