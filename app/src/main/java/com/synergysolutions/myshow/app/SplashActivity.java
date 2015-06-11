package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.helper.Utils;

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
    private int articlesCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (new DatabaseHandler(this).getArticlesCount() > 0) {
            Intent mainIntent = new Intent().setClass(SplashActivity.this, SectionsActivity.class);
            startActivity(mainIntent);
            finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
        } else {
            int articleId = 1;
            boolean getOtherResource = true;

            while(getOtherResource){
                String resourceName = "articles" + articleId;

                int resourceId = getResources().getIdentifier(resourceName, "raw", getPackageName());;

                if (resourceId > 0){
                    try {
                        new ArticlesJsonAsyncTask(this).execute(Utils.readTextFile(this, resourceId));

                        articleId++;
                        articlesCounter++;
                    } catch (Exception e){
                        e.printStackTrace();
                        resourceId = -1;
                    }
                }

                getOtherResource = resourceId > 0;
            }
        }
    }

    @Override
    public void OnArticlesJsonResultFinish(List<Article> articleList) {

        final DatabaseHandler db = new DatabaseHandler(this);

        db.insertArticles(articleList);

        db.close();

        articlesCounter--;

        if (articlesCounter == 0){
            Intent mainIntent = new Intent().setClass(SplashActivity.this, SectionsActivity.class);
            startActivity(mainIntent);
            finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
        }
    }

}
