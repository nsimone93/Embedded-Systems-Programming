package it.unipd.dei.esp1617.excuseznous.excuseznousservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //dichiaro variabili design
        //final TextView interval = (TextView) findViewById(R.id.interval);
        final SeekBar step = (SeekBar) findViewById(R.id.step);
        final Switch enablelimit= (Switch) findViewById(R.id.enablelimit);
        final EditText numbermessages = (EditText) findViewById(R.id.numbermessages);
        final TextView number = (TextView) findViewById(R.id.number);
        final Button confirm = (Button) findViewById(R.id.buttonconfirm);
        final TextView value = (TextView) findViewById(R.id.valueint);
        final Switch vibration = (Switch) findViewById(R.id.vib);
        final Switch ringtone = (Switch) findViewById(R.id.sound);

        //recupero preferenze salvate in modo persistente
        SharedPreferences preferences = getSharedPreferences("sharedpref",MODE_PRIVATE);
        int intervallo = preferences.getInt("intervallo",5);
        int maxmex = preferences.getInt("maxmex", -1);
        vibration.setChecked(preferences.getBoolean("vibration",false));
        ringtone.setChecked(preferences.getBoolean("ringtone",false));
        step.setProgress(intervallo-5);
        value.setText(Integer.toString(intervallo));
        if(maxmex != -1) {
            numbermessages.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
            enablelimit.setChecked(true);
            numbermessages.setText(Integer.toString(maxmex));
        }

        //imposto funzione switch
        enablelimit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    numbermessages.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                } else {
                    numbermessages.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                }
            }
        });

        //imposto funzione seekbar
        step.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress += 5;
                value.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        numbermessages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = numbermessages.getText().length();
                if(length > 10) {
                    //controllo che il numero inserito possa essere contenuto in un int
                    Toast.makeText(getApplicationContext(), "Choose a smaller number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //imposto funzione bottone conferma
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salviamo variabili in modo persistente
                SharedPreferences preferences = getSharedPreferences("sharedpref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                int maxmex = -1;
                Intent parametri = new Intent(getApplicationContext(), MainActivity.class);

                int interval = Integer.parseInt(value.getText().toString());
                editor.putInt("intervallo", interval);
                editor.putBoolean("vibration", vibration.isChecked());
                editor.putBoolean("ringtone", ringtone.isChecked());

                if (numbermessages.getVisibility() == View.VISIBLE) {
                    if ((numbermessages.getText().toString().equals(""))||(numbermessages.getText().length()>10)) {
                        Toast.makeText(getApplicationContext(), "Invalid Number Messages Input", Toast.LENGTH_SHORT).show();
                    } else {
                        maxmex = Integer.parseInt(numbermessages.getText().toString());
                        parametri.putExtra("maxmex", maxmex);
                        editor.putInt("maxmex", maxmex);
                        editor.apply();
                        finish();
                    }
                } else {
                    parametri.putExtra("maxmex", maxmex);
                    editor.putInt("maxmex", maxmex);
                    editor.commit();
                    finish();
                }

                if(NotificationService.IS_SERVICE_RUNNING) {
                    //metto in pausa perchè non possano verificarsi situazioni conflittuali nel cambio delle impostazioni
                    Intent service = new Intent(getApplicationContext(), NotificationService.class);
                    service.setAction(Constants.ACTION.PAUSE_ACTION);
                    startService(service);
                }

                //aggiorno le variabili della classe NotificationService per l'invio delle notifiche
                NotificationService.maxmex=maxmex;
                NotificationService.interval=interval;
                NotificationService.ringtone=ringtone.isChecked();
                NotificationService.vibration=vibration.isChecked();
            }
        });

    }

}