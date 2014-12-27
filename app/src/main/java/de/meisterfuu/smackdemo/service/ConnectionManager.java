package de.meisterfuu.smackdemo.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

/**
 * Created by Furuha on 27.12.2014.
 */
public class ConnectionManager {


    private final Context mContext;
    private boolean mActive;
    private Thread mThread;
    private Handler mTHandler;
    private ChatConnection mConnection;

    public ConnectionManager(Context pContext) {
        mActive = false;
        mContext = pContext;
    }

    public void start() {
        if (!mActive) {
            mActive = true;

            // Create ConnectionThread Loop
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mTHandler = new Handler();
                        initConnection();
                        Looper.loop();
                    }

                });
                mThread.start();
            }

        }
    }

    public void stop() {
        mActive = false;
        mTHandler.post(new Runnable() {

            @Override
            public void run() {
                if(mConnection != null){
                    mConnection.disconnect();
                }
            }
        });

    }

    private void initConnection() {
        if(mConnection == null){
            mConnection = new ChatConnection(mContext);
        }
        try {
            mConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }
}
