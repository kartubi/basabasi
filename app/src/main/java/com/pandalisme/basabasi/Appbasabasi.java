package com.pandalisme.basabasi;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by kartubi on 20/12/2017.
 */

public class Appbasabasi extends Application {


    @Override
    public void onCreate(){
        super.onCreate();
        OneSignal.startInit(this)
                .autoPromptLocation(false)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();



    }



    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler{

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;

            if (data != null) {
                Log.d("DATANYA", String.valueOf(data));
                String trigger;
                trigger = data.optString("type", null);
                switch (trigger){
                    case "url":

                        break;
                    case "news":

                        break;
                }

            }

            /*
               Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openURL", openURL);
            Log.i("OneSignalExample", "openURL = " + openURL);
            // startActivity(intent);
            startActivity(intent);
             */
        }
    }

    private class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler{

        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String customKey;
            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            if (data != null) {
                Log.d("DATANYA", data.toString());
            }
        }
    }
}
