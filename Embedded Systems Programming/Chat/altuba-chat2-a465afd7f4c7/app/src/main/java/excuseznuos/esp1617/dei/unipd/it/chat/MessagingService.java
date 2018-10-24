package excuseznuos.esp1617.dei.unipd.it.chat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import android.support.v4.app.NotificationManagerCompat;

public class MessagingService extends Service {
    public static final String REPLY_ACTION = "REPLY";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String EXTRA_REMOTE_REPLY = "extra_remote_reply";
    public static final String MSG_SEND_NOTIFICATION = "1";
    public static final String MSG_REPLY_NOTIFICATION = "10";
    private NotificationManagerCompat mNotificationManager;
    private int idNot = 0;

    @Override
    public void onCreate() {
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            if(intent.getAction().equals(MessagingService.MSG_SEND_NOTIFICATION)) {
                idNot = (int) System.currentTimeMillis();
                String dest = intent.getStringExtra("member");
                //richiamo asynctask per l'invio di un messaggio da writemessage
                SendNotification m = new SendNotification(getApplicationContext(), mNotificationManager, dest, idNot, true);

                m.execute();
            }
            else if (intent.getAction().equals(MessagingService.MSG_REPLY_NOTIFICATION)) {
                idNot = (int) System.currentTimeMillis();
                String dest = intent.getStringExtra("member");
                //richiamo asynctask per l'invio di una risposta
                SendNotification m = new SendNotification(getApplicationContext(), mNotificationManager, dest, idNot, false);

                m.execute();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class ServiceReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //uso il reciver per ottenere intent di risposta o di nuovi messaggi
            if(intent != null) {
                if(intent.getAction().equals(MSG_SEND_NOTIFICATION)) {
                    startService(intent);
                }
                else if (intent.getAction().equals(MSG_REPLY_NOTIFICATION)) {
                    startService(intent);
                }
            }
        }
    }
}
