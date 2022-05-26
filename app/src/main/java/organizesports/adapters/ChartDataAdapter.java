package organizesports.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.app.organizesports.models.listviewitems.ChartItem;

import java.util.List;

public class ChartDataAdapter extends ArrayAdapter<ChartItem> {
    List<ChartItem> objects;

    public ChartDataAdapter(Context context, List<ChartItem> objects) {
        super(context, 0, objects);
        this.objects = objects;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        //noinspection ConstantConditions
        return getItem(position).getView(position, convertView, getContext());
    }

    @Override
    public int getItemViewType(int position) {
        // return the views type
        ChartItem ci = getItem(position);
        return ci != null ? ci.getItemType() : 0;
    }

    @Override
    public int getViewTypeCount() {
        return objects.size();
    }
}