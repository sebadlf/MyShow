package com.synergysolutions.myshow.app;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sebadlf on 31/03/14.
 */
public class ArticlesLoaderAsyncTask extends AsyncTask<Void, Integer, List<Article>> {

    private Context context;
    private ArticlesAdapter articlesAdapter;

    public ArticlesLoaderAsyncTask(Context context, ArticlesAdapter articlesAdapter) {
        this.context = context;
        this.articlesAdapter = articlesAdapter;
    }

    @Override
    protected List<Article> doInBackground(Void... params) {

        DatabaseHandler db = new DatabaseHandler(this.context);

        return db.getAllArticles();
    }

    @Override
    protected void onPostExecute(List<Article> result) {

        articlesAdapter.updateList(result);
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
