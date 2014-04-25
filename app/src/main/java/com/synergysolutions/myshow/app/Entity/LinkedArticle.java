package com.synergysolutions.myshow.app.Entity;

/**
 * Created by sebadlf on 22/04/14.
 */
public class LinkedArticle {

    private int id;
    private SectionContent sectionContent;
    private ListElement listElement;
    private String alias;

    public LinkedArticle() {

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

    public ListElement getListElement() {
        return listElement;
    }

    public void setListElement(ListElement listElement) {
        this.listElement = listElement;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
