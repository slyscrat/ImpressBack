package com.slyscrat.impress.service.scheduled;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

public abstract class AbstractJsonParser {
    protected String valueOf(Object document, String path) {
        String res = "";
        try {
            res = ((JSONArray) JsonPath.read(document, path)).get(0).toString();
        }
        catch(IndexOutOfBoundsException ignored){}
        return res;
    }
}
