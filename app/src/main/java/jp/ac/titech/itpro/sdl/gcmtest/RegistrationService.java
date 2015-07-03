package jp.ac.titech.itpro.sdl.gcmtest;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationService extends IntentService {
    private final static String TAG = "RegistrationService";
    private final static String[] TOPICS = { "global" };

    public RegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            synchronized (this) {
                InstanceID iid = InstanceID.getInstance(this);
                String token = iid.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "token: " + token);

                for (String topic: TOPICS)
                    GcmPubSub.getInstance(this).
                            subscribe(token, "/topics/" + topic, null);

                pref.edit().putBoolean(MainActivity.TOKEN_REGISTERED, true).apply();
            }
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
            pref.edit().putBoolean(MainActivity.TOKEN_REGISTERED, false).apply();
        }
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(new Intent(MainActivity.REGISTRATION_COMPLETE));
    }
}
