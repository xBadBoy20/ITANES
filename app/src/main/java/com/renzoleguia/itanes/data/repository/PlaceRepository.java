package com.renzoleguia.itanes.data.repository;

import com.renzoleguia.itanes.data.local.dao.PlaceDao;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;

import java.util.List;

public class PlaceRepository {

    private final PlaceDao placeDao;

    public PlaceRepository(PlaceDao placeDao) {
        this.placeDao = placeDao;
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
