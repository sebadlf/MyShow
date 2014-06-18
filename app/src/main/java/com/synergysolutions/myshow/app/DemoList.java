package com.synergysolutions.myshow.app;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.synergysolutions.myshow.app.Entity.Article;

import java.util.List;


public class DemoList extends ActionBarActivity implements AdapterView.OnItemClickListener, IDownloadResultProcessor, IArticlesJsonResultProcessor, IArticlesLoadedResultProcessor {

    int REQUEST_CODE_ARTICLES = 1;

    ListView listView;

    DatabaseHandler databaseHandler;

    ArticlesAdapter articlesAdapter;

    private static final String URL = "http://myshow/app.php/articles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);

        listView = (ListView) findViewById(R.id.characters_list);

        articlesAdapter = new ArticlesAdapter(this);
        listView.setAdapter(articlesAdapter);

        listView.setOnItemClickListener(this);

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

            new ArticlesJsonAsyncTask(this).execute(downloadResult.getResultBody());
        }

        return null;
    }

    @Override
    public void OnArticlesJsonResultFinish(List<Article> articleList) {

        Toast.makeText(this, "Data Inserts", Toast.LENGTH_LONG).show();

        databaseHandler = new DatabaseHandler(this);

        for (Article article : articleList) {
            databaseHandler.saveArticle(article);
        }

        Toast.makeText(this, "Data Inserts Done", Toast.LENGTH_LONG).show();

        new ArticlesLoaderAsyncTask(this).execute();
    }

    @Override
    public void OnArticlesLoadedResultFinish(List<Article> result) {

        articlesAdapter.updateList(result);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArticleView.class);

        intent.putExtra("wikiaId", (int)id);

        startActivity(intent);
    }
}