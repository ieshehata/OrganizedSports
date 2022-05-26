package organizesports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.models.ExerciseTypeModel;
import com.app.organizesports.utils.SharedData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExerciseTypeAdapter extends RecyclerView.Adapter<ExerciseTypeAdapter.ViewHolder> {
    private ArrayList<ExerciseTypeModel> mData = new ArrayList<>();
    private ExerciseTypeListener mListener;
    private Context context;

    public ExerciseTypeAdapter(ArrayList<ExerciseTypeModel> data, ExerciseTypeListener userListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = userListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_exercise_type, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());

        if (!TextUtils.isEmpty(mData.get(position).getIcon())) {
            Picasso.get()
                    .load(mData.get(position).getIcon())
                    .into(holder.icon);
        }
        holder.delete.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<ExerciseTypeModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(ExerciseTypeModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<ExerciseTypeModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView icon;
        TextView title;
        ImageButton delete;

        ExerciseTypeListener listener;
        ViewHolder(View itemView, ExerciseTypeListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            delete = itemView.findViewById(R.id.delete);
            this.listener = listener;

            view.setOnClickListener(v -> listener.view(getBindingAdapterPosition()));

            delete.setOnClickListener(v -> listener.deleteItem(getBindingAdapterPosition()));
        }
    }

    public interface ExerciseTypeListener {
        void view(int position);
        void deleteItem(int position);
    }
}
