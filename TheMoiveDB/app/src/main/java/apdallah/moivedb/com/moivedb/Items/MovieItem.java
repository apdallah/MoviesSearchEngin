package apdallah.moivedb.com.moivedb.Items;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Apdallah on 3/24/2016.
 */
public class MovieItem implements Serializable{

    private String postarPath;
    private String overView;
    private String release_date;
    private String title;
    private int id;
    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostarPath() {
        return postarPath;
    }

    public void setPostarPath(String postarPath) {
        this.postarPath = postarPath;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
