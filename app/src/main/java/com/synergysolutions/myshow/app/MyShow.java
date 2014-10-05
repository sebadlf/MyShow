package com.synergysolutions.myshow.app;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by s.delafuente on 9/14/14.
 */
public class MyShow extends Application {

    @Override
    public void onCreate(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

}
