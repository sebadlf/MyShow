package com.synergysolutions.myshow.app.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebadlf on 10/04/14.
 */
public class SectionContent {

    private long id;
    private Section section;
    private String type;
    private String text;
    private List<ListElement> listElements;
    private List<LinkedArticle> linkedArticles;

    public SectionContent() {
        this.listElements = new ArrayList<ListElement>();
        this.linkedArticles = new ArrayList<LinkedArticle>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<LinkedArticle> getLinkedArticles() {
        return linkedArticles;
    }

    public void setLinkedArticles(List<LinkedArticle> linkedArticles) {
        this.linkedArticles = linkedArticles;
    }

}
