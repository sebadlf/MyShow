package com.synergysolutions.myshow.app;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by sebadlf on 31/03/14.
 */
public class ArticlesDetailsUpdaterAsyncTask extends AsyncTask<Void, Void, List<Article>> {

    private IArticlesDetailsLoadedResultProcessor articlesDetailsLoadedResultProcessor;

    public ArticlesDetailsUpdaterAsyncTask(IArticlesDetailsLoadedResultProcessor articlesDetailsLoadedResultProcessor) {
        this.articlesDetailsLoadedResultProcessor = articlesDetailsLoadedResultProcessor;
    }

    @Override
    protected List<Article> doInBackground(Void... params) {

        DatabaseHandler db = new DatabaseHandler((Context)this.articlesDetailsLoadedResultProcessor);

        List<Article> articleList = db.getAllArticlesWithoutDetails();

        return articleList;
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        this.articlesDetailsLoadedResultProcessor.OnArticlesDetailsLoadedResultFinish(result);
    }

}
