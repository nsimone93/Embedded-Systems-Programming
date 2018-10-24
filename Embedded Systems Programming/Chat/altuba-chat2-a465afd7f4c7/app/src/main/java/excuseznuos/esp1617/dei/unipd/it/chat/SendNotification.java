package excuseznuos.esp1617.dei.unipd.it.chat;

import android.support.v4.app.NotificationManagerCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import java.util.Random;


class SendNotification extends AsyncTask<Integer, Double, Void> {
    //variabili
    private final Context mContext;
    private final NotificationManagerCompat mNotifyManager;
    private final String mDest;
    private final boolean fTime;
    private int idNot =0;

    public SendNotification(Context context, NotificationManagerCompat notificationManager, String dest, int id, boolean first) {
        mContext = context;
        mNotifyManager=notificationManager;
        mDest = dest;
        idNot=id;
        fTime = first;
    }

    private Intent getMessageReplyIntent() {
        //imposto l'intent per la risposta della notifica
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(MessagingService.REPLY_ACTION)
                .putExtra(MessagingService.CONVERSATION_ID, idNot)
                .putExtra("member", mDest);
    }

    private void sendMessage() {
        //imposto il remote input per il reply
        RemoteInput remoteInput = new RemoteInput.Builder(MessagingService.EXTRA_REMOTE_REPLY)
                .setLabel("Reply")
                .build();

        PendingIntent replyIntent = PendingIntent
                .getBroadcast(mContext, idNot, getMessageReplyIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action actionReplyByRemoteInput = new NotificationCompat.Action.Builder(
                R.drawable.ic_notification, "Reply", replyIntent)
                .addRemoteInput(remoteInput)
                .build();

        //imposto una vibrazione differente per ogni mittente
            long[] vib={0,0,0,0};
            Uri drin = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            int imn=-1;
            int col=-1;
            switch (mDest) {
                case "Alberto Tubaldo":
                    vib = new long[]{0,200,0,200};
                    imn=R.mipmap.ic_tubo;
                    col=R.color.Viola;
                    break;
                case "Davide Martini":
                    vib = new long[]{0,100,0,100};
                    imn=R.mipmap.ic_martini;
                    col=R.color.Rosso;
                    break;
                case "Matteo Salvagno":
                    vib = new long[]{0,300,0,200};
                    imn=R.mipmap.ic_matteo;
                    col=R.color.Verde;
                    break;
                case "Simone Nigro":
                    vib = new long[]{0,300,0,100};
                    imn=R.mipmap.ic_simo;
                    col=R.color.Blu;
                    break;
            }

            //richiamo il layout per la notifica
            RemoteViews contentSmall = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification_small);
            contentSmall.setImageViewResource(R.id.imagenotileft, imn);
            contentSmall.setTextViewText(R.id.title, mDest);
            contentSmall.setTextColor(R.id.title, mContext.getResources().getColor(col,null));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), imn))
                    .setContentTitle(mDest)
                    .setVibrate(vib)
                    .setLights(Color.BLUE, 5000, 500)
                    .setSound(drin)
                    .setContent(contentSmall)
                    .setCustomContentView(contentSmall)
                    .setColor(mContext.getResources().getColor(col,null))
                    .addAction(actionReplyByRemoteInput);

            if((new Random().nextInt(2))<1)
            {
                //invio immagine
                //imposto un'immagine casuale per l'invio
                int num=new Random().nextInt(5);
                Bitmap icon=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_chat);
                switch (num) {
                    case 0:
                        icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.chess);
                        break;
                    case 1:
                        icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.coniglio);
                        break;
                    case 2:
                        icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.montagna);
                        break;
                    case 3:
                        icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.mare);
                        break;
                    case 4:
                        icon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.nougat);
                        break;
                }
                contentSmall.setImageViewResource(R.id.photo, R.mipmap.ic_camera);
                contentSmall.setTextViewText(R.id.text, "Immagine");
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(icon));
                }
            else {
                //invio testo
                contentSmall.setImageViewResource(R.id.photo, R.mipmap.ic_message);
                //imposto messaggi casuali
                String [] messaggi = mContext.getResources().getStringArray(R.array.messages);
                int k=new Random().nextInt(messaggi.length);
                builder.setContentText(messaggi[k]);
                contentSmall.setTextViewText(R.id.text, "Visualizza testo ...");
            }
            mNotifyManager.notify(idNot, builder.build());
    }

    @Override
    protected Void doInBackground(Integer... params) {
        //se scrivo un messaggio per la prima volta annullo il tempo di attesa per la ricezione della notifica
        if(!fTime) {
            try {
                Thread.sleep((new Random().nextInt(5) + 1) * 1000);
            } catch (InterruptedException e) {
                Log.e("InterruptedException", e.getMessage());
            }
        }
        sendMessage();
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
    }
}