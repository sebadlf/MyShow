package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synergysolutions.myshow.app.Entity.Article;

import org.w3c.dom.Text;

import java.util.StringTokenizer;

public class SectionsActivity extends Activity {

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
            int resourceId = getResources().getIdentifier(title.toLowerCase(), "array", this.getPackageName());

            sections = getResources().getStringArray(resourceId);
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

            /*
            TextView textView = new TextView(this);

            textView.setText(title);

            linearLayout.addView(textView);
            */

            Article article = new DatabaseHandler(this).getArticle(title, false);

            if (article == null){
                article = new Article();
                article.setTitle(title);
            }

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.activity_demo_list_row, null);

            ImageView imgThombnail = (ImageView) rowView.findViewById(R.id.thumbnail);

            imgThombnail.setImageResource(R.drawable.logo);

            if ((article.getThumbnail() != null) && (article.getThumbnail().startsWith("http"))){
                new DownloadImageTask(this,  imgThombnail).execute(article.getThumbnail());
            }

            TextView txtNombre = (TextView) rowView.findViewById(R.id.nombre);
            txtNombre.setText(article.getTitle());

            TextView txtAlias = (TextView) rowView.findViewById(R.id.alias);
            txtAlias.setText(article.getTeaser());

            linearLayout.addView(rowView);


            final String finalTitle = title;
            final String finalPackageName = this.getPackageName();

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = null;

                    if (getResources().getIdentifier(finalTitle.toLowerCase(), "array", finalPackageName) != 0){
                        intent = new Intent(SectionsActivity.this, SectionsActivity.class);
                        intent.putExtra("section", finalTitle);
                    } else {
                        intent = new Intent(SectionsActivity.this, ArticleView.class);
                        intent.putExtra("title", finalTitle);
                    }

                    startActivity(intent);
                }
            });

        }
    }
}
