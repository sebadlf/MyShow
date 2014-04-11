package com.synergysolutions.myshow.app;

import android.os.AsyncTask;

import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.Section;

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
            JSONArray articlesJson = (JSONArray) new JSONTokener(jsonString[0]).nextValue();

            for (int articleId = 0; articleId < articlesJson.length(); articleId++) {

                JSONObject articleJson = (JSONObject) articlesJson.get(articleId);

                Article article = new Article();
                article.setWikiaId(articleJson.getInt("wikiaId"));
                article.setTitle(articleJson.getString("title"));
                article.setUrl(articleJson.getString("url"));
                article.setTeaser(articleJson.getString("teaser"));

                JSONArray sectionsJson = articleJson.getJSONArray("sections");

                for (int sectionId = 0; sectionId < sectionsJson.length(); sectionId++) {

                    JSONObject sectionJson = (JSONObject) sectionsJson.get(sectionId);

                    Section section = new Section();
                    section.setArticle(article);
                    section.setLevel(sectionJson.getInt("level"));
                    section.setTitle(sectionJson.getString("title"));

                    article.getSections().add(section);
                }

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
