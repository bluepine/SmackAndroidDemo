package de.meisterfuu.smackdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import de.meisterfuu.smackdemo.R;

public class SmackService extends Service {


    public static final int NOTIFICATION_ID = 42;

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("SmackService started");
        builder.setContentTitle("SmackService");
        builder.setContentText("");
        builder.setOngoing(true);
        builder.setSmallIcon(R.drawable.ic_stat_service);
        this.startForeground(NOTIFICATION_ID, builder.build());
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
        stopForeground(true);
    }
}
