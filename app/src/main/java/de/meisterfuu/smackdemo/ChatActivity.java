package de.meisterfuu.smackdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import de.meisterfuu.smackdemo.service.SmackService;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener {

    private Button button;
    private EditText edTo;
    private EditText edMsg;
    private BroadcastReceiver mReceiver;
    private String log;
    private TextView edLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        button = (Button)this.findViewById(R.id.button);

        edTo = (EditText)this.findViewById(R.id.ed_to);
        edMsg = (EditText)this.findViewById(R.id.ed_text);
        edLog = (TextView)this.findViewById(R.id.log);
        log = "";
        button.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("DEBUG", "new MEssage");
                String action = intent.getAction();
                if (action.equals(SmackService.NEW_MESSAGE)) {
                    String from = intent.getStringExtra(SmackService.BUNDLE_FROM_JID);
                    String message = intent.getStringExtra(SmackService.BUNDLE_MESSAGE_BODY);

                    log = from+": "+message+"\n"+log;

                } else  if(action.equals(SmackService.NEW_ROSTER)){
                    ArrayList<String> roster = intent.getStringArrayListExtra(SmackService.BUNDLE_ROSTER);
                    if(roster == null){
                        return;
                    }
                    for (String s: roster){
                        log = s+"\n"+log;
                    }
                }
                edLog.setText(log);
            }
        };

        IntentFilter filter = new IntentFilter(SmackService.NEW_ROSTER);
        filter.addAction(SmackService.NEW_MESSAGE);
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        sendMessage();
    }

    public void sendMessage() {
        Intent intent = new Intent(SmackService.SEND_MESSAGE);
        intent.setPackage(this.getPackageName());
        intent.putExtra(SmackService.BUNDLE_MESSAGE_BODY, edMsg.getText().toString());
        intent.putExtra(SmackService.BUNDLE_TO, edTo.getText().toString());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        this.sendBroadcast(intent);
    }
}
