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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";

    public final static String TOKEN = "token";
    public final static String TOKEN_REGISTERED = "tokenRegistered";
    public final static String REGISTRATION_COMPLETE = "registrationComplete";

    public final static String MESSAGE_EXTRA = "message";
    public final static String MESSAGE_RECEIVED = "messageReceived";

    private final static String KEY_OUTPUT = "output";

    private TextView statusView;
    private TextView idView;
    private ArrayAdapter<String> outputAdapter;
    private ArrayList<String> output;

    private BroadcastReceiver regReceiver;
    private BroadcastReceiver msgReceiver;

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

        if (savedInstanceState != null)
            output = savedInstanceState.getStringArrayList(KEY_OUTPUT);
        if (output == null)
            output = new ArrayList<>();

        statusView = (TextView) findViewById(R.id.status_view);
        idView = (TextView) findViewById(R.id.id_view);
        ListView outputView = (ListView)findViewById(R.id.output_view);
        outputAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, output);
        outputView.setAdapter(outputAdapter);

        regReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshUI();
            }
        };

        msgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(MESSAGE_EXTRA);
                Log.i(TAG, "onReceive: message=" + message);
                outputAdapter.add(message);
            }
        };

        startService(new Intent(this, RegistrationService.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        for (String m: output)
            Log.i(TAG, m);
        outState.putStringArrayList(KEY_OUTPUT, output);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        refreshUI();
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(regReceiver, new IntentFilter(REGISTRATION_COMPLETE));
        bm.registerReceiver(msgReceiver, new IntentFilter(MESSAGE_RECEIVED));
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(regReceiver);
        bm.unregisterReceiver(msgReceiver);
        super.onPause();
    }

    private void refreshUI() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean(TOKEN_REGISTERED, false)) {
            String id = pref.getString(TOKEN, getString(R.string.id_default));
            Log.i(TAG, "registered id=" + id);
            statusView.setText(getString(R.string.status_registered));
            idView.setText(id);
        }
        else {
            Log.i(TAG, "unregistered");
            statusView.setText(getString(R.string.status_unregistered));
            idView.setText(getString(R.string.id_default));
        }
    }
}
