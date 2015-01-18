package de.meisterfuu.smackdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.meisterfuu.smackdemo.service.SmackConnection;
import de.meisterfuu.smackdemo.service.SmackService;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private Button button;
    private EditText edPW;
    private EditText edJID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String Password = PreferenceManager.getDefaultSharedPreferences(this).getString("xmpp_password", null);
        String Service = PreferenceManager.getDefaultSharedPreferences(this).getString("xmpp_jid", null);

        button = (Button)this.findViewById(R.id.button);

        if(!SmackService.getState().equals(SmackConnection.ConnectionState.DISCONNECTED)){
            button.setText("Disconnect");
            this.startActivity(new Intent(this, ChatActivity.class));
        }



        edPW = (EditText)this.findViewById(R.id.ed_password);
        edJID = (EditText)this.findViewById(R.id.ed_jid);

        if(Password != null){
            edPW.setText(Password);
        }
        if(Service != null){
            edJID.setText(Service);
        }

        button.setOnClickListener(this);

    }

    private void save() {
        if(!verifyJabberID(edJID.getText().toString())){
            Toast.makeText(this, "Invalid JID", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!SmackService.getState().equals(SmackConnection.ConnectionState.DISCONNECTED)){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
            .putString("xmpp_jid", edJID.getText().toString())
            .putString("xmpp_password", edPW.getText().toString())
            .commit();

            button.setText("Disconnect");
            Intent intent = new Intent(this, SmackService.class);
            this.startService(intent);

            this.startActivity(new Intent(this, ChatActivity.class));
        } else {
            button.setText("Connect");
            Intent intent = new Intent(this, SmackService.class);
            this.stopService(intent);
        }

    }

//    private boolean isServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (SmackService.class.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean verifyJabberID(String jid){
        try {
            String parts[] = jid.split("@");
            if (parts.length != 2 || parts[0].length() == 0 || parts[1].length() == 0){
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        save();
    }
}
