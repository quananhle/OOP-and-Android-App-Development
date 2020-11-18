package com.quananhle.newsgateway.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.quananhle.newsgateway.MainActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ArticlesDownloader extends AsyncTask<String, Void, Void> {
    private static final String TAG = "ArticlesDownloader";
    private NewsService
    private ArrayList<Article> articleArrayList = new ArrayList<>();

    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL_BEGIN = "https://newsapi.org/v2/everything?sources=";
    private static final String DATA_URL_END   = "&language=en&pageSize=20&apiKey=";

    String author = "", title = "", description = "", url = "", urlToImage = "", publishedAt = "";


    @Override
    protected void onPostExecute(ArrayList<Article> articles){
        Log.d(TAG, "onPostExecute: (HeadlinesLoader) | Total articles: " + articles.size());
        mainActivity.updateHeadlines(articles);
        super.onPostExecute(articles);
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
    private ArrayList<Article> parseJSON(String str){
        Log.d(TAG, "parseJSON: (HeadlinesLoader) String is " + str);
        articleArrayList = new ArrayList<>();
        Article article = new Article();
        Log.d(TAG, "parseJSON: (HeadlinesLoader) starting parsing JSON");
        /*
    @Override
    protected Void doInBackground(String... strings) {
        return null;

         */
    }
}
