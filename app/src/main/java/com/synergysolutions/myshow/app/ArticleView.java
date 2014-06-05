package com.synergysolutions.myshow.app;

import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.StartAppAd;
import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.LinkedArticle;
import com.synergysolutions.myshow.app.Entity.ListElement;
import com.synergysolutions.myshow.app.Entity.Section;
import com.synergysolutions.myshow.app.Entity.SectionContent;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class ArticleView extends ActionBarActivity {

    TextView descriptionTextView;

    DatabaseHandler db;

    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartAppAd.init(this, "104220702", "204133814");

        setContentView(R.layout.activity_article_view);

        Bundle extras = this.getIntent().getExtras();

        int id = extras.getInt("wikiaId");
        String title = extras.getString("title");

        db = new DatabaseHandler(this);

        article = null;

        if (id != 0) {
            article = db.getArticle(id);
        } else if ((title != null) && (title.length() > 0)) {
            article = db.getArticle(title, true);
        } else {
            Uri data = getIntent().getData();

            if (data != null){

                String url = data.toString();

                title = url.replace("com.synergysolutions.myshow.article://", "");

                article = db.getArticle(title, true);
            }
        }

        title = article.getTitle().trim();

        if(title.startsWith("\"") && title.endsWith("\"")){
            title = title.substring(1, title.length() - 3);
        }

        setTitle(title);

        LinearLayout myLayout = (LinearLayout) findViewById(R.id.ArticleSections);

        TextView textView = new TextView(this);

        myLayout.addView(textView);

        descriptionTextView = new TextView(this);

        descriptionTextView.setText(article.getTeaser());

        myLayout.addView(descriptionTextView);

        if ((article.getThumbnail() != null) && (article.getThumbnail().startsWith("http"))) {

            ImageView imageView = new ImageView(this);

            String url = article.getThumbnail();

            myLayout.addView(imageView);

            new DownloadImageTask(this, null).execute(url);

        }

        //////
        for(Section section : article.getSections()){
            this.drawSection(myLayout, section);
        }

        //this.linkifyTextView(descriptionTextView);

    }

    private void drawSection(LinearLayout myLayout, Section section) {

        TextView textView = new TextView(this);

        textView.setText(section.getTitle() + " (" + section.getLevel() + ")");

        textView.setTypeface(null, Typeface.BOLD);

        myLayout.addView(textView);

        //this.linkifyTextView(textView);

        for (SectionContent sectionContent : section.getSectionContents()){

            this.drawSectionContent(myLayout, sectionContent);

        }

        /*
        for(SectionImage sectionImage : section.getSectionImages()){
            this.drawSectionImage(myLayout, sectionImage);
        }
        */

        /*
        if (section.getSectionImages().size() > 0){

            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            horizontalScrollView.setBackgroundColor(getResources().getColor(android.R.color.black));

            horizontalScrollView.addView(linearLayout);

            for(SectionImage sectionImage : section.getSectionImages()){

                ImageView i = new ImageView(this);

                i.setImageResource(R.drawable.logo);

                i.setMaxHeight(200);
                i.setMaxWidth(200);

                i.setScaleType(ImageView.ScaleType.CENTER_CROP);

                new DownloadImageTask(this, i).execute(sectionImage.getSrc());

                linearLayout.addView(i);
            }

            horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle HorizontalScrollView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });


            myLayout.addView(horizontalScrollView);
        }
        */

        if (section.getSectionImages().size() > 0) {
            Gallery gallery = new Gallery(this);

            GalleryAdapter galleryAdapter = new GalleryAdapter(this, section.getSectionImages());

            gallery.setSpacing(2);

            gallery.setAdapter(galleryAdapter);

            if (section.getSectionImages().size() > 1) {
                gallery.setSelection(1);
            }

            myLayout.addView(gallery);
        }
    }

    private void drawSectionContent(LinearLayout myLayout, SectionContent sectionContent) {

        if (sectionContent.getType().equals("paragraph")){
            this.drawSectionContentParagraph(myLayout, sectionContent);
        } else if (sectionContent.getType().equals("list")){
            this.drawSectionContentList(myLayout, sectionContent);
        } else {
            Toast.makeText(this, "Invalid Section Content Type " + sectionContent.getType() ,Toast.LENGTH_LONG);
        }


    }

    private void drawSectionContentList(LinearLayout myLayout, SectionContent sectionContent) {

        for (ListElement listElement : sectionContent.getListElements()){

            TextView textView = new TextView(this);

            textView.setText(listElement.getText());

            myLayout.addView(textView);

            this.linkifyTextView(textView, listElement.getLinkedArticles());

        }


        int rId = getResources().getIdentifier("logo", "drawable", this.getApplicationContext().getPackageName());

        int a = 1;
    }

    private void drawSectionImage(LinearLayout myLayout, SectionImage sectionImage) {
            /*

            ImageView imageView = new ImageView(this);

            String url = sectionImage.getSrc();

            myLayout.addView(imageView);

            new DownloadImageTask(this.getApplicationContext(), imageView).execute(url);

            if ((sectionImage.getCaption() != null) && (sectionImage.getCaption().length() > 0)){
                TextView textView = new TextView(this);

                textView.setText(sectionImage.getCaption());

                myLayout.addView(textView);
            }

            */

    }

    private void drawSectionContentParagraph(LinearLayout myLayout, SectionContent sectionContent) {
        TextView textView = new TextView(this);

        textView.setText(sectionContent.getText());

        myLayout.addView(textView);

        this.linkifyTextView(textView, sectionContent.getLinkedArticles());
    }

    private void linkifyTextView(TextView textView, List<LinkedArticle> linkedArticles){

        for(LinkedArticle linkedArticle : linkedArticles){
            //Pattern userMatcher = Pattern.compile("\\B@[^:\\s]+");
            Pattern userMatcher = Pattern.compile(linkedArticle.getAlias());

            String userViewURL = "com.synergysolutions.myshow.article://";

            Linkify.addLinks(textView, userMatcher, userViewURL);

            this.stripUnderlines(textView);
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

    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);

            ds.setFakeBoldText(true);
            ds.setColor(Color.BLACK);
        }
    }

    private void stripUnderlines(TextView textView) {
        Spannable s = (Spannable)textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }
}
