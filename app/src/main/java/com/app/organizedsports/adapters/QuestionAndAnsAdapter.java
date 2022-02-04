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
import com.app.organizedsports.model.QuestionAndAnsModel;

import java.util.ArrayList;

public class QuestionAndAnsAdapter extends RecyclerView.Adapter<QuestionAndAnsAdapter.ViewHolder> {
    private ArrayList<QuestionAndAnsModel> mData;
    private QuestionAndAnsListener mQuestionAndAnsListener;
    private Context context;

    public QuestionAndAnsAdapter(ArrayList<QuestionAndAnsModel> data, QuestionAndAnsListener questionAndAnsListener) {
        this.mData = data;
        this.mQuestionAndAnsListener = questionAndAnsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_question_and_ans, parent, false);
        return new ViewHolder(view, mQuestionAndAnsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getAns());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<QuestionAndAnsModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        //mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restore(QuestionAndAnsModel item, int position) {
        //mData.add(position, item);
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<QuestionAndAnsModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView name;
        ImageButton delete;
        QuestionAndAnsListener listener;
        ViewHolder(View itemView, QuestionAndAnsListener listener) {
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

    public interface QuestionAndAnsListener {
        void onClick(int position);
        void deleteItem(int position);

    }
}
