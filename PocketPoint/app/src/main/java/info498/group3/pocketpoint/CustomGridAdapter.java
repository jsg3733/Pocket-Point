package info498.group3.pocketpoint;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



// is making the customGridAdapter for the icons
public class CustomGridAdapter extends BaseAdapter {

    private Context context;
    int layoutResourceId;
    private List<Icon> items;

    public CustomGridAdapter(Context context, int layoutResourceId, List<Icon> items) {
        this.context = context;
        this.items = items;
        this.layoutResourceId = layoutResourceId;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // is reseting the view to have the images
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        IconHolder holder = null;

        if (row == null) {
            LayoutInflater inflater1 = ((Activity)context).getLayoutInflater();
            row = inflater1.inflate(layoutResourceId, parent, false);

            // is setting the image view on title for the given iconholder defined below
            holder = new IconHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

            // sets the given holder to the inflater
            row.setTag(holder);

        }else {
            holder = (IconHolder)row.getTag();

        }
        Icon icon = items.get(position);
        holder.txtTitle.setText(icon.getTitle());
        // positive value indicates png in drawable folder
        if (icon.getIcon() > 0){
            holder.imgIcon.setImageResource(icon.getIcon());
        } else{//negative value indicates bitmap version
            holder.imgIcon.setImageBitmap(icon.getBitmap());
        }
        return row;
    }

    static class IconHolder {
        ImageView imgIcon;
        TextView txtTitle;

    }

}