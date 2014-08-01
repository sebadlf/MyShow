package com.synergysolutions.myshow.app.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 03/04/2014.
 */
public class Section {

    private long id;
    private Article article;
    private int level;
    private String title;
    private List<SectionContent> sectionContents;
    private List<SectionImage> sectionImages;

    public Section() {
        this.sectionContents = new ArrayList<SectionContent>();
        this.sectionImages = new ArrayList<SectionImage>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SectionContent> getSectionContents() {
        return sectionContents;
    }

    public void setSectionContents(List<SectionContent> sectionContents) {
        this.sectionContents = sectionContents;
    }

    public List<SectionImage> getSectionImages() {
        return sectionImages;
    }

    public void setSectionImages(List<SectionImage> sectionImages) {
        this.sectionImages = sectionImages;
    }
}
