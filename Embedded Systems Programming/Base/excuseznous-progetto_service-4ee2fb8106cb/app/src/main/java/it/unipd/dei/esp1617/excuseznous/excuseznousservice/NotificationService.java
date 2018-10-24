package it.unipd.dei.esp1617.excuseznous.excuseznousservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static android.app.Notification.PRIORITY_HIGH;

public class NotificationService extends Service {

    private static final String LOG_TAG = "NotificationService";
    public static boolean IS_SERVICE_RUNNING = false;
    public static String Status = Constants.STATUS.START;
    public static int maxmex=-1;
    public static int interval=5;
    public static boolean vibration = false;
    public static boolean ringtone = false;
    private boolean service = false;
    static int idNot = 0;
    static int count=0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if ((!service)&&(intent.getAction().equals(Constants.ACTION.PLAY_ACTION))) {

            Status = Constants.STATUS.START;
            SharedPreferences preferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
            interval = preferences.getInt("intervallo",5);
            maxmex = preferences.getInt("maxmex", -1);
            ringtone = preferences.getBoolean("ringtone", false);
            vibration = preferences.getBoolean("vibration", false);
            service =true;
            Log.i(LOG_TAG, "Start Foreground");
            IS_SERVICE_RUNNING=true;
            showNotification();
            doPlay();
        }
        else {
            if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)) //pause notification
            {
                doPause();

                MainActivity.btnpause.setEnabled(false);
                MainActivity.btnplay.setEnabled(true);
                MainActivity.btncancel.setEnabled(true);

            } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) //play notification
            {
                doPlay();
                MainActivity.btnpause.setEnabled(true);
                MainActivity.btnplay.setEnabled(false);
                MainActivity.btncancel.setEnabled(true);
            } else if (intent.getAction().equals(Constants.ACTION.CANCEL_ACTION)) //stop notification
            {
                //fermo l'invio delle notifiche
                MainActivity.btnpause.setEnabled(false);
                MainActivity.btnplay.setEnabled(true);
                MainActivity.btncancel.setEnabled(false);
                notificationTask.cancel(true);
                stopForeground(true);
                Log.i(LOG_TAG, "Stop");
                Status = Constants.STATUS.START;
                service =false;
                stopSelf();
            }
        }
        return START_STICKY;

    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.CANCEL_ACTION);
        PendingIntent pstopIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notification);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Excusez Nous")
                .setTicker("Excusez Nous")
                .setContentText("Scegli un' azione")
                .setGroup("Manager")
                .setPriority(PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_pause, "Pausa", ppauseIntent))
                .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_play, "Play", pplayIntent))
                .addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.ic_cancel, "Termina", pstopIntent)).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }

    private NotificationTask notificationTask;
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "OnDestroy");
        IS_SERVICE_RUNNING =false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void doPlay() {
        if(Status.equals(Constants.STATUS.REPLAY))
        {
            //riparto ad inviare notifiche
            Status=Constants.STATUS.PLAY;
            if(maxmex!=-1) {
                if(count<maxmex) {
                    notificationTask = new NotificationTask(this, interval, maxmex-count);
                    notificationTask.execute();
                }
                else {
                    notificationTask = new NotificationTask(this, interval, maxmex);
                    notificationTask.execute();
                }
            }
            else {

                notificationTask = new NotificationTask(this, interval, maxmex);

                    notificationTask.execute();
            }
            Log.i(LOG_TAG, "Play");
        }
        if (Status.equals(Constants.STATUS.START))
        {
            //faccio partire le notifiche
            Status=Constants.STATUS.PLAY;
            if(maxmex!=-1) {
                notificationTask = new NotificationTask(this, interval, maxmex);
                    notificationTask.execute();
                //riabilitare bottoni main activity
            }
            else {
                notificationTask = new NotificationTask(this, interval, maxmex);
                notificationTask.execute();
            }
            Log.i(LOG_TAG, "Start");
        }
    }

    private void doPause() {
        if(Status.equals(Constants.STATUS.PLAY))
        {
            //abilitare pause e stop
            Status = Constants.STATUS.REPLAY;
            notificationTask.cancel(true);
            Log.i(LOG_TAG, "Pause");
        }

    }
}
