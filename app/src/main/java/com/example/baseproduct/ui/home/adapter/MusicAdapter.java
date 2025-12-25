package com.example.baseproduct.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproduct.call_api.model.MusicModel;
import com.example.baseproduct.databinding.ItemMusicBinding;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private final List<MusicModel> musicList;
    private final OnMusicClickListener listener;

    public MusicAdapter(List<MusicModel> musicList, OnMusicClickListener listener) {
        this.musicList = musicList;
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

    public void addListData(List<MusicModel> newList) {
        musicList.clear();
        musicList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnMusicClickListener {
        void onClick(MusicModel music);
    }
}
