package io.magpi.geo;

import io.vertx.core.json.JsonObject;

public class Coordinate {

    Double latitude=null;
    Double longitude=null;
    Double elevation=null;
    Double resolution=null;

    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(Double latitude, Double longitude, Double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public JsonObject json() {
        JsonObject json=new JsonObject();

        json.put("latitude",latitude);
        json.put("longitude",longitude);

        if(elevation!=null) {
            json.put("elevation", elevation);
        }
        if(resolution!=null){
            json.put("resolution",resolution);
        }

        return json;
    }

    public String latLongStr(){

        return latitude+","+longitude;
    }
}
