package com.test.videoplayer.Fragments;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.videoplayer.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VideoListRecyclerViewAdapter extends RecyclerView.Adapter<VideoListRecyclerViewAdapter.ViewHolder> {

    private final List<VideoItem> videos;
    private final LayoutInflater inflater;
    private VideoItemClickListener itemClickListener;

    public VideoListRecyclerViewAdapter(Context context, List<VideoItem> items) {
        inflater = LayoutInflater.from(context);
        videos = items;
    }

    public void setVideoItemClickListener(VideoItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_video_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String title = videos.get(position).title;

        holder.titleText.setText(title);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView titleText;

        public ViewHolder(View binding) {
            super(binding);
            titleText = binding.findViewById(R.id.title_video);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onVideoItemClick(videos.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    public interface VideoItemClickListener {
        void onVideoItemClick(VideoItem item);
    }

    public static class VideoItem implements Parcelable {
        public final String title;
        public final String uri;

        public VideoItem(String title, String uri) {
            this.title = title;
            this.uri = uri;
        }

        protected VideoItem(Parcel in) {
            title = in.readString();
            uri = in.readString();
        }

        public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
            @Override
            public VideoItem createFromParcel(Parcel in) {
                return new VideoItem(in);
            }

            @Override
            public VideoItem[] newArray(int size) {
                return new VideoItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(uri);
        }
    }
}