package com.renzoleguia.itanes.data.remote.api;

import com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ItanesApiService {

    @GET("xBadBoy20/98e4bfe2867cd65282c25b2dca58f5da/raw/places.json")
    Call<List<PlaceRemoteDto>> getPlaces();
}
