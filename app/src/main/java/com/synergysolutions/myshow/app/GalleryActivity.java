package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.Section;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import java.util.ArrayList;

public class GalleryActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private StaggeredGalleryAdapter mAdapter;

    private ArrayList<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_grid);

        DatabaseHandler db = new DatabaseHandler(this);

        Bundle extras = this.getIntent().getExtras();

        long id = extras.getLong("articleId");

        Article article = db.getArticle(id);

        String title = article.getTitle() + " " + "Gallery";

        setTitle(title);
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        ArrayList<SectionImage> images = new ArrayList<SectionImage>();

        for (Section section : article.getSections()){
            for (SectionImage sectionImage : section.getSectionImages()){
                images.add(sectionImage);
            }
        }

        mAdapter = new StaggeredGalleryAdapter(this, android.R.layout.simple_list_item_1, images);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        /*
        final ArrayList<String> sampleData = generateData();
        for (String data : sampleData) {
            mAdapter.add(data);
        }
        // stash all the data in our backing store
        mData.addAll(sampleData);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
        */
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();

        //Intent intent = new Intent(GalleryActivity.this, FullScreenViewActivity.class);
        //startActivity(intent);
    }
}