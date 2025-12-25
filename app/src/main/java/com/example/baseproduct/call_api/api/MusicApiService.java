package com.example.baseproduct.call_api.api;

import com.example.baseproduct.call_api.model.MusicModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MusicApiService {
    @GET("data/music_list.json")
    Call<List<MusicModel>> getMusic();
}

