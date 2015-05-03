package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.synergysolutions.myshow.app.Entity.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Handler;

public class SplashActivity extends Activity implements IArticlesJsonResultProcessor {

    private long splashDelay = 6000; //6 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (new DatabaseHandler(this).getArticlesCount() > 0) {
            Intent mainIntent = new Intent().setClass(SplashActivity.this, SectionsActivity.class);
            startActivity(mainIntent);
            finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
        } else {
            new ArticlesJsonAsyncTask(this).execute(this.readTextFile(this, R.raw.articles));
        }

    }

    private String readTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return stringBuilder.toString();
    }

    @Override
    public void OnArticlesJsonResultFinish(List<Article> articleList) {

        Date start = new Date();

        final DatabaseHandler db = new DatabaseHandler(this);

        db.insertArticles(articleList);

        db.close();

        long diff = new Date().getTime() - start.getTime();

        Log.println(Log.DEBUG, SplashActivity.class.getName(), "Tiempo = " + diff);

        Intent mainIntent = new Intent().setClass(SplashActivity.this, SectionsActivity.class);
        startActivity(mainIntent);
        finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
    }

}
