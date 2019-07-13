package muchbeer.raum.com.gcertnotekeeper.internet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import muchbeer.raum.com.gcertnotekeeper.R;

public class BookListActivity extends AppCompatActivity {
    private ProgressBar mLoadingProgress;
    private final String LOG_TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);


        try {
            URL bookUrl = ApiUtil.buildUrl("cooking");
            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }

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
            TextView tvResult = (TextView) findViewById(R.id.tvResponse);
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
            Log.d(LOG_TAG, "This is now producing result which is: " + result);
        }
    }


}
