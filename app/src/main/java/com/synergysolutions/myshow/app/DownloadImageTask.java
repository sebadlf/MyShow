package com.synergysolutions.myshow.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String LOG_TAG = "DownloadImageTask";
    private final Context context;
    WeakReference<ImageView> bmImage;

    public DownloadImageTask(Context context, ImageView bmImage) {
        this.context = context;
        this.bmImage = bmImage != null ? new WeakReference<ImageView>(bmImage) : null;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            File file = this.getImageFile(context, urldisplay);

            if (file.exists()) {

                mIcon11 = BitmapFactory.decodeStream(new FileInputStream(file));

            } else {

                InputStream inputStream = new java.net.URL(urldisplay).openStream();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;

                if (this.isExternalStorageWritable()) {

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    while ((len = inputStream.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                        fileOutputStream.write(buffer, 0, len);
                    }

                    fileOutputStream.close();

                } else {
                    while ((len = inputStream.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                }

                baos.flush();

                mIcon11 = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));

            }

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        if (bmImage != null) {
            ImageView imageView = bmImage.get();

            if (imageView != null) {
                imageView.setImageBitmap(result);
            }
        } else {
            BitmapDrawable bd = new BitmapDrawable(context.getResources(), result);

            ((ActionBarActivity)context).getActionBar().setIcon(bd);
        }

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

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

        String fileName = photoUrl.substring(photoUrl.lastIndexOf('/') + 1, photoUrl.length());

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        return file;
    }

}