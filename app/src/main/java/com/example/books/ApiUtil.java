package com.example.books;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {

    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String KEY = "Key";
    private static final String API_KEY = "your google books api key";

    private ApiUtil(){}

    private static final String BASE_API_URL =
            "https://WWW.googleapis.com/books/v1/volumes";

    public static URL buildUrl(String title){

        //String fullUrl = BASE_API_URL + "?q=" + title;
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        try{
            //url = new URL(fullUrl);
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if(hasData){
                return scanner.next();
            }else {
                return null;
            }
        }
        catch (Exception e){
            Log.d("Error", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }

    }

    public static ArrayList<Book> getBookFromJson(String json){

        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGELINK = "imageLinks";
        final String THUMBNAILS = "thumbnail";

        ArrayList<Book> books = new ArrayList<>();

        try{

            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayBooks = jsonObject.getJSONArray(ITEMS);
            int numberOfBook = arrayBooks.length();

            for(int i =0; i < numberOfBook; i++){
                JSONObject item = arrayBooks.getJSONObject(i);
                JSONObject itemVolumeInfo = item.getJSONObject(VOLUME_INFO);
                JSONObject imageLinks = itemVolumeInfo.getJSONObject(IMAGELINK);

                int authorNumber = itemVolumeInfo.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNumber];
                for(int j = 0; j < authorNumber; j++){
                    authors[j] = itemVolumeInfo.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(
                        item.getString(ID),
                        itemVolumeInfo.getString(TITLE),
                        (itemVolumeInfo.isNull(SUBTITLE) ? "" : itemVolumeInfo.getString(SUBTITLE)),
                        authors,
                        itemVolumeInfo.getString(PUBLISHER),
                        itemVolumeInfo.getString(PUBLISHED_DATE),
                        itemVolumeInfo.getString(DESCRIPTION),
                        imageLinks.getString(THUMBNAILS));
                books.add(book);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return books;
    }
}
