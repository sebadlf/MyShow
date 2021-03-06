package com.synergysolutions.myshow.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.synergysolutions.myshow.app.Entity.Article;

import java.util.Collections;
import java.util.List;

/**
 * Created by sebadlf on 30/03/14.
 */
public class ArticlesAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Article> articleList = Collections.EMPTY_LIST;

    private Context context;

    public ArticlesAdapter(Context context){
        this.context = context;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(List<Article> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Article)this.getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_demo_list_row, null);
        }

        Article article = (Article)this.getItem(position);

        ImageView imgThombnail = (ImageView) convertView.findViewById(R.id.thumbnail);

        imgThombnail.setImageResource(R.drawable.logo);

        if ((article.getThumbnail() != null) && (article.getThumbnail().startsWith("http"))){
            new DownloadImageTask(context,  imgThombnail).execute(article.getThumbnail());
        }

        TextView txtNombre = (TextView) convertView.findViewById(R.id.nombre);
        txtNombre.setText(article.getTitle());

        TextView txtAlias = (TextView) convertView.findViewById(R.id.alias);
        txtAlias.setText(article.getTeaser());

        return convertView;

    }


}
