package com.synergysolutions.myshow.app;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by sebadlf on 31/03/14.
 */
public class ArticlesGetWithoutDetailsLoaderAsyncTask extends AsyncTask<Void, Integer, List<Article>> {

    private IArticlesDetailsLoadedResultProcessor articlesDetailsLoadedResultProcessor;

    public ArticlesGetWithoutDetailsLoaderAsyncTask(IArticlesDetailsLoadedResultProcessor articlesDetailsLoadedResultProcessor) {
        this.articlesDetailsLoadedResultProcessor = articlesDetailsLoadedResultProcessor;
    }

    @Override
    protected List<Article> doInBackground(Void... params) {

        DatabaseHandler db = new DatabaseHandler((Context)this.articlesDetailsLoadedResultProcessor);

        return db.getAllArticlesWithoutDetails();
    }

    @Override
    protected void onPostExecute(List<Article> result) {

        this.articlesDetailsLoadedResultProcessor.OnArticlesDetailsLoadedResultFinish(result);

    }
}
