package de.meisterfuu.smackdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SmackService extends Service {


    public static final String NEW_MESSAGE = "de.meisterfuu.smackdemo.newmessage";
    public static final String SEND_MESSAGE = "de.meisterfuu.smackdemo.sendmessage";
    public static final String NEW_ROSTER = "de.meisterfuu.smackdemo.newroster";

    public static final String BUNDLE_FROM_JID = "b_from";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_ROSTER = "b_body";
    public static final String BUNDLE_TO = "b_to";

    private ConnectionManager mManager;

    public SmackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mManager = new ConnectionManager(SmackService.this);
        mManager.start();

        return Service.START_STICKY;
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mManager != null){
            mManager.stop();
        }
    }
}
