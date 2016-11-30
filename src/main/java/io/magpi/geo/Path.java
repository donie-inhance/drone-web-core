package io.magpi.geo;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class Path extends ArrayList<Coordinate> implements List<Coordinate> {

    public JsonArray json(){

        JsonArray json=new JsonArray();

        for(Coordinate c:this){
            json.add(c.json());
        }

        return json;
    }
}
