package com.synergysolutions.myshow.app.Entity;

/**
 * Created by sebadlf on 22/04/14.
 */
public class Alias {
    private int id;
    private Article article;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
