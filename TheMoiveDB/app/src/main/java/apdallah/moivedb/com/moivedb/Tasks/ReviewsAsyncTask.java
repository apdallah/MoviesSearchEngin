package apdallah.moivedb.com.moivedb.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import apdallah.moivedb.com.moivedb.Adapters.ReviewsAdapter;
import apdallah.moivedb.com.moivedb.Adapters.TrailersAdapter;
import apdallah.moivedb.com.moivedb.Items.ReviewItem;
import apdallah.moivedb.com.moivedb.Items.TrailersItem;

/**
 * Created by Apdallah on 3/25/2016.
 */
public class ReviewsAsyncTask extends AsyncTask<String, Void, ArrayList<ReviewItem>> {
    ReviewsAdapter reviewsAdapter;

    public ReviewsAsyncTask(ReviewsAdapter adapter) {
        this.reviewsAdapter = adapter;
    }

    @Override
    protected ArrayList<ReviewItem> doInBackground(String... params) {
        if (null != params[1] && !params[1].isEmpty()) {

            final String MOIVE_REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";
            final String API_ID = "api_key";
            Uri builtUri = Uri.parse(MOIVE_REVIEW_BASE_URL).buildUpon()
                    .appendQueryParameter(API_ID, params[1])
                    .build();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moivesJsonString = null;
            try {
                URL url = new URL(builtUri.toString());
                //Connect to the api
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //open input stream and read Json data
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                moivesJsonString = buffer.toString();

                Log.i("Trailers", moivesJsonString);

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", builtUri.toString());

                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e("ProtocolException", builtUri.toString());

                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOECxption", builtUri.toString());
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            if (null != moivesJsonString)
                return getReviewssFromJson(moivesJsonString);
            else
                return new ArrayList<ReviewItem>();
        } else return new ArrayList<ReviewItem>();


    }

    @Override
    protected void onPostExecute(ArrayList<ReviewItem> reviewsItems) {
        reviewsAdapter.setMoiveItems(reviewsItems);
        reviewsAdapter.notifyDataSetChanged();
    }

    private ArrayList<ReviewItem> getReviewssFromJson(String moivesJson) {
        ArrayList<ReviewItem> reviews = new ArrayList<ReviewItem>();
        try {
            JSONObject moiveJsonObj = new JSONObject(moivesJson);
            JSONArray moiveJsonArr = moiveJsonObj.getJSONArray("results");
            for (int i = 0; i < moiveJsonArr.length(); i++) {
                ReviewItem item = new ReviewItem();
                JSONObject jsonItem = moiveJsonArr.getJSONObject(i);
                item.setAuthor(jsonItem.getString("author"));
                item.setContent(jsonItem.getString("content"));
                reviews.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}

