package com.synergysolutions.myshow.app;

import android.os.AsyncTask;

import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.ListElement;
import com.synergysolutions.myshow.app.Entity.Section;
import com.synergysolutions.myshow.app.Entity.SectionContent;
import com.synergysolutions.myshow.app.Entity.SectionImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdelafuente on 01/04/2014.
 */
public class ArticlesJsonAsyncTaskAsyncTask extends AsyncTask<String, Integer, List<Article>> {

    private static final String TAG = "HttpGetTask";

    IArticlesJsonResultProcessor articlesJsonResultProcessor;

    public ArticlesJsonAsyncTaskAsyncTask(IArticlesJsonResultProcessor articlesJsonResultProcessor) {
        this.articlesJsonResultProcessor = articlesJsonResultProcessor;
    }

    protected List<Article> doInBackground(String... jsonString) {

        List<Article> result = new ArrayList<Article>();

        try {
            JSONArray articlesJson = (JSONArray) new JSONTokener(jsonString[0]).nextValue();

            for (int articleId = 0; articleId < articlesJson.length(); articleId++) {

                JSONObject articleJson = (JSONObject) articlesJson.get(articleId);

                Article article = new Article();
                article.setWikiaId(articleJson.getInt("wikiaId"));
                article.setTitle(articleJson.getString("title"));
                article.setUrl(articleJson.getString("url"));
                article.setArticleType(articleJson.getString("articleType"));
                article.setTeaser(articleJson.getString("teaser"));
                article.setThumbnail(articleJson.getString("thumbnail"));

                JSONArray sectionsJson = articleJson.getJSONArray("sections");

                for (int sectionId = 0; sectionId < sectionsJson.length(); sectionId++) {

                    JSONObject sectionJson = (JSONObject) sectionsJson.get(sectionId);

                    Section section = new Section();
                    section.setArticle(article);
                    section.setLevel(sectionJson.getInt("level"));
                    section.setTitle(sectionJson.getString("title"));

                    article.getSections().add(section);

                    JSONArray sectionContentsJson = sectionJson.getJSONArray("content");

                    for (int sectionContentId = 0; sectionContentId < sectionContentsJson.length(); sectionContentId++) {

                        JSONObject sectionContentJson = (JSONObject) sectionContentsJson.get(sectionContentId);

                        SectionContent sectionContent = new SectionContent();
                        sectionContent.setSection(section);
                        sectionContent.setType(sectionContentJson.getString("type"));
                        sectionContent.setText(sectionContentJson.getString("text"));

                        section.getSectionContents().add(sectionContent);

                        //ListElement

                        JSONArray listElementsJson = sectionContentJson.getJSONArray("elements");

                        for (int listElementId = 0; listElementId < listElementsJson.length(); listElementId++) {

                            JSONObject listElementJson = (JSONObject) listElementsJson.get(listElementId);

                            ListElement listElement = new ListElement();
                            listElement.setSectionContent(sectionContent);
                            listElement.setText(listElementJson.getString("text"));

                            sectionContent.getListElements().add(listElement);
                        }
                    }

                    JSONArray sectionImagesJson = sectionJson.getJSONArray("images");

                    for (int sectionImageId = 0; sectionImageId < sectionImagesJson.length(); sectionImageId++) {

                        JSONObject sectionImageJson = (JSONObject) sectionImagesJson.get(sectionImageId);

                        SectionImage sectionImage = new SectionImage();
                        sectionImage.setSection(section);
                        sectionImage.setSrc(sectionImageJson.getString("src"));
                        sectionImage.setCaption(sectionImageJson.getString("caption"));

                        section.getSectionImages().add(sectionImage);
                    }

                }

                result.add(article);
            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        this.articlesJsonResultProcessor.OnArticlesJsonResultFinish(result);
    }
}
