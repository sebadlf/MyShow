package com.synergysolutions.myshow.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by sebadlf on 24/04/14.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);

        bmImage.setBackgroundColor(Color.RED);

            /*
            int lines = (int)Math.round(bmImage.getDrawable().getIntrinsicHeight() / descriptionTextView.getLineHeight());

            String text = descriptionTextView.getText().toString();

            // Get the icon and its width
            Drawable DICON = bmImage.getDrawable();
            int leftMargin = DICON.getIntrinsicWidth() + 10 ;

            //Set the icon in R.id.icon
            //ImageView icon =(ImageView)findViewById(R.id.icon) ;
            bmImage.setBackgroundDrawable (DICON) ;

            SpannableString SS = new SpannableString (text);
            //Expose the indent for the first three rows
            SS.setSpan(new MyLeadingMarginSpan2(lines,leftMargin),0,SS.length(),0);

            //TextView MessageView = ( TextView ) findViewById ( R.id.message_view) ;
            //MessageView. setText ( SS ) ;

            descriptionTextView.setText(SS);


            RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.ArticleView);
            myLayout.invalidate();
            */
    }
}