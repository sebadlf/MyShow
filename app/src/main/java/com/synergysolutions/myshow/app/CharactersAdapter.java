package com.synergysolutions.myshow.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by sebadlf on 30/03/14.
 */
public class CharactersAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Character> characterList = Collections.EMPTY_LIST;

    private Context context;

    public CharactersAdapter(Context context){
        this.context = context;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(List<Character> characterList) {
        this.characterList = characterList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return characterList.size();
    }

    @Override
    public Object getItem(int position) {
        return characterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Character)this.getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_demo_list_row, null);
        }

        Character character = (Character)this.getItem(position);

        TextView txtNombre = (TextView) convertView.findViewById(R.id.nombre);
        txtNombre.setText(character.getName());

        TextView txtAlias = (TextView) convertView.findViewById(R.id.alias);
        txtAlias.setText(character.getAlias());

        return convertView;

    }


}
