package com.synergysolutions.myshow.app.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 03/04/2014.
 */
public class Article {

    private long id;
    private long wikiaId;
    private String title;
    private String url;
    private String articleType;
    private String teaser;
    private String thumbnail;
    private String originalDimensions;
    private List<Section> sections;
    private List<Alias> aliases;

    public Article(){
        this.sections = new ArrayList<Section>();
        this.aliases = new ArrayList<Alias>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWikiaId() {
        return wikiaId;
    }

    public void setWikiaId(int wikiaId) {
        this.wikiaId = wikiaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOriginalDimensions() {
        return originalDimensions;
    }

    public void setOriginalDimensions(String originalDimensions) {
        this.originalDimensions = originalDimensions;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases = aliases;
    }
}
