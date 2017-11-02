package com.example.roman.vocabulary.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.roman.vocabulary.utilities.DebugUtility;
import com.example.roman.vocabulary.utilities.RetrofitUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

/**
 * Created by roman on 09.10.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getData().size() > 0) {
            DebugUtility.logTest(TAG, "Message data payload: " + remoteMessage.getData());
            if (!remoteMessage.getData().containsKey("default"))
                return;


            /*try {
                Gson gson = RetrofitUtils.getInstance().getGson();

                String info = remoteMessage.getData().get("default").toString();
                DebugUtility.logTest(TAG, "default: " + info);
                PushInfo pushInfo = gson.fromJson(info, PushInfo.class);
                if (pushInfo==null)
                    return;
                generateMessageNotification(getApplicationContext(),pushInfo);
                *//*switch (pushInfo.getType()) {
                    case "meeting":

                        break;


                }*//*

            } catch (Exception exception) {
                if (exception != null)
                    DebugUtility.logTest(TAG, exception.getMessage());
            }*/

        }


        if (remoteMessage.getNotification() != null) {
            DebugUtility.logTest(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    /*private void generateMessageNotification(Context context, PushInfo pushInfo) {
        if (pushInfo == null) {
            return;
        }
        if (pushInfo.getType().equals("meetingChat")&& EventBus.getDefault().hasSubscriberForEvent(ReceiveMessagePushEvent.class)) {
            DebugUtility.logTest(TAG, "------- hasSubscriberForEvent");
            EventBus.getDefault().post(new ReceiveMessagePushEvent(pushInfo));
            //return;
        }
        DebugUtility.logTest(TAG,"generateMessageNotification "+pushInfo.getMeetingId());
        Intent intent = new Intent(EXTRA_NEW_PUSH);
        intent.putExtra(EXTRA_NEW_PUSH, Parcels.wrap(pushInfo));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

*//*
        String titleText=context.getString(R.string.order_notification_text_1,orderPush.getCount());
        String contentText=context.getString(R.string.order_notification_text_2,orderPush.getProduct());*//*
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)//
                .setContentTitle(context.getString(R.string.app_name))
                //.setContentTitle(TextUtils.isEmpty(pushMessage.getTopic()) ? context.getString(R.string.app_name) : pushMessage.getTopic())
                .setContentText(pushInfo.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(pushInfo.getMessage()))//
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setTicker(pushInfo.getMessage())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        notificationManager.notify(pushInfo.getMeetingId(), notification);

    }*/
}
