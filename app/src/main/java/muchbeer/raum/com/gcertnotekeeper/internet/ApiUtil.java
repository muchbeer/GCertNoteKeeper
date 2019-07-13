package muchbeer.raum.com.gcertnotekeeper.internet;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil {

    //Try to access the following link:
    //https://www.googleapis.com/books/v1/volumes/?q=android
    //https is the scheme, www.googleapis.com=Authority, /books/v1/volumes/=paths, ?q=android is the query
    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes";
    public static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyBMczblzWi3frUkgWfWLtjEZIc6wrI2mLg";

    public static URL buildUrl(String title) {


      /*  public static URL buildUrl(String title) {
            String fullUrl = BASE_API_URL + "?Q=" + title;
            URL url = null;

            try {
                url = new URL(fullUrl);
            }catch (Exception e) {
                e.printStackTrace();
            }return url;
        }*/

        URL url = null;
        //build the below url
        //https://www.googleapis.com/books/v1/volumes?q=cooking&key=AI....
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        }
        catch (Exception e){
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
            if (hasData) {
                return scanner.next();
            } else {
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
}
