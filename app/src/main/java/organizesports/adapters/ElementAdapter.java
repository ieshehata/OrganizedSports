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
import com.app.organizesports.activities.admin.DataElementEditorActivity;
import com.app.organizesports.models.ElementModel;
import com.app.organizesports.utils.SharedData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> {
    private ArrayList<ElementModel> mData = new ArrayList<>();
    private ElementListener mListener;
    private Context context;

    public ElementAdapter(ArrayList<ElementModel> data, ElementListener userListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = userListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_element, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int type = mData.get(position).getType();
        holder.type.setText(DataElementEditorActivity.types.get(type).getText());
        boolean contentAvailable = mData.get(position).getContent().size() > 0;
        if(type == 0 && contentAvailable) { //Title
            holder.content.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            holder.content.setText(mData.get(position).getContent().get(0));
        } else if(type == 1 && contentAvailable) { //Content
            holder.content.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            holder.content.setText(mData.get(position).getContent().get(0));
        } else if(type == 2 && contentAvailable) { //Image
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(mData.get(position).getContent().get(0))) {
                Picasso.get()
                        .load(mData.get(position).getContent().get(0))
                        .into(holder.image);
            }
        } else if(type == 3) { //Sep
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        } else if(type == 4) { //Youtube
            holder.content.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            holder.content.setText("https://youtu.be/"  + mData.get(position).getContent().get(0));
        } else {
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        }


        holder.adminButtons.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<ElementModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(ElementModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<ElementModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView image;
        TextView type, content;
        ImageButton delete, edit;
        LinearLayout adminButtons;
        ElementListener listener;
        ViewHolder(View itemView, ElementListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            type = itemView.findViewById(R.id.type);
            content = itemView.findViewById(R.id.content);
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

    public interface ElementListener {
        void view(int position);
        void deleteItem(int position);
        void editItem(int position);

    }
}
