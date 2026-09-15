package com.renzoleguia.itanes.data.remote.retrofit;

import com.renzoleguia.itanes.data.remote.api.ItanesApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Cambia esto en RetrofitClient.java
    private static final String BASE_URL = "https://gist.githubusercontent.com/";
    private static Retrofit retrofit = null;

    private RetrofitClient() {
        // Evitar instanciación directa
    }

    public static ItanesApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ItanesApiService.class);
    }
}
