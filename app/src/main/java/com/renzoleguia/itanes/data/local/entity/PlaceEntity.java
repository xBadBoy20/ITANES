package com.renzoleguia.itanes.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class PlaceEntity {

    @PrimaryKey
    private int id;
    private String name;
    private String shortDescription;
    private String description;
    private String address;
    private double latitude;
    private double longitude;
    private String imageUrl;
    private int orderNumber;
    private String updatedAt;

    public PlaceEntity(int id, String name, String shortDescription, String description,
                       String address, double latitude, double longitude, String imageUrl,
                       int orderNumber, String updatedAt) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.orderNumber = orderNumber;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getShortDescription() { return shortDescription; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUrl() { return imageUrl; }
    public int getOrderNumber() { return orderNumber; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    public void setDescription(String description) { this.description = description; }
    public void setAddress(String address) { this.address = address; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
