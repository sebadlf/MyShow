package com.synergysolutions.myshow.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SectionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        Bundle extras = this.getIntent().getExtras();
        String title = extras.getString("title");


        String[] sections;

        if ((title != null) && (title.length() > 0)){
            int resourceId = getResources().getIdentifier(title, "values", this.getPackageName());

            sections = getResources().getStringArray(resourceId);
        } else {
            sections = getResources().getStringArray(R.array.sections);
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Sections);

        for (final String section : sections){

            String[] parts = section.split("|||");

            title = parts[0];
            String icon;
            if (parts.length == 2){
                icon = parts[1];
            }

            TextView textView = new TextView(this);
            linearLayout.addView(textView);

            textView.setText(title);

            final String finalTitle = title;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SectionsActivity.this, SectionsActivity.class);
                    intent.putExtra("Section", finalTitle);
                    startActivity(intent);
                }
            });
        }
    }
}
