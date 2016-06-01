package apdallah.moivedb.com.moivedb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import apdallah.moivedb.com.moivedb.Items.ReviewItem;
import apdallah.moivedb.com.moivedb.R;

/**
 * Created by Apdallah on 3/24/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ReviewItem> reviewItems;
    Inflater inflater;

    public ReviewsAdapter(Context contx, ArrayList<ReviewItem> moive_Items) {
        this.context = contx;
        this.reviewItems = moive_Items;
    }

    public void setMoiveItems(ArrayList<ReviewItem> moiveItems) {
        this.reviewItems.clear();
        this.reviewItems.addAll(moiveItems);
    }

    @Override
    public int getCount() {
        return reviewItems.size();
    }

    @Override
    public ReviewItem getItem(int position) {
        return reviewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView titileView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reivew_item, parent, false);
            viewHolder.titileView = (TextView) convertView.findViewById(R.id.review_cont);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titileView.setText(reviewItems.get(position).getContent());
        return convertView;

    }


}
