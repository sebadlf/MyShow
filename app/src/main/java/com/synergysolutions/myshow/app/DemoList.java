package com.synergysolutions.myshow.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.synergysolutions.myshow.app.Entity.Article;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;


public class DemoList extends ActionBarActivity implements IDownloadResultProcessor, IArticlesJsonResultProcessor, IArticlesLoadedResultProcessor {

    int REQUEST_CODE_ARTICLES = 1;

    ListView listView;

    List<Article> articlesWithoutDetails;

    DatabaseHandler databaseHandler;

    ArticlesAdapter articlesAdapter;

    private static final String URL = "http://myshow/app.php/articles";
    //private static final String URL_DETAILS = "http://agentsofshield.wikia.com/api/v1/Articles/Details?ids=%s&abstract=500&width=200&height=200";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);

        listView = (ListView) findViewById(R.id.characters_list);

        articlesAdapter = new ArticlesAdapter(this);
        listView.setAdapter(articlesAdapter);

        if (new DatabaseHandler(this).getArticlesCount() > 0) {

            new ArticlesLoaderAsyncTask(this).execute();

        } else {
            Toast.makeText(this, "Download Data", Toast.LENGTH_LONG).show();

            new DownloaderAsyncTask(REQUEST_CODE_ARTICLES, this).execute(URL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public DownloadResult OnDownloadFinish(DownloadResult downloadResult) {

        //Toast.makeText(this, downloadResult.getResultBody(), Toast.LENGTH_LONG).show();;

        if (downloadResult.getRequestCode() == REQUEST_CODE_ARTICLES) {

            Toast.makeText(this, "Parsing Data", Toast.LENGTH_LONG).show();

            new ArticlesJsonAsyncTaskAsyncTask(this).execute(downloadResult.getResultBody());

        }

        return null;
    }

    @Override
    public DownloadResult OnArticlesJsonResultFinish(List<Article> articleList) {

        Toast.makeText(this, "Data Inserts", Toast.LENGTH_LONG).show();

        databaseHandler = new DatabaseHandler(this);

        for (Article article : articleList) {
            databaseHandler.saveArticle(article);
        }

        Toast.makeText(this, "Data Inserts Done", Toast.LENGTH_LONG).show();

        new ArticlesLoaderAsyncTask(this).execute();

        return null;
    }

    @Override
    public void OnArticlesLoadedResultFinish(List<Article> result) {

        articlesAdapter.updateList(result);

        //new ArticlesDetailsUpdaterAsyncTask(this).execute();
    }
}