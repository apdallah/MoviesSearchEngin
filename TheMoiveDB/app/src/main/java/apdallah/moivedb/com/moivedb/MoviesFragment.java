package apdallah.moivedb.com.moivedb;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import apdallah.moivedb.com.moivedb.Adapters.MoivesAdapter;
import apdallah.moivedb.com.moivedb.Items.MovieItem;
import apdallah.moivedb.com.moivedb.MoiveDataBase.MovieDbHelper;
import apdallah.moivedb.com.moivedb.Tasks.MoivesAsyncTask;


public class MoviesFragment extends Fragment implements Serializable {

    private GridView moivesGridView;
    private MoivesAdapter adapter;
    private MovieDbHelper mDbHelper;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public boolean UpdateWithSortOrder() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String selection = prefs.getString(getResources().getString(R.string.pref_selection_key), getString(R.string.pref_most_val));
        Log.i("SELECTION", "Selection : " + selection);
        if (selection.equals(getString(R.string.pref_fav_val))) {
            updateWithFavourites();
            return true;
        } else {
            if (isNetworkConnected()) {
                new MoivesAsyncTask(adapter, getActivity()).execute(selection, getResources().getString(R.string.Api_key));
                return true;
            } else {
                Toast.makeText(getActivity(), "No InternetConnection !!", Toast.LENGTH_LONG).show();
                return false;

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mDbHelper = new MovieDbHelper(getActivity());
        ArrayList<MovieItem> movieItems = new ArrayList<>();
        moivesGridView = (GridView) view.findViewById(R.id.moives_grid_view);
        adapter = new MoivesAdapter(getActivity(), movieItems);
        moivesGridView.setAdapter(adapter);
        moivesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((OnItemSelectedCallBack) getActivity())
                        .onItemSelected(adapter.getItem(position));
             }
        });

        UpdateWithSortOrder();

        return view;
    }



    @Override
    public void onResume() {


        super.onResume();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public boolean updateWithFavourites() {
        ArrayList<MovieItem> favouriteItems = mDbHelper.getFavourites();
        if (favouriteItems.size() != 0) {
            //set selection to favourites in shared prefrance
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.pref_selection_key), getString(R.string.pref_fav_val));
            editor.commit();
            //set adapter and notfiy data changed
            adapter.setMovieItems(favouriteItems);
            adapter.notifyDataSetChanged();
            //update action bar
//            getActivity().getActionBar().setTitle(getString(R.string.pref_fav_label));
            if (MoviesActivity.mTwoPane)
                ((MoviesFragment.OnItemSelectedCallBack) getActivity())
                        .onItemSelected(adapter.getItem(0));
            return true;
        } else {
            Toast.makeText(getActivity(), "Nothing added to Favourite", Toast.LENGTH_LONG).show();
            return false;
        }


    }

    public interface OnItemSelectedCallBack {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MovieItem dataItem);
    }

}
