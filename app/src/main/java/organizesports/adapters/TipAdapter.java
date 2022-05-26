package organizesports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.activities.admin.TipEditorActivity;
import com.app.organizesports.models.TipModel;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {
    private ArrayList<TipModel> mData = new ArrayList<>();
    private Context context;

    public TipAdapter(ArrayList<TipModel> data) {
        mData.clear();
        this.mData.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_tip, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.content.setText(mData.get(position).getText());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<TipModel> getData() {
        return this.mData;
    }

    public void updateData(ArrayList<TipModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title, content;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);

            view.setOnClickListener(v -> {
                SharedData.currentTip = mData.get(getAdapterPosition());
                Intent intent = new Intent(context, TipEditorActivity.class);
                context.startActivity(intent);
            });
        }
    }
}
