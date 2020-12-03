package com.quananhle.newsgateway.loader;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.service.Article;
import com.quananhle.newsgateway.service.NewsService;

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

public class HeadlinesLoaderRunnable implements Runnable{
    private static final String TAG = "HeadlinesLoader";
    private static final String REQUEST_METHOD = "GET";
    private MainActivity mainActivity;

    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL = "http://newsapi.org/v2/top-headlines?country=us&apiKey=";

    String author = "", title = "", description = "", url = "", urlToImage = "", publishedAt = "";

    public HeadlinesLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run(){
        final ArrayList<Article> articles;
        String dataURL = DATA_URL + API_KEY;
        Log.d(TAG, "doInBackground: URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: URL to use is " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            Log.d(TAG, "doInBackground: " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            Log.e(TAG, "doInBackground: MalformedURLException ", mURLe);
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            Log.e(TAG, "doInBackground: ProtocolException ", pe);
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "doInBackground: FileNotFoundException ", fnfe);
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: IOException ", ioe);
            ioe.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Exception ", e);
            e.printStackTrace();
        }
        articles = parseJSON(stringBuilder.toString());
    }

    //====================== *** HELPER•METHODS *** ======================//

    private void processResults(String str) {
        if (str == null){
            Log.d(TAG, "handleResults: Failure in data downloading");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.downloadFailed();
                }
            });
            return;
        }
        final ArrayList<Article> articles = parseJSON(str);
        if (articles != null)  {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.updateHeadlines(articles);
                }
            });
        }
    }

    private ArrayList<Article> parseJSON(String str){
        Log.d(TAG, "parseJSON: (HeadlinesLoader) String is " + str);
        ArrayList<Article> headlineList = new ArrayList<>();
        Article article = new Article();
        Log.d(TAG, "parseJSON: (HeadlinesLoader) starting parsing JSON");
        /*
        {
        "status": "ok",
        "totalResults": 38,
        "articles": [
        {
            "source": {
                "id": "cnn",
                "name": "CNN"
            },
            "author": "Eric Bradner, CNN",
            "title": "Biden builds out White House senior staff with top campaign advisers - CNN",
            "description": "President-elect Joe Biden is building out the staff that will surround him in the White House, announcing Tuesday that he is tapping two long-time advisers for top roles and naming several other veterans of his presidential campaign to senior positions.",
            "url": "https://www.cnn.com/2020/11/17/politics/biden-white-house-senior-staff/index.html",
            "urlToImage": "https://cdn.cnn.com/cnnnext/dam/assets/201116175720-joe-biden-1105-file-super-tease.jpg",
            "publishedAt": "2020-11-17T16:13:00Z",
            "content": "(CNN)President-elect Joe Biden is building out the staff that will surround him in the White House, announcing Tuesday that he is tapping two long-time advisers for top roles and naming several other… [+3179 chars]"
        },
        {
            "source": {
                "id": null,
                "name": "CNBC"
            },
            "author": "Jacob Pramuk",
            "title": "Congress is far from sending more stimulus help as coronavirus cases surge and economic pain looms - CNBC",
            "description": "Congress has failed to pass another coronavirus stimulus bill as infections rise and state and local governments implement new restrictions.",
            "url": "https://www.cnbc.com/2020/11/17/coronavirus-stimulus-bill-mcconnell-schumer-pelosi-have-not-held-talks.html",
            "urlToImage": "https://image.cnbcfm.com/api/v1/image/106798213-1605630445857-106798213-1605629350167-gettyimages-1229556275-201109dc-015.jpg?v=1605630487",
            "publishedAt": "2020-11-17T15:45:00Z",
            "content": "Congress appeared nowhere close to passing another coronavirus relief bill Tuesday as infections surge across the country and new public health restrictions threaten businesses and jobs.\r\nLawmakers h… [+4067 chars]"
        },
         */
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray articles = (JSONArray) jsonObject.get("articles");
            for (int i=0; i < articles.length(); ++i){
                JSONObject jsonObj = (JSONObject) articles.get(i);
                article.setAuthor(getAuthor(jsonObj));
                article.setTitle(getTitle(jsonObj));
                article.setDescription(getDescription(jsonObj));
                article.setUrl(getUrl(jsonObj));
                article.setImageUrl(getImage(jsonObj));
                article.setPublishingDate(getDate(jsonObj));
                headlineList.add(article);
            }
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesDownloader) | Exception " + e);
        }
        return headlineList;
    }

    //====================== *** HELPER•METHODS *** ======================//
    private String getAuthor(JSONObject object){
        try {
            author = !object.has("author") ? "" : object.getString("author");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (getAuthor) " + e);
        }
        return author;
    }
    private String getTitle(JSONObject object){
        try {
            title = !object.has("title") ? "" : object.getString("title");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (getTitle) " + e);
        }
        return title;
    }
    private String getDescription(JSONObject object){
        try {
            description = !object.has("description") ? "" : object.getString("description");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (getDescription) " + e);
        }
        return description;
    }
    private String getUrl(JSONObject object){
        try{
            url = !object.has("url") ? "" : object.getString("url");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (getUrl) " + e);
        }
        return url;
    }
    private String getImage(JSONObject object) {
        try {
            urlToImage = !object.has("urlToImage") ? "" : object.getString("urlToImage");
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (getImage) " + e);
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
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (ParseException) " + pe);
        } catch (Exception e){
            Log.d(TAG, "parseJSON: (HeadlinesLoader) | (Exception) " + e);
        }
        return publishedAt;
    }
}
