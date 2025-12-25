package com.example.baseproduct.ui.home;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.example.baseproduct.base.BaseActivity;
import com.example.baseproduct.call_api.api.MusicApiService;
import com.example.baseproduct.call_api.client.RetrofitClient;
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
        musicAdapter = new MusicAdapter(music -> {

        });
        binding.rcvData.setAdapter(musicAdapter);

        MusicApiService apiService = RetrofitClient.getClient().create(MusicApiService.class);
        Call<List<MusicModel>> call = apiService.getMusic();

        call.enqueue(new Callback<List<MusicModel>>() {
            @Override
            public void onResponse(Call<List<MusicModel>> call, Response<List<MusicModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MusicModel> musicList = response.body();
                    listMusic.clear();
                    listMusic.addAll(musicList);
                    musicAdapter.addListData(listMusic);
                    for (MusicModel music : musicList) {
                        Log.d("check_data", "Song: " + music.getName() + " - " + music.getSinger());
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MusicModel>> call, Throwable t) {
                Log.e("check_data", "Error: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
