package com.synergysolutions.myshow.app;

import android.os.AsyncTask;

import com.synergysolutions.myshow.app.Entity.Alias;
import com.synergysolutions.myshow.app.Entity.Article;
import com.synergysolutions.myshow.app.Entity.LinkedArticle;
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
public class ArticlesJsonAsyncTask extends AsyncTask<String, Integer, List<Article>> {

    private static final String TAG = "HttpGetTask";

    IArticlesJsonResultProcessor articlesJsonResultProcessor;

    public ArticlesJsonAsyncTask(IArticlesJsonResultProcessor articlesJsonResultProcessor) {
        this.articlesJsonResultProcessor = articlesJsonResultProcessor;
    }

    protected List<Article> doInBackground(String... jsonString) {

        List<Article> result = new ArrayList<Article>();

        try {
            JSONArray articlesJson = (JSONArray) new JSONTokener(jsonString[0]).nextValue();

            jsonString[0] = "";

            for (int articleId = 0; articleId < articlesJson.length(); articleId++) {

                JSONObject articleJson = (JSONObject) articlesJson.get(articleId);

                Article article = new Article();
                article.setWikiaId(articleJson.getInt("id"));
                article.setTitle(articleJson.getString("title"));
                article.setUrl(articleJson.getString("url"));
                article.setArticleType(articleJson.getString("type"));
                article.setTeaser(articleJson.getString("abstract"));
                article.setThumbnail(articleJson.getString("thumbnail"));

                JSONArray aliasesJson = articleJson.getJSONArray("alias");

                for (int aliasId = 0; aliasId < aliasesJson.length(); aliasId++) {

                    String aliasTitle = aliasesJson.getString(aliasId);

                    Alias alias = new Alias();
                    alias.setArticle(article);
                    alias.setTitle(aliasTitle);

                    article.getAliases().add(alias);
                }

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

                        if (sectionContentJson.has("text")){
                            sectionContent.setText(sectionContentJson.getString("text"));
                        } else {
                            sectionContent.setText("");
                        }

                        section.getSectionContents().add(sectionContent);

                        //LinkedArticle

                        JSONArray linkedArticlesJson = sectionContentJson.getJSONArray("links");

                        for (int linkedArticleId = 0; linkedArticleId < linkedArticlesJson.length(); linkedArticleId++) {

                            JSONObject linkedArticleJson = (JSONObject) linkedArticlesJson.get(linkedArticleId);

                            LinkedArticle linkedArticle = new LinkedArticle();
                            linkedArticle.setSectionContent(sectionContent);
                            linkedArticle.setAlias(linkedArticleJson.getString("alias"));

                            sectionContent.getLinkedArticles().add(linkedArticle);
                        }

                        //ListElement

                        JSONArray listElementsJson = sectionContentJson.getJSONArray("elements");

                        for (int listElementId = 0; listElementId < listElementsJson.length(); listElementId++) {

                            JSONObject listElementJson = (JSONObject) listElementsJson.get(listElementId);

                            ListElement listElement = new ListElement();
                            listElement.setSectionContent(sectionContent);
                            listElement.setText(listElementJson.getString("text"));

                            sectionContent.getListElements().add(listElement);

                            //LinkedArticle
                            linkedArticlesJson = listElementJson.getJSONArray("links");

                            for (int linkedArticleId = 0; linkedArticleId < linkedArticlesJson.length(); linkedArticleId++) {

                                JSONObject linkedArticleJson = (JSONObject) linkedArticlesJson.get(linkedArticleId);

                                LinkedArticle linkedArticle = new LinkedArticle();
                                linkedArticle.setListElement(listElement);
                                linkedArticle.setAlias(linkedArticleJson.getString("alias"));

                                listElement.getLinkedArticles().add(linkedArticle);
                            }

                        }
                    }

                    JSONArray sectionImagesJson = sectionJson.getJSONArray("images");

                    for (int sectionImageId = 0; sectionImageId < sectionImagesJson.length(); sectionImageId++) {

                        JSONObject sectionImageJson = (JSONObject) sectionImagesJson.get(sectionImageId);

                        SectionImage sectionImage = new SectionImage();
                        sectionImage.setSection(section);
                        sectionImage.setSrc(sectionImageJson.getString("src"));

                        if (sectionImageJson.has("caption")){
                            sectionImage.setCaption(sectionImageJson.getString("caption"));
                        }


                        section.getSectionImages().add(sectionImage);
                    }

                }

                result.add(article);
            }

        } catch (JSONException e) {

            e.printStackTrace();
            result = new ArrayList<Article>();

        }

        return result;
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        this.articlesJsonResultProcessor.OnArticlesJsonResultFinish(result);
    }
}
