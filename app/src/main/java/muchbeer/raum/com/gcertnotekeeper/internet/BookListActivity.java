package muchbeer.raum.com.gcertnotekeeper.internet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import muchbeer.raum.com.gcertnotekeeper.R;
import muchbeer.raum.com.gcertnotekeeper.search.SearchActivity;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ProgressBar mLoadingProgress;
    private final String LOG_TAG = getClass().getSimpleName();
    private RecyclerView rvBooks;
    URL bookUrl;

    private LinearLayoutManager mBookLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);

        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        try {
            if (query == null  || query.isEmpty()) {
                bookUrl = ApiUtil.buildUrl("cooking");
            }
            else {
                bookUrl = new URL(query);
            }
            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }



        //create the layoutManager for the books (linear in this case, scrolling vertically
        mBookLayoutManager =
                new LinearLayoutManager(this);
        rvBooks.setLayoutManager(mBookLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
        final MenuItem searchItem=menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchURL);
            }
            catch (IOException e) {
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            mLoadingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {

            TextView tvError = (TextView) findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null) {
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }
            else {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);

                //This calls for a success
                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                String resultString = "";
                Log.d(LOG_TAG, "The result are: " + result);

                BookAdapter adapter = new BookAdapter(books);
                rvBooks.setAdapter(adapter);
            }

            /*TextView tvResult = (TextView) findViewById(R.id.tvResponse);
            TextView tvError = (TextView) findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null) {
                tvResult.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "This is now producing null " + result);
            }
            else {
                tvResult.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }

            tvResult.setText(result);
            Log.d(LOG_TAG, "This is now producing result which is: " + result);*/
        }
    }


}
