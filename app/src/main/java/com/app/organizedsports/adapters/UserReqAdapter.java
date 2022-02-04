package com.app.organizedsports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizedsports.R;
import com.app.organizedsports.model.UsersReqModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserReqAdapter extends RecyclerView.Adapter<UserReqAdapter.ViewHolder>{
    private ArrayList<UsersReqModel> mData = new ArrayList<>();
    private UserReqListener mListener;
    private Context context;
    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
    @SuppressLint("SimpleDateFormat")

    public UserReqAdapter(ArrayList<UsersReqModel> data, UserReqListener listener) {
        mData.clear();
        this.mData.addAll(data);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_user_request, parent, false);
        return new ViewHolder(view, mListener);
    }

    @SuppressLint({"UseCompatTextViewDrawableApis", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getUser().getName());
        holder.email.setText(mData.get(position).getUser().getEmail());
        holder.phone.setText(mData.get(position).getUser().getPhone());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<UsersReqModel> getData() {
        return this.mData;
    }

    public void updateData(ArrayList<UsersReqModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView date, name, email,  phone;
        Button acceptButton, rejectButton, show;
        UserReqListener mListener;
        ViewHolder(View itemView, UserReqListener listener) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            acceptButton = itemView.findViewById(R.id.accept);
            rejectButton = itemView.findViewById(R.id.reject);
            show = itemView.findViewById(R.id.show);

            this.mListener = listener;

            view.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });

            show.setOnClickListener(v -> {
                listener.view(getAdapterPosition());
            });

            acceptButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), true);
            });

            rejectButton.setOnClickListener(v -> {
                listener.response(getAdapterPosition(), false);
            });
        }
    }

    public interface UserReqListener {
        void response(int position, boolean isAccepted);
        void view(int position);
    }
}
