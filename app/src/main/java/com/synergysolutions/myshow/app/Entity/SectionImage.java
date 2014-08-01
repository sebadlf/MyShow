package com.synergysolutions.myshow.app.Entity;

/**
 * Created by sebadlf on 10/04/14.
 */
public class SectionImage {

    private long id;
    private Section section;
    private String src;
    private String caption;

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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
