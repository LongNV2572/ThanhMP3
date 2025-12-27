package com.example.baseproduct.ui.user.home.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproduct.R;
import com.example.baseproduct.databinding.ItemMusicBinding;
import com.example.baseproduct.model.MusicModel;

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

            binding.ivMore.setOnClickListener(v -> {
                showMorePopup(binding.ivMore, model);
            });
        }
    }

    private void showMorePopup(View anchorView, MusicModel item) {

        LayoutInflater inflater = LayoutInflater.from(anchorView.getContext());
        View popupView = inflater.inflate(R.layout.popup_more, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Cho phép click ngoài để dismiss
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Show bên dưới ivMore
        popupWindow.showAsDropDown(anchorView, 0, 8);
        View btnAddToAlbum = popupView.findViewById(R.id.btnAddToAlbum);
        View btnDownload = popupView.findViewById(R.id.btnDownload);

        btnAddToAlbum.setOnClickListener(v -> {
            popupWindow.dismiss();
            listener.onAddToAlbum(item);
        });

        btnDownload.setOnClickListener(v -> {
            popupWindow.dismiss();
            listener.onDownload(item);
        });
    }


    public void addListData(List<MusicModel> newList) {
        if (newList != null) {
            musicList.clear();
            musicList.addAll(newList);
            notifyDataSetChanged();
        }
    }

    public interface OnMusicClickListener {
        void onClick(MusicModel music);

        void onAddToAlbum(MusicModel music);

        void onDownload(MusicModel music);
    }
}
