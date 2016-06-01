package apdallah.moivedb.com.moivedb.Tasks;

import android.app.Activity;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import apdallah.moivedb.com.moivedb.Items.MovieItem;
import apdallah.moivedb.com.moivedb.Adapters.MoivesAdapter;
import apdallah.moivedb.com.moivedb.MoviesActivity;
import apdallah.moivedb.com.moivedb.MoviesFragment;

/**
 * Created by Apdallah on 3/24/2016.
 */
public class MoivesAsyncTask extends AsyncTask<String, Void, ArrayList<MovieItem>> {
    MoivesAdapter moiveAdapter;
    Activity activity;


    public MoivesAsyncTask(MoivesAdapter adapter, Activity act) {
        this.moiveAdapter = adapter;
        this.activity = act;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {

        if (null != params[1] && !params[1].isEmpty()) {
            String selectionPref = params[0];

            final String POPULAR_MOIVES_BASE =
                    "https://api.themoviedb.org/3/" + selectionPref + "?";
            // final String QUERY_PARAM = "sort_by";
            final String API_ID = "api_key";
            Uri builtUri = Uri.parse(POPULAR_MOIVES_BASE).buildUpon()
                    .appendQueryParameter(API_ID, params[1])
                    .build();
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moivesJsonString = null;
            try {
                URL url = new URL(builtUri.toString());
                //Connect to the api
                urlConnection = (HttpsURLConnection) url.openConnection();
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

            if (moivesJsonString != null && !moivesJsonString.isEmpty())
                return getMoivesFromJson(moivesJsonString);
            else
                return new ArrayList<MovieItem>();

        } else {
            Log.i("APIKEYELSE",""+params[1]);
            return new ArrayList<MovieItem>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItems) {
        moiveAdapter.setMovieItems(movieItems);
        moiveAdapter.notifyDataSetChanged();
        if (MoviesActivity.mTwoPane&&movieItems.size()!=0)
            ((MoviesFragment.OnItemSelectedCallBack) activity)
                    .onItemSelected(moiveAdapter.getItem(0));
    }

    private String getReadableDate(String date) {
        String readableDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date pdate = format.parse(date);
            readableDate = pdate.toString();
            Log.i("DAte", readableDate + "");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return readableDate;
    }

    private ArrayList<MovieItem> getMoivesFromJson(String moivesJson) {
        ArrayList<MovieItem> moives = new ArrayList<MovieItem>();
        try {
            JSONObject moiveJsonObj = new JSONObject(moivesJson);
            JSONArray moiveJsonArr = moiveJsonObj.getJSONArray("results");
            for (int i = 0; i < moiveJsonArr.length(); i++) {
                MovieItem item = new MovieItem();
                JSONObject jsonItem = moiveJsonArr.getJSONObject(i);
                item.setOverView(jsonItem.getString("overview"));
                item.setTitle(jsonItem.getString("title"));
                item.setPostarPath(jsonItem.getString("poster_path"));
                item.setRelease_date(jsonItem.getString("release_date"));
                item.setId(jsonItem.getInt("id"));
                item.setRate(jsonItem.getDouble("vote_average"));
                moives.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moives;
    }
}
