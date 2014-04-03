package com.synergysolutions.myshow.app;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public class ArticlesJsonAsyncTaskAsyncTask extends AsyncTask<String, Integer, List<Article>> {

    private static final String TAG = "HttpGetTask";

    IArticlesJsonResultProcessor articlesJsonResultProcessor;

    public ArticlesJsonAsyncTaskAsyncTask(IArticlesJsonResultProcessor articlesJsonResultProcessor) {
        this.articlesJsonResultProcessor = articlesJsonResultProcessor;
    }

    protected List<Article> doInBackground(String... jsonString) {

        List<Article> result = new ArrayList<Article>();

        try {
            JSONObject responseObject = (JSONObject) new JSONTokener(jsonString[0]).nextValue();

            JSONArray items = responseObject.getJSONArray("items");

            for (int idx = 0; idx < items.length(); idx++) {

                JSONObject item = (JSONObject) items.get(idx);

                Article article = new Article();
                article.setWikiaId(item.getInt("id"));
                article.setTitle(item.getString("title"));
                article.setUrl(item.getString("url"));
                result.add(article);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        this.articlesJsonResultProcessor.OnArticlesJsonResultFinish(result);
    }
}
