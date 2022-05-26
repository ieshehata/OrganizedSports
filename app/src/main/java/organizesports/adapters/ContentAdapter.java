package organizesports.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.models.ElementModel;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private ArrayList<ElementModel> mData = new ArrayList<>();
    private Context context;

    public ContentAdapter(ArrayList<ElementModel> data) {
        mData.clear();
        this.mData.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_content, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int type = mData.get(position).getType();
        boolean contentAvailable = mData.get(position).getContent().size() > 0;

        if(type == 0 && contentAvailable) { //Title
            holder.title.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.sep.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

            holder.title.setText(mData.get(position).getContent().get(0));
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if(type == 1 && contentAvailable) { //Content
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.sep.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

            holder.content.setText(mData.get(position).getContent().get(0));
        } else if(type == 2 && contentAvailable) { //Image
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.sep.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(mData.get(position).getContent().get(0))) {
                Picasso.get()
                        .load(mData.get(position).getContent().get(0))
                        .into(holder.image);
            }
        } else if(type == 3) { //Sep
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.sep.setVisibility(View.VISIBLE);
            holder.youTubePlayerView.setVisibility(View.GONE);
        } else if(type == 4) { //Youtube
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.sep.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

            holder.videoId = mData.get(position).getContent().get(0);
            if(holder.youTubePlayer != null) {
                holder.youTubePlayer.loadVideo(holder.videoId, 0);
            }else {
                holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayerNow) {
                        holder.youTubePlayer = youTubePlayerNow;
                        holder.youTubePlayer.loadVideo(holder.videoId, 0);
                        holder.youTubePlayerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        holder.youTubePlayerView.setVisibility(View.GONE);
                        holder.videoId = "";
                        super.onError(youTubePlayer, error);
                    }
                });
            }
        }else {
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.sep.setVisibility(View.GONE);
            holder.youTubePlayerView.setVisibility(View.GONE);

        }



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<ElementModel> getData() {
        return this.mData;
    }

    public void remove(int position) {
        notifyItemRemoved(position);
    }

    public void restore(ElementModel item, int position) {
        notifyItemInserted(position);
    }

    public void updateData(ArrayList<ElementModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view, sep;
        ImageView image;
        TextView title, content;
        YouTubePlayerView youTubePlayerView;
        YouTubePlayer youTubePlayer;
        String videoId = "";

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            sep = itemView.findViewById(R.id.sep);
            youTubePlayerView = itemView.findViewById(R.id.player);

        }
    }
}
