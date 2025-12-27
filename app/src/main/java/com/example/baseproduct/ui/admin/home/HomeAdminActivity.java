package com.example.baseproduct.ui.admin.home;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.databinding.ActivityHomeAdminBinding;
import com.example.baseproduct.databinding.DialogDeleteMusicBinding;
import com.example.baseproduct.dialog.exit.ExitAppDialog;
import com.example.baseproduct.model.MusicModel;
import com.example.baseproduct.ui.admin.home.adapter.HomeAdminAdapter;
import com.example.baseproduct.ui.both.add.MusicAddActivity;
import com.example.baseproduct.ui.both.edit.MusicEditActivity;
import com.example.baseproduct.ui.both.infor.InforAcitivity;
import com.example.baseproduct.ui.both.play.PlayActivity;
import com.example.baseproduct.util.Constant;
import com.example.baseproduct.util.SharePrefUtils;
import com.example.baseproduct.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeAdminActivity extends BaseActivity<ActivityHomeAdminBinding> {

    @Override
    public ActivityHomeAdminBinding getBinding() {
        return ActivityHomeAdminBinding.inflate(getLayoutInflater());
    }

    private HomeAdminAdapter musicAdapter;
    private final List<MusicModel> listMusic = new ArrayList<>();


    @Override
    public void initView() {
        musicAdapter = new HomeAdminAdapter(new HomeAdminAdapter.OnMusicClickListener() {
            @Override
            public void onClick(MusicModel music) {
                Utils.hideKeyboard(HomeAdminActivity.this);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", music);
                startNextActivity(PlayActivity.class, bundle);
            }

            @Override
            public void onEdit(MusicModel music) {
                onEditMusic(music);
            }

            @Override
            public void onDelete(MusicModel music) {
                onDeleteMusic(music);
            }
        });
        binding.rcvData.setAdapter(musicAdapter);

        DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("list_music");

        musicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listMusic.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    MusicModel music = child.getValue(MusicModel.class);
                    if (music != null) {
                        String musicId = child.getKey();
                        music.setMusicId(musicId);

                        if (SharePrefUtils.getInt(Constant.ID_DATA, 0) == 0) {
                            SharePrefUtils.putInt(Constant.ID_DATA, listMusic.size() + 1);
                        }
                        listMusic.add(music);
                    }
                }
                musicAdapter.addListData(listMusic);
                Log.d("Firebase", "Size = " + listMusic.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        });

    }

    @Override
    public void bindView() {
        binding.ivInfor.setOnClickListener(v -> {
            Utils.hideKeyboard(this);
            startNextActivity(InforAcitivity.class, null);
        });

        binding.btnAdd.setOnClickListener(v -> {
            Utils.hideKeyboard(this);
            startNextActivity(MusicAddActivity.class, null);
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                List<MusicModel> listSearch = new ArrayList<>();
                String edtSearch = binding.edtSearch.getText().toString().trim().toLowerCase();
                if (edtSearch.isEmpty()) {
                    listSearch.addAll(listMusic);
                } else {
                    for (int i = 0; i < listMusic.size(); i++) {
                        MusicModel item = listMusic.get(i);
                        if (item.getName().toLowerCase().contains(edtSearch) || item.getSinger().toLowerCase().contains(edtSearch)) {
                            listSearch.add(listMusic.get(i));
                        }
                    }
                }
                if (listSearch.isEmpty()){
                    binding.lnEmpty.setVisibility(View.VISIBLE);
                }else  {
                    binding.lnEmpty.setVisibility(View.GONE);
                }
                musicAdapter.addListData(listSearch);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    public void onEditMusic(MusicModel music) {
        Utils.hideKeyboard(HomeAdminActivity.this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", music);
        startNextActivity(MusicEditActivity.class, bundle);
    }

    public void onDeleteMusic(MusicModel music) {
        Dialog dialog = new Dialog(this);
        DialogDeleteMusicBinding bindingDelete = DialogDeleteMusicBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingDelete.getRoot());
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
        bindingDelete.tvCancel.setOnClickListener(v -> dialog.dismiss());

        bindingDelete.tvAgree.setOnClickListener(v -> {
            dialog.dismiss();

            DatabaseReference musicRef = FirebaseDatabase.getInstance().getReference("list_music");

            musicRef.child(music.getMusicId()).removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onBack() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this);
        exitAppDialog.init(this::finishAffinity);
        exitAppDialog.show();
    }
}
