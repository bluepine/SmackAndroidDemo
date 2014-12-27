package de.meisterfuu.smackdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.meisterfuu.smackdemo.service.SmackService;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private Button button;
    private EditText edPW;
    private EditText edUser;
    private EditText edEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String Password = PreferenceManager.getDefaultSharedPreferences(this).getString("xmpp_password", null);
        String Username = PreferenceManager.getDefaultSharedPreferences(this).getString("xmpp_username", null);
        String Endpoint = PreferenceManager.getDefaultSharedPreferences(this).getString("xmpp_endpoint", null);
        boolean connected = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xmpp_started", false);

        if(isServiceRunning()){
            this.startActivity(new Intent(this, ChatActivity.class));
        }

        button = (Button)this.findViewById(R.id.button);

        edPW = (EditText)this.findViewById(R.id.ed_password);
        edUser = (EditText)this.findViewById(R.id.ed_username);
        edEndpoint = (EditText)this.findViewById(R.id.ed_endpoint);

        if(Password != null){
            edPW.setText(Password);
        }
        if(Password != null){
            edUser.setText(Username);
        }
        if(Password != null){
            edEndpoint.setText(Endpoint);
        }

        button.setOnClickListener(this);

    }

    private void save(){
        boolean connected = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xmpp_started", false);
        if(!connected){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
            .putString("xmpp_username", edUser.getText().toString())
            .putString("xmpp_password", edPW.getText().toString())
            .putString("xmpp_endpoint", edEndpoint.getText().toString())
            .putBoolean("xmpp_started", true)
            .commit();

            button.setText("Disconnect");
            Intent intent = new Intent(this, SmackService.class);
            this.startService(intent);

            this.startActivity(new Intent(this, ChatActivity.class));
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
            .putBoolean("xmpp_started", false)
            .commit();
            button.setText("Connect");
            Intent intent = new Intent(this, SmackService.class);
            this.stopService(intent);

        }

    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SmackService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        save();
    }
}
