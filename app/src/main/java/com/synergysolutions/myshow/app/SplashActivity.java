package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    private long splashDelay = 6000; //6 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, splashDelay);//Pasado los 6 segundos dispara la tarea
    }
}
