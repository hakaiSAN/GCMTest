package jp.ac.titech.itpro.sdl.gcmtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    private final static String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.i(TAG, "onMessageReceived: from=" + from);
        String message = data.getString("message");

        Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED);
        intent.putExtra(MainActivity.MESSAGE_EXTRA, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        sendNotification(message);
    }

    private void sendNotification(String message) {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int nid = 1;

        PendingIntent pi = PendingIntent.getActivity(this, nid,
                new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder nb = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("GCM")
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pi);

        nm.notify(nid, nb.build());
    }
}
