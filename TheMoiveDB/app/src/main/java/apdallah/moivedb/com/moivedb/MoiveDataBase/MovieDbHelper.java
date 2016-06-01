package apdallah.moivedb.com.moivedb.MoiveDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import apdallah.moivedb.com.moivedb.Items.MovieItem;

/**
 * Created by Apdallah on 3/30/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + MovieContract.FavouriteEntry.TABLE_NAME + " (" +
                MovieContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.FavouriteEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieContract.FavouriteEntry.COLUMN_RATE + " REAL NOT NULL, " +
                MovieContract.FavouriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavouriteEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavouriteEntry.COLUMN_TITTLE + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<MovieItem> getFavourites() {
        ArrayList<MovieItem> FavouriteMoives = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor favCursor = db.query(MovieContract.FavouriteEntry.TABLE_NAME, null, null, null, null, null, null);
        favCursor.moveToFirst();
        for (int i = 0; i < favCursor.getCount(); i++) {
            MovieItem item = new MovieItem();
            item.setId(favCursor.getInt(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_ID)));
            item.setOverView(favCursor.getString(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_OVERVIEW)));
            item.setPostarPath(favCursor.getString(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_POSTER)));
            item.setRate(favCursor.getDouble(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_RATE)));
            item.setTitle(favCursor.getString(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_TITTLE)));
            item.setRelease_date(favCursor.getString(favCursor.getColumnIndex(MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE)));
            FavouriteMoives.add(item);
            favCursor.moveToNext();
        }
        favCursor.close();

        return FavouriteMoives;
    }

    public long addToFavourites(MovieItem movieItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues favouriteValues = new ContentValues();
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_ID, movieItem.getId());
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_OVERVIEW, movieItem.getOverView());
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_POSTER, movieItem.getPostarPath());
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_RATE, movieItem.getRate());
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE, movieItem.getRelease_date());
        favouriteValues.put(MovieContract.FavouriteEntry.COLUMN_TITTLE, movieItem.getTitle());
        long added = db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, favouriteValues);

        return added;
    }

    public boolean removeFromFavourites(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(MovieContract.FavouriteEntry.TABLE_NAME, MovieContract.FavouriteEntry.COLUMN_ID + "=" + id, null);
        if (delete == 1) return true;
        else return false;
    }
}
