package com.synergysolutions.myshow.app;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sebadlf on 31/03/14.
 */
public class CharactersLoaderAsyncTask extends AsyncTask<Void, Integer, List<Character>> {

    private Context context;
    private CharactersAdapter charactersAdapter;

    public CharactersLoaderAsyncTask(Context context, CharactersAdapter charactersAdapter) {
        this.context = context;
        this.charactersAdapter = charactersAdapter;
    }

    @Override
    protected List<Character> doInBackground(Void... params) {

        DatabaseHandler db = new DatabaseHandler(this.context);

        return db.getAllCharacters();
    }

    @Override
    protected void onPostExecute(List<Character> result) {

        charactersAdapter.updateList(result);
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
