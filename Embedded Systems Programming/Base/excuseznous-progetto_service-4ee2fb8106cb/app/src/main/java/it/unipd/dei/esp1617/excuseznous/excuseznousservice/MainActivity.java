package it.unipd.dei.esp1617.excuseznous.excuseznousservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static Button btnplay;
    static Button btnpause;
    static Button btncancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("Metodo", "onCREATE");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
        startActivity(setting);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("Metodo", "onRESUME");

        btnplay = (Button) findViewById(R.id.play);
        btnpause = (Button) findViewById(R.id.pause);
        btncancel = (Button) findViewById(R.id.cancel);
        
        //setto lo stato dei bottoni in base allo stato in cui si trova il service
        if (NotificationService.Status.equals(Constants.STATUS.START)) {
            btnplay.setEnabled(true);
            btnpause.setEnabled(false);
            btncancel.setEnabled(false);
        }
        if (NotificationService.Status.equals(Constants.STATUS.PLAY)) {
            btnplay.setEnabled(false);
            btnpause.setEnabled(true);
            btncancel.setEnabled(true);
        }
        if (NotificationService.Status.equals(Constants.STATUS.REPLAY)) {
            btnplay.setEnabled(true);
            btnpause.setEnabled(false);
            btncancel.setEnabled(true);
        }

        btnplay.setOnClickListener(new View.OnClickListener() {
            //disabilitare bottone play dopo un click e abilitare pausa e stop
            @Override
            public void onClick(View v) {
                if (btnplay.isEnabled()) {
                    //se il bottone è abilitato passo al service l'informazione della pressione del tasto play
                    btnplay.setEnabled(false);
                    btnpause.setEnabled(true);
                    btncancel.setEnabled(true);
                    Intent service = new Intent(MainActivity.this, NotificationService.class);
                    service.setAction(Constants.ACTION.PLAY_ACTION);
                    NotificationService.IS_SERVICE_RUNNING = true;
                    startService(service);
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            //disabilitare bottoni pausa e stop e abilitare bottone play
            @Override
            public void onClick(View v) {
                if(btncancel.isEnabled()) {
                    //se il bottone è abilitato passo al service l'informazione della pressione del tasto stop
                    btnplay.setEnabled(true);
                    btnpause.setEnabled(false);
                    btncancel.setEnabled(false);
                    Intent service = new Intent(MainActivity.this, NotificationService.class);
                    service.setAction(Constants.ACTION.CANCEL_ACTION);
                    NotificationService.IS_SERVICE_RUNNING = true;
                    startService(service);

                }
            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnpause.isEnabled()) {
                    //se il bottone è abilitato passo al service l'informazione della pressione del tasto pause
                    btnplay.setEnabled(true);
                    btnpause.setEnabled(false);
                    btncancel.setEnabled(true);
                    Intent service = new Intent(MainActivity.this, NotificationService.class);
                    service.setAction(Constants.ACTION.PAUSE_ACTION);
                    NotificationService.IS_SERVICE_RUNNING = true;
                    startService(service);
                }
            }
        });

    }
}
