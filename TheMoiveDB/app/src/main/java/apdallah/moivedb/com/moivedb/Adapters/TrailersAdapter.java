package apdallah.moivedb.com.moivedb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import apdallah.moivedb.com.moivedb.Items.TrailersItem;
import apdallah.moivedb.com.moivedb.R;

/**
 * Created by Apdallah on 3/24/2016.
 */
public class TrailersAdapter extends BaseAdapter {
    private Context context;
     private  ArrayList<TrailersItem> trailersItems;
    Inflater inflater;
    public TrailersAdapter(Context contx, ArrayList<TrailersItem> trailers) {
        this.context = contx;
        this.trailersItems=trailers;
    }

    public void setTrailerItems(ArrayList<TrailersItem> moiveItems) {
        this.trailersItems.clear();
        this.trailersItems.addAll(moiveItems);
    }

    @Override
    public int getCount() {
        return trailersItems.size();
    }

    @Override
    public TrailersItem getItem(int position) {
        return trailersItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
  static class ViewHolder{
    ImageView imageView;
      TextView titileView;
}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        if (convertView == null) {
                convertView=LayoutInflater.from(context).inflate(R.layout.trailer_item,parent,false);
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.thum);
            viewHolder.titileView=(TextView)convertView.findViewById(R.id.trailer_titile);

            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.titileView.setText(trailersItems.get(position).getTitile());


        return convertView;

    }


}
