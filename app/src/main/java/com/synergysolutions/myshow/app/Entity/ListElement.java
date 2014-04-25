package com.synergysolutions.myshow.app.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebadlf on 10/04/14.
 */
public class ListElement {

    private int id;
    private SectionContent sectionContent;
    private String text;
    private List<LinkedArticle> linkedArticles;

    public ListElement() {
        this.linkedArticles = new ArrayList<LinkedArticle>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SectionContent getSectionContent() {
        return sectionContent;
    }

    public void setSectionContent(SectionContent sectionContent) {
        this.sectionContent = sectionContent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<LinkedArticle> getLinkedArticles() {
        return linkedArticles;
    }

    public void setLinkedArticles(List<LinkedArticle> linkedArticles) {
        this.linkedArticles = linkedArticles;
    }

}
