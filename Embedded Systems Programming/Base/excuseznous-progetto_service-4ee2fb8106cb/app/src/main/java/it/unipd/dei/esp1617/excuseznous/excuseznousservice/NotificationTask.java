package it.unipd.dei.esp1617.excuseznous.excuseznousservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import java.util.Random;

class NotificationTask extends AsyncTask<Integer, Double, Void> {
    //dichiaro variabili della classe
    private final static String TAG = NotificationTask.class.getName();
    private NotificationCompat.Builder mBuilder;
    private final Context mContext;
    private NotificationManager mNotifyManager;
    private int interval;
    private int maxmex;
    //array di stringhe per campi Text e Title notifica
    private String[] messages = new String[25];
    private String[] from = new String[8];

    public NotificationTask(Context context, int step, int mex) {
        mContext = context;
        interval = step;
        maxmex = mex;
        messages = mContext.getResources().getStringArray(R.array.messages);
        from = mContext.getResources().getStringArray(R.array.from);
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        super.onPreExecute();
        //settaggio notificationManager e notificationBuilder
        initNotification();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        //Log.d(TAG, "doInBackground");
        if (maxmex > 0) {
            //numero messaggi limitato
            for (int i = 0; i < maxmex; i++) {
                if(isCancelled()) break;
                setStartedNotification();
                NotificationService.count++;
                if(i<(maxmex-1)) {
                    //se ho appena mandato l'ultima notifica salto l'attesa
                    try {
                        Thread.sleep(interval * 1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        } else {
            //numero messaggi illimitato
            boolean flag = true;
            while (flag) {
                //continuo finchè l'utente non bloccherà l'invio
                if(isCancelled()) break;
                setStartedNotification();
                NotificationService.count++;
                try {
                    Thread.sleep(interval * 1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        Log.d(TAG, "onPostExecute");
        super.onPostExecute(result);
        /*
            questo metodo sarà eseguito solo nel caso sia stato impostato l'invio di un numero limitato di messaggi
            e il Task li abbia inoltrati tutti
        */
        //setto in maniera appropriata le variabili di NotificationService e sistemo lo stato dei bottoni della MainActivity
        MainActivity.btnplay.setEnabled(true);
        MainActivity.btnpause.setEnabled(false);
        MainActivity.btncancel.setEnabled(true);
        NotificationService.Status = Constants.STATUS.REPLAY;
        NotificationService.count=0;
    }

    private void initNotification() {
        mNotifyManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
    }

    private void setStartedNotification() {
        //costruzione della notifica in base alle impostazioni
        Random r = new Random();
        int m = r.nextInt(24);
        int f = r.nextInt(7);
        if(NotificationService.vibration) {
            //vibrazione attiva
            long vib[] = new long[2];
            vib[0]=0;
            vib[1]=500;
            if(NotificationService.ringtone) {
                //ringtone attiva vibrazione attiva
                Uri drin = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSmallIcon(R.drawable.ic_notification).setContentTitle(from[f])
                        .setContentText(messages[m])
                        .setVibrate(vib)
                        .setSound(drin)
                        .setAutoCancel(true);
            }
            else
            {
                //ringtone disattivata vibrazione attiva
                mBuilder.setSmallIcon(R.drawable.ic_notification).setContentTitle(from[f])
                        .setContentText(messages[m])
                        .setVibrate(vib)
                        .setAutoCancel(true);
            }
        }
        else {
            if(NotificationService.ringtone) {
                //vibrazione disattivata ringtone attiva
                Uri drin = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSmallIcon(R.drawable.ic_notification).setContentTitle(from[f])
                        .setContentText(messages[m])
                        .setSound(drin)
                        .setAutoCancel(true);
            }
            else
            {
                //vibrtazione disattivata ringtone disattivata
                mBuilder.setSmallIcon(R.drawable.ic_notification).setContentTitle(from[f])
                        .setContentText(messages[m])
                        .setAutoCancel(true);
            }
        }
        //collego il click sulla singola notifica all'apertura della MainActivity
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        //uso come identificatore della singola notifica la variabile idNot della classe NotificationService
        mNotifyManager.notify(NotificationService.idNot, mBuilder.build());
        NotificationService.idNot++;
    }
}