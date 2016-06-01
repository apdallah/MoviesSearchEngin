package apdallah.moivedb.com.moivedb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import apdallah.moivedb.com.moivedb.Items.MovieItem;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.OnItemSelectedCallBack, Serializable {

    private final String FRAGMENT_TAG = "moives";
    private String sortOrder = "";
   public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DetailsFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortOrder = prefs.getString(getResources().getString(R.string.pref_selection_key), getResources().getString(R.string.pref_most_val));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(MoviesActivity.this, PrefSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_favourit) {

            MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
            if (fragment.updateWithFavourites())
                getSupportActionBar().setTitle(getString(R.string.pref_fav_label));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String newSortOrder = prefs.getString(getResources().getString(R.string.pref_selection_key), getResources().getString(R.string.pref_most_val));
        if (null != newSortOrder && newSortOrder != sortOrder) {
            //if updated with sort order successfully
            if (fragment.UpdateWithSortOrder()) {
                //update actionbar titile
                if (newSortOrder.equals(getResources().getString(R.string.pref_most_val)))
                    getSupportActionBar().setTitle(getResources().getString(R.string.pref_most_label));
                else if (newSortOrder.equals(getString(R.string.pref_fav_val)))
                    getSupportActionBar().setTitle(getResources().getString(R.string.pref_fav_label));
                else if (newSortOrder.equals(getString(R.string.pref_top_val)))
                    getSupportActionBar().setTitle(getResources().getString(R.string.pref_top_label));
            }
        } else {
            //update actionbar titile
            if (newSortOrder.equals(getResources().getString(R.string.pref_most_val)))
                getSupportActionBar().setTitle(getResources().getString(R.string.pref_most_label));
            else if (newSortOrder.equals(getString(R.string.pref_fav_val)))
                getSupportActionBar().setTitle(getResources().getString(R.string.pref_fav_label));
            else if (newSortOrder.equals(getString(R.string.pref_top_val)))
                getSupportActionBar().setTitle(getResources().getString(R.string.pref_top_label));
        }


        super.onResume();
    }



    @Override
    public void onItemSelected(MovieItem dataItem) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putSerializable("MovieItem", dataItem);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(MoviesActivity.this, DetailsActivity.class);
            intent.putExtra("MovieItem", dataItem);
            startActivity(intent);
        }
    }
}
