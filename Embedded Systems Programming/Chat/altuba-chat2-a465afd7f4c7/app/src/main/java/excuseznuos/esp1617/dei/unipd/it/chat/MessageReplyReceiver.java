package excuseznuos.esp1617.dei.unipd.it.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

public class MessageReplyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //se ho un id notifica consistente cancello la notifica a cui viene risposto
            if (MessagingService.REPLY_ACTION.equals(intent.getAction())) {
            int conversationId = intent.getIntExtra(MessagingService.CONVERSATION_ID, -1);
                String dest = intent.getStringExtra("member");
            if (conversationId != -1) {
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(context);
                notificationManager.cancel(conversationId);
            }
            //invio a MessagingService un intent per far rispondere
            Intent replyintent = new Intent(context, MessagingService.class)
                .setAction(MessagingService.MSG_REPLY_NOTIFICATION)
                    .putExtra("member", dest);
                context.startService(replyintent);
        }
    }
}
