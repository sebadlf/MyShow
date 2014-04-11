package com.synergysolutions.myshow.app;

import android.content.Context;
import android.os.AsyncTask;

import com.synergysolutions.myshow.app.Entity.Article;

import java.util.List;

/**
 * Created by sebadlf on 31/03/14.
 */
public class ArticlesLoaderAsyncTask extends AsyncTask<Void, Integer, List<Article>> {

    private IArticlesLoadedResultProcessor articlesLoadedResultProcessor;

    public ArticlesLoaderAsyncTask(IArticlesLoadedResultProcessor articlesLoadedResultProcessor) {
        this.articlesLoadedResultProcessor = articlesLoadedResultProcessor;
    }

    @Override
    protected List<Article> doInBackground(Void... params) {

        DatabaseHandler db = new DatabaseHandler((Context)this.articlesLoadedResultProcessor);

        return db.getAllArticles();
    }

    @Override
    protected void onPostExecute(List<Article> result) {

        this.articlesLoadedResultProcessor.OnArticlesLoadedResultFinish(result);

    }

    /*

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();

        pDialog.setProgress(progreso);
    }

    @Override
    protected void onPreExecute() {

        pDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MiTareaAsincronaDialog.this.cancel(true);
            }
        });

        pDialog.setProgress(0);
        pDialog.show();
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(MainHilos.this, "Tarea cancelada!",
                Toast.LENGTH_SHORT).show();
    }

    */
}
