package organizesports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.models.DataModel;
import com.app.organizesports.utils.SharedData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<DataModel> mData = new ArrayList<>();
    private DataListener mListener;
    private Context context;

    public DataAdapter(ArrayList<DataModel> data, DataListener userListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = userListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_data, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.description.setText(mData.get(position).getDescription());

        if (!TextUtils.isEmpty(mData.get(position).getIcon())) {
            Picasso.get()
                    .load(mData.get(position).getIcon())
                    .into(holder.image);
        }
        holder.adminButtons.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<DataModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(DataModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<DataModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView image;
        TextView title, description;
        ImageButton delete, edit;
        LinearLayout adminButtons;
        DataListener listener;
        ViewHolder(View itemView, DataListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            adminButtons = itemView.findViewById(R.id.admin_buttons);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

            this.listener = listener;

            view.setOnClickListener(v -> listener.view(getBindingAdapterPosition()));
            delete.setOnClickListener(v -> listener.deleteItem(getBindingAdapterPosition()));
            edit.setOnClickListener(v -> listener.editItem(getBindingAdapterPosition()));
        }
    }

    public interface DataListener {
        void view(int position);
        void deleteItem(int position);
        void editItem(int position);

    }
}
