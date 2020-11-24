package com.quananhle.newsgateway.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticlesDownloader extends AsyncTask<String, Void, ArrayList<Article>> {
    private static final String TAG = "ArticlesDownloader";
    private NewsService newsService;
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL_BEGIN = "https://newsapi.org/v2/everything?sources=";
    private static final String DATA_URL_END   = "&language=en&pageSize=20&apiKey=";

    String author = "", title = "", description = "", url = "", urlToImage = "", publishedAt = "";

    public ArticlesDownloader(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        String source = strings[0];
        String dataURL = DATA_URL_BEGIN + source + DATA_URL_END + API_KEY;
        Log.d(TAG, "doInBackground: (ArticlesDownloader) URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: (ArticlesDownloader) " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            Log.d(TAG, "doInBackground: (ArticlesDownloader) " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            Log.e(TAG, "doInBackground: (ArticlesDownloader) MalformedURLException ", mURLe);
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            Log.e(TAG, "doInBackground: (ArticlesDownloader) ProtocolException ", pe);
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "doInBackground: (ArticlesDownloader) FileNotFoundException ", fnfe);
            fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: (ArticlesDownloader) IOException ", ioe);
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: (ArticlesDownloader) Exception ", e);
            e.printStackTrace();
        }
        articleArrayList = parseJSON(stringBuilder.toString());
        Log.d(TAG, "doInBackground: (ArticlesDownloader) | articleArrayList: " + articleArrayList);
        return articleArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles){
        Log.d(TAG, "onPostExecute: (ArticlesDownloader) | Total articles: " + articles.size());
        newsService.setArticles(articles);
        super.onPostExecute(articles);
    }

    private ArrayList<Article> parseJSON(String str){
        Log.d(TAG, "parseJSON: (ArticlesDownloader) String is " + str);
        ArrayList<Article> articleList = new ArrayList<>();
        Article article = new Article();
        Log.d(TAG, "parseJSON: (ArticlesDownloader) starting parsing JSON");
        /*
        {
        "status": "ok",
        "totalResults": 4612,
        "articles": [
        {
            "source": {
                "id": "cnn",
                "name": "CNN"
            },
            "author": "Michelle Toh, CNN Business",
            "title": "Beyond Meat launches plant-based minced pork in China",
            "description": "Beyond Meat is launching a plant-based version of China's favorite meat.",
            "url": "https://www.cnn.com/2020/11/18/business/beyond-meat-china-intl-hnk/index.html",
            "urlToImage": "https://cdn.cnn.com/cnnnext/dam/assets/201118123254-beyond-pork-biscuits-gravy-super-tease.jpg",
            "publishedAt": "2020-11-18T06:30:47Z",
            "content": null
        },
        {
            "source": {
                "id": "cnn",
                "name": "CNN"
            },
            "author": "Madeline Holcombe and Raja Razek, CNN",
            "title": "Father sues the city of Fort Worth and former police officer for shooting death of Atatiana Jefferson",
            "description": "The family of Atatiana Jefferson, who was fatally shot through the window of her home, has filed a lawsuit against the city of Fort Worth and Aaron Dean, the former Fort Worth police officer who shot her.",
            "url": "https://www.cnn.com/2020/11/18/us/atatiana-jefferson-fort-worth-father-lawsuit/index.html",
            "urlToImage": "https://cdn.cnn.com/cnnnext/dam/assets/191013161737-atatiana-koquice-jefferson-fort-worth-police-shooting-super-tease.jpg",
            "publishedAt": "2020-11-18T06:26:09Z",
            "content": "(CNN)The family of Atatiana Jefferson, who was fatally shot through the window of her home, has filed a lawsuit against the city of Fort Worth and Aaron Dean, the former Fort Worth police officer who… [+2002 chars]"
        },
        {
            "source": {
                "id": "cnn",
                "name": "CNN"
            },
            "author": "Shelby Lin Erdman, CNN",
            "title": "FDA authorizes first rapid Covid-19 self-testing kit for at-home diagnosis",
            "description": "The US Food and Drug Administration has issued an emergency use authorization for the first self-test for Covid-19 that can provide rapid results at home.",
            "url": "https://www.cnn.com/2020/11/18/health/covid-home-self-test/index.html",
            "urlToImage": "https://cdn.cnn.com/cnnnext/dam/assets/200811105852-fda-super-tease.jpg",
            "publishedAt": "2020-11-18T05:52:18Z",
            "content": null
        },
         */
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray articles = (JSONArray) jsonObject.get("articles");
            for (int i=0; i < articles.length(); ++i){
                JSONObject jsonObj = (JSONObject) articles.get(i);
                article = new Article();
                article.setAuthor(getAuthor(jsonObj));
                article.setTitle(getTitle(jsonObj));
                article.setDescription(getDescription(jsonObj));
                article.setUrl(getUrl(jsonObj));
                article.setImageUrl(getImage(jsonObj));
                article.setPublishingDate(getDate(jsonObj));
                articleList.add(article);
            }
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | Exception " + e);
        }
        return articleList;
    }
    //====================== *** HELPER•METHODS *** ======================//
    private String getAuthor(JSONObject object){
        try {
            author = !object.has("author") ? "" : object.getString("author");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (getAuthor) " + e);
        }
        return author;
    }
    private String getTitle(JSONObject object){
        try {
            title = !object.has("title") ? "" : object.getString("title");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (getTitle) " + e);
        }
        return title;
    }
    private String getDescription(JSONObject object){
        try {
            description = !object.has("description") ? "" : object.getString("description");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (getDescription) " + e);
        }
        return description;
    }
    private String getUrl(JSONObject object){
        try {
            url = !object.has("url") ? "" : object.getString("url");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (getUrl) " + e);
        }
        return url;
    }
    private String getImage(JSONObject object){
        try {
            urlToImage = !object.has("urlToImage") ? "" : object.getString("urlToImage");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (getImage) " + e);
        }
        return urlToImage;
    }
    private String getDate(JSONObject object){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        try {
            publishedAt = !object.has("publishedAt") ? "" : object.getString("publishedAt");
            if (publishedAt != null || !publishedAt.isEmpty()){
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(publishedAt);
            }
            publishedAt = simpleDateFormat.format(date);
        } catch (ParseException pe){
            pe.printStackTrace();
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (ParseException) " + pe);
        } catch (Exception e){
            Log.d(TAG, "parseJSON: (ArticlesDownloader) | (Exception) " + e);
        }
        return publishedAt;
    }
}
