package com.synergysolutions.myshow.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by sebadlf on 24/04/14.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String LOG_TAG = "DownloadImageTask";
    private final Context context;
    ImageView bmImage;

    public DownloadImageTask(Context context, ImageView bmImage) {
        this.context = context;
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            File file = this.getImageFile(context, urldisplay);

            if (file.exists()){

                mIcon11 = BitmapFactory.decodeStream(new FileInputStream(file));

            } else {

                if (!file.mkdirs()) {
                    Log.e(LOG_TAG, "Directory not created");
                }

                InputStream inputStream = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(inputStream);

                if (this.isExternalStorageWritable()) {

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, read);
                    }

                    inputStream.close();
                    fileOutputStream.close();
                }

            }

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

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getImageFile(Context context, String photoUrl) {

        photoUrl = photoUrl.replace("/", "-").replace(":", "-").replace(".", "-").replace("%", "-");

        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), photoUrl + ".png");

        boolean ex = file.exists();

        return file;
    }

}