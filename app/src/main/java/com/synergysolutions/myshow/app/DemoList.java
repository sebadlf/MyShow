package com.synergysolutions.myshow.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class DemoList extends ActionBarActivity implements IDownloadResultProcessor {

    ListView listView;

    DatabaseHandler databaseHandler;

    CharactersAdapter charactersAdapter;

    private static final String USER_NAME = "aporter";
    private static final String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
            + USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);

        listView = (ListView) findViewById(R.id.characters_list);

        databaseHandler = new DatabaseHandler(this);

        databaseHandler.addCharacter(new Character(1, "Pablo", "PPP"));

        charactersAdapter = new CharactersAdapter(this);
        listView.setAdapter(charactersAdapter);

        //new CharactersLoaderAsyncTask(this, this.charactersAdapter).execute();

        new DownloaderAsyncTask(this).execute(URL);
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

        Toast.makeText(this, downloadResult.getResultBody(), Toast.LENGTH_LONG).show();;

        return null;
    }
}
