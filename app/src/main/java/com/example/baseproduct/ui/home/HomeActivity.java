package com.example.baseproduct.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.call_api.client.ApiClient;
import com.example.baseproduct.call_api.model.MusicModel;
import com.example.baseproduct.databinding.ActivityHomeBinding;
import com.example.baseproduct.dialog.exit.ExitAppDialog;
import com.example.baseproduct.ui.home.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> {

    @Override
    public ActivityHomeBinding getBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    private MusicAdapter musicAdapter;
    private final List<MusicModel> listMusic = new ArrayList<>();

    @Override
    public void initView() {
        musicAdapter = new MusicAdapter(listMusic, music -> {

        });
        binding.rcvData.setAdapter(musicAdapter);


        ApiClient.getApi().getMusic().enqueue(new Callback<List<MusicModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MusicModel>> call, @NonNull Response<List<MusicModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listMusic.clear();
                    listMusic.addAll(response.body());
                    musicAdapter.addListData(listMusic);

                    Log.d("MUSIC", "Size: " + listMusic.size());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MusicModel>> call, @NonNull Throwable t) {
                Log.e("MUSIC", "Error: " + t.getMessage());
            }
        });

    }

    @Override
    public void bindView() {

    }

    @Override
    public void onBack() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(this);
        exitAppDialog.init(this::finishAffinity);
        exitAppDialog.show();
    }
}
