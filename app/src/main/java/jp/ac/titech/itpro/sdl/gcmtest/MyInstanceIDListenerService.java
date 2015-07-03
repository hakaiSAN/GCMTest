package jp.ac.titech.itpro.sdl.gcmtest;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private final static String TAG = "MyInstanceIDListenerService";

    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegistrationService.class));
    }
}
