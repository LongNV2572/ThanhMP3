package com.example.baseproduct.ui.both.edit;

import android.widget.Toast;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityMusicEditBinding;
import com.example.baseproduct.model.MusicModel;
import com.example.baseproduct.util.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicEditActivity extends BaseActivity<ActivityMusicEditBinding> {
    @Override
    public ActivityMusicEditBinding getBinding() {
        return ActivityMusicEditBinding.inflate(getLayoutInflater());
    }

    MusicModel itemMusic;

    @Override
    public void initView() {
        super.initView();

        itemMusic = (MusicModel) getIntent().getSerializableExtra("data");

        binding.tvCategory.setText(itemMusic.getCategory());
        binding.tvName.setText(itemMusic.getName());
        binding.tvSinger.setText(itemMusic.getSinger());
        binding.tvImage.setText(itemMusic.getImage());
        binding.tvLink.setText(itemMusic.getLink());
    }

    @Override
    public void bindView() {
        super.bindView();

        binding.ivBack.setOnClickListener(v -> {
            onBack();
        });

        binding.tvAdd.setOnClickListener(v -> {
            Utils.hideKeyboard(this);
            String category = binding.tvCategory.getText().toString().trim();
            String name = binding.tvName.getText().toString().trim();
            String singer = binding.tvSinger.getText().toString().trim();
            String image = binding.tvImage.getText().toString().trim();
            String link = binding.tvLink.getText().toString().trim();

            if (category.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền thể loại", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền tên bài hát", Toast.LENGTH_SHORT).show();
                return;
            }

            if (singer.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền tên ca sĩ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (image.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền ảnh bài hát", Toast.LENGTH_SHORT).show();
                return;
            }

            if (link.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền link bài hát", Toast.LENGTH_SHORT).show();
                return;
            }

            itemMusic.setCategory(category);
            itemMusic.setName(name);
            itemMusic.setSinger(singer);
            itemMusic.setImage(image);
            itemMusic.setLink(link);

            DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("list_music");
            musicRef.child(itemMusic.getMusicId()).setValue(itemMusic).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            onBack();
        });
    }
}
