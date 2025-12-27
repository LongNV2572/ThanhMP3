package com.example.baseproduct.ui.admin.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.baseproduct.R;
import com.example.baseproduct.databinding.ItemMusicBinding;
import com.example.baseproduct.databinding.PopupMoreAdminBinding;
import com.example.baseproduct.model.MusicModel;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminAdapter extends RecyclerView.Adapter<HomeAdminAdapter.MusicViewHolder> {

    private final List<MusicModel> musicList;
    private final OnMusicClickListener listener;

    public HomeAdminAdapter(OnMusicClickListener listener) {
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

            Context context = binding.getRoot().getContext();
            if (model.getImage().isEmpty()){
                Glide.with(context).load(R.drawable.ic_default_music).into(binding.ivAvatar);
            }else  {
                Glide.with(context).load(model.getImage()).into(binding.ivAvatar);
            }

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
        PopupMoreAdminBinding binding = PopupMoreAdminBinding.inflate(inflater);

        PopupWindow popupWindow = new PopupWindow(
                binding.getRoot(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Cho phép click ngoài để dismiss
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Đo kích thước popup
        binding.getRoot().measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int popupHeight = binding.getRoot().getMeasuredHeight();

        // Lấy vị trí của anchorView trên màn hình
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorY = location[1];
        int anchorHeight = anchorView.getHeight();

        // Lấy chiều cao màn hình
        DisplayMetrics displayMetrics = anchorView.getContext().getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        // Tính khoảng trống phía dưới
        int spaceBelow = screenHeight - (anchorY + anchorHeight);

        // Nếu không đủ chỗ bên dưới, hiển thị bên trên
        if (spaceBelow < popupHeight) {
            // Show phía trên anchorView
            popupWindow.showAsDropDown(anchorView, 0, -(anchorHeight + popupHeight + 8));
        } else {
            // Show bên dưới anchorView như bình thường
            popupWindow.showAsDropDown(anchorView, 0, 8);
        }

        binding.btnEdit.setOnClickListener(v -> {
            popupWindow.dismiss();
            listener.onEdit(item);
        });

        binding.btnDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            listener.onDelete(item);
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

        void onEdit(MusicModel music);

        void onDelete(MusicModel music);
    }
}
