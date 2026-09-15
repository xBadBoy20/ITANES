package com.renzoleguia.itanes.data.mapper;

import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapper {

    public static PlaceEntity toEntity(PlaceRemoteDto dto) {
        if (dto == null) return null;
        return new PlaceEntity(
                dto.getId(),
                dto.getName(),
                dto.getShortDescription(),
                dto.getDescription(),
                dto.getAddress(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getImageUrl(),
                dto.getOrderNumber(),
                dto.getUpdatedAt()
        );
    }

    public static List<PlaceEntity> toEntityList(List<PlaceRemoteDto> remotePlaces) {
        List<PlaceEntity> entities = new ArrayList<>();
        if (remotePlaces != null) {
            for (PlaceRemoteDto dto : remotePlaces) {
                entities.add(toEntity(dto));
            }
        }
        return entities;
    }
}
