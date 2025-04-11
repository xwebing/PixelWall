package com.pixelwall.pixelwall.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pixelwall.pixelwall.R;
import com.pixelwall.pixelwall.model.Wallpaper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder> {
    private List<Wallpaper> wallpapers = new ArrayList<>();
    private final OnWallpaperClickListener listener;

    public interface OnWallpaperClickListener {
        void onWallpaperClick(Wallpaper wallpaper, int position);
    }

    public WallpaperAdapter(OnWallpaperClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wallpaper, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        try {
            Wallpaper wallpaper = wallpapers.get(position);
            if (wallpaper != null && wallpaper.getImageUrl() != null) {
                Glide.with(holder.itemView.getContext())
                        .load(wallpaper.getImageUrl())
                        .placeholder(R.drawable.placeholder) // 占位图
                        .error(R.drawable.error) // 错误图
                        .centerCrop()
                        .into(holder.wallpaperImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return wallpapers.size();
    }

    public void setWallpapers(List<Wallpaper> wallpapers) {
        this.wallpapers = wallpapers;
        notifyDataSetChanged();
    }

    public void addWallpapers(List<Wallpaper> newWallpapers) {
        if (newWallpapers != null && !newWallpapers.isEmpty()) {
            int startPosition = wallpapers.size();
            wallpapers.addAll(newWallpapers);
            notifyItemRangeInserted(startPosition, newWallpapers.size());
        }
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder {
        ImageView wallpaperImage;

        WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            wallpaperImage = itemView.findViewById(R.id.wallpaperImage);
            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onWallpaperClick(wallpapers.get(position), position);
                }
            });
        }
    }
}