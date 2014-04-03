package com.synergysolutions.myshow.app;

import java.util.List;

/**
 * Created by sdelafuente on 03/04/2014.
 */
public interface IArticlesLoadedResultProcessor {

    void OnArticlesLoadedResultFinish(List<Article> result);

}
