package jp.ac.titech.itpro.sdl.gcmtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    public final static String TOKEN_REGISTERED = "tokenRegistered";
    public final static String REGISTRATION_COMPLETE = "registrationComplete";

    private TextView statusView;

    private BroadcastReceiver regReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
                != ConnectionResult.SUCCESS) {
            Toast.makeText(this, R.string.google_play_service_unavailable,
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        statusView = (TextView) findViewById(R.id.status_view);

        regReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                if (pref.getBoolean(TOKEN_REGISTERED, false)) {
                    Log.i(TAG, "registered");
                    statusView.setText(getString(R.string.status_registered));
                }
                else {
                    Log.i(TAG, "unregistered");
                    statusView.setText(getString(R.string.status_unregistered));
                }
            }
        };

        startService(new Intent(this, RegistrationService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(regReceiver, new IntentFilter(REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(regReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
