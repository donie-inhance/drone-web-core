package io.magpi.geo;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Grid extends ArrayList<Path> implements List<Path> {

    public JsonArray json(){

        JsonArray json=new JsonArray();

        for(Path c:this){
            json.add(c.json());
        }

        return json;
    }

    public CharSequence latLongStr() {
        List<String> locations=new ArrayList<>();


        for(Path p:this){
            locations.add(p.latLongStr());
        }


        return String.join("|",locations);
    }
}
