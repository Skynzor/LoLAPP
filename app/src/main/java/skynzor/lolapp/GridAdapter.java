package skynzor.lolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Primularose on 12-1-2018.
 */

public class GridAdapter extends BaseAdapter {

    private Integer img[];
    private String name[];
    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(Context context, Integer img[], String name[])
    {

        this.context = context;
        this.img = img;
        this.name = name;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;

        if(convertView == null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custom_layout, null);

        }
        ImageView image = (ImageView) gridView.findViewById(R.id.images);
        TextView champNames = (TextView) gridView.findViewById(R.id.championNames);

        image.setImageResource(img[position]);
        champNames.setText(name[position]);


        return gridView;
    }
}
