package apdallah.moivedb.com.moivedb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

import apdallah.moivedb.com.moivedb.Items.MovieItem;
import apdallah.moivedb.com.moivedb.R;

/**
 * Created by Apdallah on 3/24/2016.
 */
public class MoivesAdapter extends BaseAdapter {
    private Context context;
    private final String PIC_BASE_PATH = "http://image.tmdb.org/t/p/w500";
    private ArrayList<MovieItem> movieItems;
    Inflater inflater;

    public MoivesAdapter(Context contx, ArrayList<MovieItem> moive_Items) {
        this.context = contx;
        this.movieItems = moive_Items;
    }

    public void setMovieItems(ArrayList<MovieItem> movieItems) {
        this.movieItems.clear();
        this.movieItems.addAll(movieItems);
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public MovieItem getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.poster);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(PIC_BASE_PATH + movieItems.get(position).getPostarPath()).into(viewHolder.imageView);

        return convertView;

    }


}
