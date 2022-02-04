package com.app.organizedsports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizedsports.R;
import com.app.organizedsports.model.TipsModel;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.utils.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder>{
    private ArrayList<TipsModel> mData = new ArrayList<>();
    private TipsAdapter.TipListener mTipListener;
    private Context context;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");

    public TipsAdapter(ArrayList<TipsModel> data, TipListener tipListener) {
        mData.clear();
        this.mData.addAll(data);
        this.mTipListener = tipListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_tip, parent, false);
        return new ViewHolder(view,mTipListener);
    }
    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tip.setText(mData.get(position).getTips());
        holder.data.setText((CharSequence) mData.get(position).getCreatedAt());

    }




    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<TipsModel> getData() {
        return this.mData;
    }

    public void updateData(ArrayList<TipsModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        LinearLayout actionButtons;
        TextView tip,  data;
        ImageButton delete;
        TipsAdapter.TipListener listener;

        ViewHolder(View itemView, TipListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            actionButtons = itemView.findViewById(R.id.action_buttons);
            tip = itemView.findViewById(R.id.tip);
            data = itemView.findViewById(R.id.date);

            this.listener = listener;

            view.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });



            delete.setOnClickListener(v -> listener.deleteItem(getAdapterPosition()));
        }
    }

    public interface TipListener {
        void view(int position);
        void deleteItem(int position);
    }

}
