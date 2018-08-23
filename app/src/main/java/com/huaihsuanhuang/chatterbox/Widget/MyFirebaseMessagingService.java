package com.huaihsuanhuang.chatterbox.Widget;

import android.app.NotificationManager;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.huaihsuanhuang.chatterbox.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // ...
        String TAG = "Getmsg";

        Log.d(TAG, "messagefrom: " + remoteMessage.getFrom());
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp).setContentTitle("Title").setContentText("Context");
        int mNotification_id = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(mNotification_id,builder.build());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String messagefromcloud = remoteMessage.getNotification().getBody();
            Log.d("fromcloud", messagefromcloud);
        }
//remind didn't retrieve the cm p21
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                String messagefrom = remoteMessage.getNotification().getBody();
                Log.e("newToken", newToken);


            }
        });
    }
}
