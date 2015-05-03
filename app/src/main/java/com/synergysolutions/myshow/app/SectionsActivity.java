package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.synergysolutions.myshow.app.Entity.Article;

import java.util.Random;
import java.util.StringTokenizer;

public class SectionsActivity extends Activity {

    protected String[] getStringArrayResourceFromTitle(String title) {
        int resourceId = getResources().getIdentifier(title.replace(" ", "").toLowerCase(), "array", this.getPackageName());

        String[] sections = new String[0];

        if (resourceId > 0){
            sections = getResources().getStringArray(resourceId);
        }

        return sections;
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    protected String getRandomThumbnailFromSectionTitle(String title) {
        String[] titles = getStringArrayResourceFromTitle(title);

        Article article = null;

        if (titles. length > 0){
            String relatedTitle = titles[randInt(0, titles.length - 1)];

            article = new DatabaseHandler(this).getArticle(relatedTitle, false);
        }

        return article != null ? article.getThumbnail() : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        Bundle extras = this.getIntent().getExtras();

        String title = null;

        if (extras != null) {
            title = extras.getString("section");
        }

        String[] sections;

        if ((title != null) && (title.length() > 0)){
            sections = getStringArrayResourceFromTitle(title);
        } else {
            sections = getResources().getStringArray(R.array.sections);
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Sections);

        for (final String section : sections){

            StringTokenizer stringTokenizer = new StringTokenizer(section, "|||");

            title = stringTokenizer.nextToken();
            String icon;
            if (stringTokenizer.hasMoreTokens()){
                icon = stringTokenizer.nextToken();
            }

            Article article = new DatabaseHandler(this).getArticle(title, false);

            if (article == null){
                article = new Article();
                article.setTitle(title);

                //Get Replacement Thumbnail
                article.setThumbnail(getRandomThumbnailFromSectionTitle(title));

            }

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.activity_demo_list_row, null);

            /////////////////////////////////////////////////

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


            ////////////////////////////////////////////////

            ImageView imgThombnail = (ImageView) rowView.findViewById(R.id.thumbnail);

            if ((article.getThumbnail() != null) && (article.getThumbnail().startsWith("http"))){
                //new DownloadImageTask(this,  imgThombnail).execute(article.getThumbnail());
                ImageLoader.getInstance().displayImage(article.getThumbnail(), imgThombnail, displayImageOptions);
            } else {
                imgThombnail.setImageResource(R.drawable.logo);
            }

            imgThombnail.setScaleType(ImageView.ScaleType.FIT_CENTER);

            TextView txtNombre = (TextView) rowView.findViewById(R.id.nombre);
            txtNombre.setText(article.getTitle()); // + " " + String.valueOf(article.getWikiaId()));

            TextView txtAlias = (TextView) rowView.findViewById(R.id.alias);
            txtAlias.setText(article.getTeaser());

            linearLayout.addView(rowView);

            final long finalId = article.getId();
            final String finalTitle = title;
            final String finalPackageName = this.getPackageName();

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = null;

                    if (getResources().getIdentifier(finalTitle.replace(" ", "").toLowerCase(), "array", finalPackageName) != 0){
                        intent = new Intent(SectionsActivity.this, SectionsActivity.class);
                        intent.putExtra("section", finalTitle);

                        startActivity(intent);
                    } else if (finalId != 0)  {
                        intent = new Intent(SectionsActivity.this, ArticleView.class);
                        intent.putExtra("title", finalTitle);

                        startActivity(intent);
                    } else {
                        Toast.makeText(SectionsActivity.this, "Comming Soon", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }
}
