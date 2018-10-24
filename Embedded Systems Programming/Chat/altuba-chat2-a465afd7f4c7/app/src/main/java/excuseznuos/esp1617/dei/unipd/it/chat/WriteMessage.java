package excuseznuos.esp1617.dei.unipd.it.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WriteMessage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_message);
        //variabili
        TextView dst = (TextView) findViewById(R.id.destinatario);
        ImageView icon = (ImageView) findViewById(R.id.image);
        final Button invia = (Button) findViewById(R.id.invia);
        final Button annulla = (Button) findViewById(R.id.annulla);
        final EditText messaggio = (EditText) findViewById(R.id.messaggio);
        final String dest = getIntent().getExtras().getString("Destinatario");
        final int id_pic = getIntent().getExtras().getInt("id_pic");
        dst.setText(dest);
        icon.setImageResource(id_pic);
        //gestisco il bottone invia
        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = messaggio.getText().length();
                if(length < 1) {
                    Toast.makeText(getApplicationContext(), "Inserisci testo", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent replyintent = new Intent(getApplicationContext(), MessagingService.class);
                    replyintent.setAction(MessagingService.MSG_SEND_NOTIFICATION);
                    replyintent.putExtra("member", dest);
                    startService(replyintent);
                    finish();
                }
            }
        });
        //gestisco il bottone annulla
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
