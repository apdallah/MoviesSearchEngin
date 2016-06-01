package apdallah.moivedb.com.moivedb.Tasks;

import android.content.Intent;
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

import apdallah.moivedb.com.moivedb.Adapters.TrailersAdapter;
import apdallah.moivedb.com.moivedb.DetailsFragment;
import apdallah.moivedb.com.moivedb.Items.TrailersItem;

/**
 * Created by Apdallah on 3/25/2016.
 */
public class TrailersAsyncTask extends AsyncTask<String, Void, ArrayList<TrailersItem>> {
    TrailersAdapter trailersAdapter;
     public TrailersAsyncTask(TrailersAdapter adapter) {
        this.trailersAdapter = adapter;
     }

    @Override
    protected ArrayList<TrailersItem> doInBackground(String... params) {

        if (null != params[1] && !params[1].isEmpty()) {

            final String MOIVE_TRAILER = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";
            final String API_ID = "api_key";
            Uri builtUri = Uri.parse(MOIVE_TRAILER).buildUpon()
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
                return getTrailersFromJson(moivesJsonString);
            else
                return new ArrayList<TrailersItem>();
        } else
            return new ArrayList<TrailersItem>();
    }

    @Override
    protected void onPostExecute(ArrayList<TrailersItem> trailersItems) {
        trailersAdapter.setTrailerItems(trailersItems);
        trailersAdapter.notifyDataSetChanged();
        if (trailersItems.size() != 0) {
             if (DetailsFragment.mShareActionProvider != null) {
                DetailsFragment.mShareActionProvider.setShareIntent(CreateShareIntent("https://www.youtube.com/watch?v=" + trailersAdapter.getItem(0).getKey()));
            }
        }


    }
    private Intent CreateShareIntent(String shareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareText + " @MovieDb");
        return shareIntent;
    }
    private ArrayList<TrailersItem> getTrailersFromJson(String moivesJson) {
        ArrayList<TrailersItem> trailers = new ArrayList<TrailersItem>();
        try {
            JSONObject moiveJsonObj = new JSONObject(moivesJson);
            JSONArray moiveJsonArr = moiveJsonObj.getJSONArray("results");
            for (int i = 0; i < moiveJsonArr.length(); i++) {
                TrailersItem item = new TrailersItem();
                JSONObject jsonItem = moiveJsonArr.getJSONObject(i);
                item.setTitile(jsonItem.getString("name"));
                item.setKey(jsonItem.getString("key"));
                trailers.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}
