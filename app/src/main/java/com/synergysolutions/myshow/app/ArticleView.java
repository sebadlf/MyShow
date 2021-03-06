package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.startapp.android.publish.StartAppAd;
import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.LinkedArticle;
import com.synergysolutions.myshow.app.Entity.ListElement;
import com.synergysolutions.myshow.app.Entity.Section;
import com.synergysolutions.myshow.app.Entity.SectionContent;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class ArticleView extends ActionBarActivity {

    DatabaseHandler db;

    Article article;
    ArrayList<String> sections = new ArrayList();
    LinearLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String startappAccountId = getResources().getString(R.string.startapp_accountid);
        String startappAppId = getResources().getString(R.string.startapp_appid);

        StartAppAd.init(this, startappAccountId, startappAppId);

        String[] sectionsArray = getResources().getStringArray(R.array.sections);
        for (int i = 0; i < sectionsArray.length; i++) {
            StringTokenizer stringTokenizer = new StringTokenizer(sectionsArray[i], "|||");

            String title = stringTokenizer.nextToken();

            sections.add(title);
        }

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

                title = url.replace(getResources().getString(R.string.app_schema) + "://", "");

                article = db.getArticle(title, true);
            }
        }

        setTitle(article.getTitle().trim());

        myLayout = (LinearLayout) findViewById(R.id.ArticleSections);

        if ((article.getThumbnail() != null) && (article.getThumbnail().startsWith("http"))) {

            ImageView imageView = new ImageView(this);

            String url = article.getThumbnail();

            myLayout.addView(imageView);

            new DownloadImageTask(this, null).execute(url);
        }

//        TextView textView = new TextView(this);
//        textView.setText(String.valueOf(article.getWikiaId()));
//        myLayout.addView(textView);

        for(Section section : article.getSections()){
            this.drawSection(myLayout, section);
        }

        //Section[] articleSectionsArray = new Section[article.getSections().size()];
        //articleSectionsArray = article.getSections().toArray(articleSectionsArray);
        //new DrawSectionAsync().execute(articleSectionsArray);
    }

    private class DrawSectionAsync extends AsyncTask<Section, LinearLayout, Long> {
        protected Long doInBackground(Section... sections) {
            int count = sections.length;

            for (int i = 0; i < count; i++) {
                LinearLayout linearLayout = new LinearLayout(ArticleView.this);

                ArticleView.this.drawSection(linearLayout, sections[i]);

                publishProgress(linearLayout);
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return 0L;
        }

        protected void onProgressUpdate(LinearLayout... progress) {
            //setProgressPercent(progress[0]);
            for (int i = 0; i < progress[0].getChildCount(); i++){
                View view = progress[0].getChildAt(0);

                progress[0].removeViewAt(0);

                myLayout.addView(view);
            }
        }

        protected void onPostExecute(Long result) {

        }
    }

    private String stripSlayesOld(String text){

        text = text.trim();

        StringTokenizer st = new StringTokenizer(text, "\n");
        StringBuilder sb = new StringBuilder();

        while (st.hasMoreTokens()){
            String token = st.nextToken();

            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = "\"" + token.substring(1, token.length() - 2).trim() + "\"";
            }

            sb.append(token);
            sb.append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }

    private void drawSection(LinearLayout myLayout, Section section) {

        TextView textView = new TextView(this);

        textView.setText(section.getTitle());// + " (" + section.getLevel() + ")");

        textView.setTypeface(null, Typeface.BOLD);

        if(section.getLevel() == 1){
            textView.setTextAppearance(getApplicationContext(), R.style.level1);
            textView.setPadding(0,0,0,5);
        } else if(section.getLevel() == 2){
            textView.setTextAppearance(getApplicationContext(), R.style.level2);
            textView.setPadding(0,5,0,5);
        } else if(section.getLevel() == 3){
            textView.setTextAppearance(getApplicationContext(), R.style.level3);
            textView.setPadding(0,5,0,5);
        } else if(section.getLevel() == 4){
            textView.setTextAppearance(getApplicationContext(), R.style.level4);
            textView.setPadding(0,5,0,5);
        } else {
            textView.setTextAppearance(getApplicationContext(), R.style.level5);
            textView.setPadding(0,5,0,5);
        }

        if (section.getLevel() > 1){
            myLayout.addView(textView);
        }

        //this.linkifyTextView(textView);

        for (SectionContent sectionContent : section.getSectionContents()){

            this.drawSectionContent(myLayout, sectionContent);

        }


        if (section.getSectionImages().size() > 0) {

            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);

            TableLayout tableLayout = new TableLayout(this);

            //tableLayout.setLayoutParams(tableParams);

            myLayout.addView(tableLayout);

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(tableParams);

            tableLayout.addView(tableRow);

            final float densityDpi = getResources().getDisplayMetrics().densityDpi;
            final int widthPixels = getResources().getDisplayMetrics().widthPixels;
            final int heightPixels = getResources().getDisplayMetrics().heightPixels;
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

            //double factor =  1 + maxSize * (20 / 3);

            int max = (int) (getResources().getDisplayMetrics().widthPixels / densityDpi * 1.5) ;

            int cant = section.getSectionImages().size() > max ? max : section.getSectionImages().size();

            for (int i = 0; i < cant; i++){

                SectionImage sectionImage = section.getSectionImages().get(i);

                ImageView imageView = new ImageView(this);

                imageView.setImageResource(R.drawable.logo);

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                ImageLoader.getInstance().displayImage(sectionImage.getSrc(), imageView, displayImageOptions);

                imageView.setLayoutParams(rowParams);

                tableRow.addView(imageView);


                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent().setClass(ArticleView.this, GalleryActivity.class);

                        intent.putExtra("articleId", article.getId());

                        startActivity(intent);
                    }
                });


            }

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

            /*
            Gallery gallery = new Gallery(this);

            GalleryAdapter galleryAdapter = new GalleryAdapter(this, section.getSectionImages());

            gallery.setSpacing(2);

            gallery.setAdapter(galleryAdapter);

            if (section.getSectionImages().size() > 1) {
                gallery.setSelection(1);
            }

            final Activity activity = this;

            gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(activity, ((SectionImage)parent.getItemAtPosition(position)).getSrc(), Toast.LENGTH_SHORT);
                }
            });

            myLayout.addView(gallery);

           */


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

            String userViewURL = getResources().getString(R.string.app_schema) + "://";

            Linkify.addLinks(textView, userMatcher, userViewURL);

            this.stripUnderlines(textView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_view, menu);

        MenuItem menuItem = menu.add(0, 1, 1, R.string.home_label);

        for (int i = 0; i < sections.size(); i++) {
            menuItem = menu.add(0, 1, 1, sections.get(i));
        }

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
        } else if(item.getTitle() == getResources().getString(R.string.home_label)) {
            Intent mainIntent = new Intent().setClass(ArticleView.this, SectionsActivity.class);
            startActivity(mainIntent);
        } else if (sections.contains(item.getTitle().toString())){
            Intent intent = new Intent(ArticleView.this, SectionsActivity.class);
            intent.putExtra("section", item.getTitle().toString());

            startActivity(intent);
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
