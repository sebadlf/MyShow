package com.synergysolutions.myshow.app;

import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synergysolutions.myshow.app.Entity.Article;

import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;


public class ArticleView extends ActionBarActivity {

    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        Bundle extras = this.getIntent().getExtras();

        long id = extras.getLong("wikiaId");

        DatabaseHandler db = new DatabaseHandler(this);

        Article article = null;

        if (id != 0) {
            article = db.getArticle(id);
        } else {
            Uri data = getIntent().getData();

            if (data != null){

                String url = data.toString();

                url = url.replace("com.synergysolutions.myshow.article://", "");

                article = db.getArticle(url);
            }
        }

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.ArticleView);

        TextView textView = new TextView(this);

        textView.setText(article.getTitle());

        myLayout.addView(textView);

        descriptionTextView = new TextView(this);

        descriptionTextView.setText(article.getTeaser());

        myLayout.addView(descriptionTextView);

        ImageView imageView = new ImageView(this);

        String url = "http://img4.wikia.nocookie.net/__cb20130517163516/agentsofshield/images/1/11/SKYESeason1.jpg";

        myLayout.addView(imageView);

        new DownloadImageTask(imageView).execute(url);

        //////

        //Pattern userMatcher = Pattern.compile("\\B@[^:\\s]+");

        for(Article articleLink : db.getAllArticles()){
            if (article.getTitle().equals(articleLink.getTitle()) == false){
                Pattern userMatcher = Pattern.compile(articleLink.getTitle());

                String userViewURL = "com.synergysolutions.myshow.article://";

                Linkify.addLinks(descriptionTextView, userMatcher, userViewURL);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_view, menu);
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

}
