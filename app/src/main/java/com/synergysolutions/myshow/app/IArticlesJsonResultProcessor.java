package com.synergysolutions.myshow.app;

import com.synergysolutions.myshow.app.Entity.Article;

import java.util.List;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public interface IArticlesJsonResultProcessor {

    DownloadResult OnArticlesJsonResultFinish(List<Article> articleList);

}
