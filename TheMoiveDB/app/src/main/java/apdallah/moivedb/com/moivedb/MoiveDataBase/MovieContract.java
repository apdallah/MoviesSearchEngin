package apdallah.moivedb.com.moivedb.MoiveDataBase;

import android.provider.BaseColumns;

/**
 * Created by Apdallah on 3/30/2016.
 */
public class MovieContract {
    public static class FavouriteEntry implements BaseColumns {
        public static final String TABLE_NAME="favourite";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_POSTER="poster";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_RATE="rate";
        public static final String COLUMN_RELEASE_DATE ="release_date";
        public static final String COLUMN_TITTLE ="tittle";

    }
}
