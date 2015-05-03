package com.synergysolutions.myshow.app;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.synergysolutions.myshow.app.Entity.SectionImage;

public class StaggeredGalleryAdapter extends ArrayAdapter<SectionImage> {

    private static final String TAG = "SampleAdapter";

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public StaggeredGalleryAdapter(Context context,
                                   int textViewResourceId,
                                   ArrayList<SectionImage> objects) {

        super(context, textViewResourceId, objects);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.gallery_row,
                    parent, false);
            vh = new ViewHolder();
            vh.imgView = (DynamicHeightImageView) convertView
                    .findViewById(R.id.imgView);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        vh.imgView.setHeightRatio(positionHeight);

        /////////////////////////////////////////////////

        final float densityDpi =  getContext().getResources().getDisplayMetrics().densityDpi;
        final int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        final int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        final int maxPixels = widthPixels > heightPixels ? widthPixels : heightPixels;
        final float maxSize = maxPixels / densityDpi;

        DisplayImageOptions displayImageOptions = new DisplayImageOptions
                .Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .preProcessor(new BitmapProcessor() {
                    @Override
                    public Bitmap process(Bitmap bitmap) {

                        Bitmap result = null;

                        if (bitmap.getHeight() > bitmap.getWidth()){
                            int width = bitmap.getWidth();
                            int x = (int) ((bitmap.getHeight() - width) * 0.3);

                            result = Bitmap.createBitmap(bitmap, 0, 0, width, width);
                        } else {
                            int height = bitmap.getHeight();
                            int y = (int) ((bitmap.getWidth() - height) * 0.5);

                            result = Bitmap.createBitmap(bitmap, y, 0, height, height);
                        }

                        return Bitmap.createScaledBitmap(result, (int)densityDpi, (int)densityDpi, true);
                    }
                })
                .build();

        //////////////////////////////////////////////


        ImageLoader.getInstance().displayImage(getItem(position).getSrc(), vh.imgView, displayImageOptions);

        return convertView;
    }

    static class ViewHolder {
        DynamicHeightImageView imgView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }
}