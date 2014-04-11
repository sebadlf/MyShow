package com.synergysolutions.myshow.app.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebadlf on 10/04/14.
 */
public class SectionContent {

    private int id;
    private Section section;
    private String type;
    private String text;
    private List<ListElement> listElements;

    public SectionContent() {
        this.listElements = new ArrayList<ListElement>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ListElement> getListElements() {
        return listElements;
    }

    public void setListElements(List<ListElement> listElements) {
        this.listElements = listElements;
    }
}
