package com.renzoleguia.itanes.data.repository;

import android.util.Log;

import com.renzoleguia.itanes.data.local.dao.PlaceDao;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.mapper.PlaceMapper;
import com.renzoleguia.itanes.data.remote.api.ItanesApiService;
import com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto;
import com.renzoleguia.itanes.data.remote.retrofit.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private final PlaceDao placeDao;
    private final ItanesApiService apiService;
    private final ExecutorService executor;

    public PlaceRepository(PlaceDao placeDao) {
        this.placeDao = placeDao;
        this.apiService = RetrofitClient.getApiService();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void syncPlaces() {
        Log.d("ITANES_SYNC", "Iniciando sincronización");
        apiService.getPlaces().enqueue(new Callback<List<PlaceRemoteDto>>() {
            @Override
            public void onResponse(Call<List<PlaceRemoteDto>> call, Response<List<PlaceRemoteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlaceRemoteDto> remotePlaces = response.body();
                    Log.d("ITANES_SYNC", remotePlaces.size() + " lugares recibidos");

                    if (!remotePlaces.isEmpty()) {
                        executor.execute(() -> {
                            try {
                                List<PlaceEntity> entities = PlaceMapper.toEntityList(remotePlaces);
                                placeDao.insertAll(entities);
                                Log.d("ITANES_SYNC", entities.size() + " lugares guardados en Room");
                                Log.d("ITANES_SYNC", "Sincronización completada");
                            } catch (Exception e) {
                                Log.e("ITANES_SYNC", "Error al guardar en Room: " + e.getMessage());
                            }
                        });
                    } else {
                        Log.d("ITANES_SYNC", "La lista recibida está vacía.");
                    }
                } else {
                    Log.e("ITANES_SYNC", "Error HTTP en sincronización: " + (response != null ? response.code() : "null"));
                }
            }

            @Override
            public void onFailure(Call<List<PlaceRemoteDto>> call, Throwable t) {
                Log.e("ITANES_SYNC", "Error de red en sincronización: " + t.getMessage());
            }
        });
    }

    public void insertAll(List<PlaceEntity> places) {
        placeDao.insertAll(places);
    }

    public void insert(PlaceEntity place) {
        placeDao.insert(place);
    }

    public List<PlaceEntity> getAllPlaces() {
        return placeDao.getAllPlaces();
    }

    public PlaceEntity getPlaceById(int id) {
        return placeDao.getPlaceById(id);
    }

    public int getCount() {
        return placeDao.getCount();
    }

    public void deleteAll() {
        placeDao.deleteAll();
    }
}
