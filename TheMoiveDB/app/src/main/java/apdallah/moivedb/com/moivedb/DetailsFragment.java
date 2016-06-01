package apdallah.moivedb.com.moivedb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apdallah.moivedb.com.moivedb.Adapters.ReviewsAdapter;
import apdallah.moivedb.com.moivedb.Adapters.TrailersAdapter;
import apdallah.moivedb.com.moivedb.Items.MovieItem;
import apdallah.moivedb.com.moivedb.Items.ReviewItem;
import apdallah.moivedb.com.moivedb.Items.TrailersItem;
import apdallah.moivedb.com.moivedb.MoiveDataBase.MovieDbHelper;
import apdallah.moivedb.com.moivedb.Tasks.ReviewsAsyncTask;
import apdallah.moivedb.com.moivedb.Tasks.TrailersAsyncTask;

/**
 * Created by Apdallah on 3/25/2016.
 */
public class DetailsFragment extends Fragment {
    private final String PIC_BASE_PATH = "http://image.tmdb.org/t/p/w500";
    public static android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private final String APP_NAME = "@TheMoiveDB";
    public static String shareStr="";
    private ImageView favorite;
    private MovieItem movieItem;
    private MovieDbHelper mDbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_fragment_menu, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null)
            mShareActionProvider.setShareIntent(CreateShareIntent());
    }

    private Intent CreateShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareStr + APP_NAME);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mDbHelper = new MovieDbHelper(getActivity());
        TextView details = (TextView) view.findViewById(R.id.details);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView titile = (TextView) view.findViewById(R.id.titile);
        TextView rate = (TextView) view.findViewById(R.id.rate);
        ImageView Poster = (ImageView) view.findViewById(R.id.posterview);
        favorite = (ImageView) view.findViewById(R.id.favirout);
        Bundle args = getArguments();
        if (null != args) {
            movieItem = (MovieItem) args.getSerializable("MovieItem");
        } else if (getActivity().getIntent().getSerializableExtra("MovieItem") != null) {
            movieItem = (MovieItem) getActivity().getIntent().getSerializableExtra("MovieItem");

        }
        if (movieItem != null) {
            details.setText(movieItem.getOverView());
            titile.setText(movieItem.getTitle());
            date.setText(movieItem.getRelease_date());
            rate.setText(movieItem.getRate() + "/10");

            Picasso.with(getActivity()).load(PIC_BASE_PATH + movieItem.getPostarPath()).into(Poster);
//Get Trailers into list
            ListView trailerList = (ListView) view.findViewById(R.id.trailerList);
            final TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), new ArrayList<TrailersItem>());
            trailerList.setAdapter(trailersAdapter);
            trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + trailersAdapter.getItem(position).getKey()
                            )));
                }
            });
            new TrailersAsyncTask(trailersAdapter).execute(movieItem.getId() + "", getResources().getString(R.string.Api_key));
//Get Reviews into list
            ListView reviewsList = (ListView) view.findViewById(R.id.reviewsList);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), new ArrayList<ReviewItem>());
            reviewsList.setAdapter(reviewsAdapter);
            new ReviewsAsyncTask(reviewsAdapter).execute(movieItem.getId() + "", getResources().getString(R.string.Api_key));
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    boolean fav = prefs.getBoolean(movieItem.getId() + "", false);
                    setFavourite(!fav);

                }
            });
            loadFavourite();

        }
        return view;
    }

    private void loadFavourite() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean FavOrNot = prefs.getBoolean(movieItem.getId() + "", false);
        Log.i("loadFavourite", FavOrNot + ": " + movieItem.getId());

        if (FavOrNot) {
            favorite.setBackgroundResource(R.drawable.fav_h);
        } else
            favorite.setBackgroundResource(R.drawable.fav);

    }

    private void setFavourite(boolean FavOrNot) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(movieItem.getId() + "", FavOrNot);
        editor.commit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean f = prefs.getBoolean(movieItem.getId() + "", false);
        if (FavOrNot) {
            long id = mDbHelper.addToFavourites(movieItem);
            favorite.setBackgroundResource(R.drawable.fav_h);
        } else {
            boolean id = mDbHelper.removeFromFavourites(movieItem.getId());
            favorite.setBackgroundResource(R.drawable.fav);

        }


    }
}
