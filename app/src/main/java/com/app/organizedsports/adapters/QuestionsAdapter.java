package com.app.organizedsports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizedsports.R;
import com.app.organizedsports.model.QuestionsModel;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private ArrayList<QuestionsModel> mData;
    private QuestionsListener mQuestionsListener;
    private Context context;

    public QuestionsAdapter(ArrayList<QuestionsModel> data, QuestionsListener questionsListener) {
        this.mData = data;
        this.mQuestionsListener = questionsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_questions, parent, false);
        return new ViewHolder(view, mQuestionsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<QuestionsModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(QuestionsModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<QuestionsModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView name;
        ImageButton delete;
        QuestionsListener listener;
        ViewHolder(View itemView, QuestionsListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            name = itemView.findViewById(R.id.name);
            delete = itemView.findViewById(R.id.delete);
            this.listener = listener;
            view.setOnClickListener(this);
            delete.setOnClickListener(v -> listener.deleteItem(getAdapterPosition()));
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    public interface QuestionsListener {
        void onClick(int position);
        void deleteItem(int position);

    }
}
