package com.example.baseproduct.call_api.client;

import com.example.baseproduct.call_api.api.MusicApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static MusicApi getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/LongNV2572/ThanhMP3/main/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MusicApi.class);
    }
}

