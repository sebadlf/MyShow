package com.synergysolutions.myshow.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SectionImage sectionImage = (SectionImage)this.getItem(position);

        if (convertView == null) {
            convertView = new ImageView(this.context);
        }
        ImageView imageView = (ImageView) convertView;
        /*
        ImageView i =



        new DownloadImageTask(context,i).execute(sectionImage.getSrc());

        return i;
        */

        imageView.setImageResource(R.drawable.logo);
        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);



        ImageLoader.getInstance().displayImage(sectionImage.getSrc(), imageView);

        return convertView;
    }

    /*
    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub

        ImageView i = new ImageView(context);

        i.setImageResource(R.drawable.logo);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }
    */
}
