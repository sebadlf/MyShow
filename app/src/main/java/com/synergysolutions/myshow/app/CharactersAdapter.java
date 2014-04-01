package com.synergysolutions.myshow.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by sebadlf on 30/03/14.
 */
public class CharactersAdapter extends BaseAdapter {

    private List<Character> charactersList = Collections.emptyList();

    private Context context;

    public CharactersAdapter(Context context){
        this.context = context;
    }

    public void updateCharactersList(List<Character> charactersList){
        this.charactersList = charactersList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return charactersList.size();
    }

    @Override
    public Object getItem(int position) {
        return charactersList.get(position);
    }

    @Override
    public long getItemId(int position) {

        Character character = (Character)this.getItem(position);

        return character != null ? character.getId() : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
