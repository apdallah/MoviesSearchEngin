package apdallah.moivedb.com.moivedb;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;

/**
 * Created by Apdallah on 3/25/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        if (savedInstanceState == null) {

            DetailsFragment fragment = new DetailsFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

            getSupportActionBar().setTitle("Moive Details");

    }
}
