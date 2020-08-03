package com.example.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mProgressLoading;
    private RecyclerView recycler;
    private BookListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            mProgressLoading = (ProgressBar) findViewById(R.id.progressBar);
            recycler = (RecyclerView) findViewById(R.id.recycler);

            recycler.setLayoutManager(new LinearLayoutManager(BookListActivity.this));
            recycler.hasFixedSize();

            URL url = ApiUtil.buildUrl("cooking");
            new BookQueryTask().execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = (MenuItem) menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        try {
            URL url = ApiUtil.buildUrl(s);
            new BookQueryTask().execute(url);
        }catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BookQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String jsonResult = null;
            try {
                 jsonResult = ApiUtil.getJson(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResult;
        }

        @Override
        protected void onPostExecute(String jsonResult) {

            ArrayList<Book> books;
            TextView error = (TextView) findViewById(R.id.error);
            mProgressLoading.setVisibility(View.INVISIBLE);
//            StringBuilder result = new StringBuilder();

            //TextView text = (TextView) findViewById(R.id.tvResponse);


            if(jsonResult == null){
                error.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.INVISIBLE);
            }
            else{
                books = ApiUtil.getBookFromJson(jsonResult);

/*                for(Book book : books){
                    result.append(book.getTitle()).append("\n").append(book.getPublishedDate()).append("\n");
                }*/

                error.setVisibility(View.INVISIBLE);
                recycler.setVisibility(View.VISIBLE);

                adapter = new BookListAdapter(BookListActivity.this, books);
                recycler.setAdapter(adapter);
            }
            //text.setText(result.toString());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressLoading.setVisibility(View.VISIBLE);
        }
    }

}
