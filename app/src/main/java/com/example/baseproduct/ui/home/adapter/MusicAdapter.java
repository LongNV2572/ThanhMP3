package com.example.baseproduct.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproduct.call_api.model.MusicModel;
import com.example.baseproduct.databinding.ItemMusicBinding;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private final List<MusicModel> musicList;
    private final OnMusicClickListener listener;

    public MusicAdapter(OnMusicClickListener listener) {
        this.musicList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMusicBinding binding = ItemMusicBinding.inflate(inflater, parent, false);
        return new MusicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bind(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {

        private final ItemMusicBinding binding;

        MusicViewHolder(ItemMusicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MusicModel model) {
            binding.tvName.setText(model.getName());
            binding.tvSinger.setText(model.getSinger());

            Glide.with(binding.getRoot().getContext()).load(model.getImage()).into(binding.ivAvatar);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(model);
                }
            });
        }
    }

    // Method cập nhật dữ liệu - cách đơn giản
    public void addListData(List<MusicModel> newList) {
        if (newList != null) {
            musicList.clear();
            musicList.addAll(newList);
            notifyDataSetChanged();
        }
    }

    // Method cập nhật dữ liệu - với DiffUtil (hiệu suất tốt hơn)
    public void updateMusicList(List<MusicModel> newList) {
        if (newList == null) return;

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return musicList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return musicList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                MusicModel oldMusic = musicList.get(oldItemPosition);
                MusicModel newMusic = newList.get(newItemPosition);
                return oldMusic.getName().equals(newMusic.getName()) && oldMusic.getSinger().equals(newMusic.getSinger());
            }
        });

        musicList.clear();
        musicList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OnMusicClickListener {
        void onClick(MusicModel music);
    }
}
