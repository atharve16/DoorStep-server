package com.doostep.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEntity {

    @Id
    private String id;
    private String planName;
    private String builder;
    private String community;
    private String city;
    private String state;
    private String zipCode;
    private String price;
    private int beds;
    private double baths;
    private int sqft;
    private String lotSize;
    private int stories;
    private String status;
    private int yearBuilt;
    private String images;
    private String description;
    private List<String> features;
    private List<String> amenities;
    private Coordinates coordinates;

    public static class Coordinates {
        private double lat;
        private double lng;

        // Getters and Setters
        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }
        public double getLng() { return lng; }
        public void setLng(double lng) { this.lng = lng; }
    }

}
