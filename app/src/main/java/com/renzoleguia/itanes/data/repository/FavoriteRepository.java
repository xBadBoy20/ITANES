package com.renzoleguia.itanes.data.repository;

import com.renzoleguia.itanes.data.local.dao.FavoriteDao;
import com.renzoleguia.itanes.data.local.entity.FavoriteEntity;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;

    public FavoriteRepository(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public void addFavorite(FavoriteEntity favorite) {
        favoriteDao.insertFavorite(favorite);
    }

    public void removeFavorite(int placeId) {
        favoriteDao.deleteFavorite(placeId);
    }

    public boolean isFavorite(int placeId) {
        return favoriteDao.isFavorite(placeId);
    }
}
