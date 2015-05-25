package com.example.manjari.mahakumbh;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationIntentService extends IntentService {

    public static final int DEFAULT_SOUND=1;
    public static final String notifTitle="A new Chitthi received!!";
     /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
     public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            GoogleCloudMessaging gcm= GoogleCloudMessaging.getInstance(this);
            String desc=intent.getStringExtra("mail_body");
            String title=intent.getStringExtra("mail_subject");
            String from=intent.getStringExtra("mail_from");
            String date=intent.getStringExtra("mail_date");
            String messageType=gcm.getMessageType(intent);
            if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
                sendNotification("Message has been deleted from the server",null);
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
                sendNotification("Send error in gcm",null);
            }
            else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
                sendNotification(desc,title);
            }
            NotificationReceiver.completeWakefulIntent(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(String msg,String title){
        int notify_id=1;

        Intent detailIntent=new Intent(this,MenuActivity.class);

        detailIntent.putExtra("msg", msg);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MenuActivity.class);
        stackBuilder.addNextIntent(detailIntent);

        PendingIntent contentIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification=new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.splash);
        //notification.setVibrate(new long[]{100, 100});
        notification.setContentTitle(notifTitle);
        notification.setContentText(title);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        notification.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManager notifManage=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifManage.notify(notify_id,notification.build());
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
