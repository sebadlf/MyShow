package com.synergysolutions.myshow.app;

import java.util.List;

/**
 * Created by sdelafuente on 03/04/2014.
 */
public interface IArticlesDetailsLoadedResultProcessor {

    void OnArticlesDetailsLoadedResultFinish(List<Article> result);

}
