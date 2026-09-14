package com.renzoleguia.itanes.data.remote.api;

import com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ItanesApiService {

    @GET("v1/fad2b51e-da5c-4e91-b11b-22f6de580fa9")
    Call<List<PlaceRemoteDto>> getPlaces();
}
