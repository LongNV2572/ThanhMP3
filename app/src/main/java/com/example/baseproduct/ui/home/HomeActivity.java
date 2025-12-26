package com.example.baseproduct.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.model.MusicModel;
import com.example.baseproduct.databinding.ActivityHomeBinding;
import com.example.baseproduct.dialog.exit.ExitAppDialog;
import com.example.baseproduct.ui.home.adapter.MusicAdapter;
import com.example.baseproduct.ui.play.PlayActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    private MusicAdapter musicAdapter;
    private final List<MusicModel> listMusic = new ArrayList<>();

    @Override
    public void initView() {
        musicAdapter = new MusicAdapter(music -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", music);
            startNextActivity(PlayActivity.class, bundle);
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
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                List<MusicModel> listSearch = new ArrayList<>();
                String edtSearch = binding.edtSearch.getText().toString().trim();
                if (edtSearch.equals("")) {
                    listSearch.addAll(listMusic);
                } else {
                    for (int i = 0; i < listMusic.size(); i++) {
                        MusicModel item = listMusic.get(i);
                        if (item.getName().contains(edtSearch) || item.getSinger().contains(edtSearch)) {
                            listSearch.add(listMusic.get(i));
                        }
                    }
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

    @Override
    public void onBack() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this);
        exitAppDialog.init(this::finishAffinity);
        exitAppDialog.show();
    }
}
