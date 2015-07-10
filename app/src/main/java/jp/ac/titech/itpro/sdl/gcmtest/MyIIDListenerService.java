package jp.ac.titech.itpro.sdl.gcmtest;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyIIDListenerService extends InstanceIDListenerService {
    private final static String TAG = "MyIIDListenerService";

    @Override
    public void onTokenRefresh() {
        Log.i(TAG, "onTokenRefresh");
        startService(new Intent(this, RegistrationService.class));
    }
}
