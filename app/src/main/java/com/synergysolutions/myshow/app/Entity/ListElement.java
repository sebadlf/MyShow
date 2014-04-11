package com.synergysolutions.myshow.app.Entity;

/**
 * Created by sebadlf on 10/04/14.
 */
public class ListElement {

    private int id;
    private SectionContent sectionContent;
    private String text;

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
}
