package com.synergysolutions.myshow.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 03/04/2014.
 */
public class Article {

    private int id;
    private int wikiaId;
    private String title;
    private String url;
    private String type;
    private String abstractDesc;
    private String thumbnail;
    private String originalDimensions;
    private List<Section> sections;

    private boolean detailsDownloaded;
    private boolean sectionsDownloaded;

    public Article(){
        this.sections = new ArrayList<Section>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWikiaId() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbstractDesc() {
        return abstractDesc;
    }

    public void setAbstractDesc(String abstractDesc) {
        this.abstractDesc = abstractDesc;
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

    public boolean isDetailsDownloaded() {
        return detailsDownloaded;
    }

    public void setDetailsDownloaded(boolean detailsDownloaded) {
        this.detailsDownloaded = detailsDownloaded;
    }

    public boolean isSectionsDownloaded() {
        return sectionsDownloaded;
    }

    public void setSectionsDownloaded(boolean sectionsDownloaded) {
        this.sectionsDownloaded = sectionsDownloaded;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
