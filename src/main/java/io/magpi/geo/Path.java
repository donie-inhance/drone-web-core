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

    public String latLongStr() {
        List<String> locations=new ArrayList<>();


        for(Coordinate p:this){
            locations.add(p.latLongStr());
        }


        return String.join("|",locations);
    }
}
