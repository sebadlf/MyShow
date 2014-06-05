package com.synergysolutions.myshow.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import java.util.Collections;
import java.util.List;

/**
 * Created by sebadlf on 30/03/14.
 */
public class GalleryAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<SectionImage> urlList = Collections.EMPTY_LIST;

    private Context context;

    public GalleryAdapter(Context context, List<SectionImage> urlList) {
        this.context = context;

        this.urlList = urlList;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public Object getItem(int position) {
        return urlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((SectionImage) this.getItem(position)).getId();
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if (convertView == null) {
        //    convertView = inflater.inflate(R.layout.activity_demo_list_row, null);
        //}


        SectionImage sectionImage = (SectionImage)this.getItem(position);

        ImageView i = new ImageView(this.context);

        i.setImageResource(R.drawable.logo);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.CENTER_CROP);

        new DownloadImageTask(context,  i).execute(sectionImage.getSrc());

        return i;
    }
    */

    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub

        ImageView i = new ImageView(context);

        i.setImageResource(R.drawable.logo);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }
}
