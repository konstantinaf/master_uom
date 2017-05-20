package com.uom.jira.consumers.utils;

import com.atlassian.jira.rest.client.internal.json.IssueJsonParser;
import com.uom.jirareport.consumers.dto.IssueDTO;
import com.uom.jirareport.consumers.dto.IssueResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fotarik on 20/05/2017.
 */
@Slf4j
public class JSONUtils {

    public static <T> List<T> getJSONArrayFromResponse(String responseStr, Class<T> klazz) {
        List<T> list = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseStr);
        } catch (JSONException e) {
            log.error("Cannot create JSON Array ",e);
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                log.error("Cannot read json object ",e);
            }

            ObjectMapper mapper = new ObjectMapper();
            Object object = null;
            try {
                object = mapper.readValue(jsonobject.toString(), klazz);
            } catch (IOException e) {
                log.error("Cannot create map the object ",e);
            }

            list.add(klazz.cast(object));
        }

        return list;
    }

    public static JSONObject getJSONObjectFromResponse(String responseStr) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseStr);
        } catch (JSONException e) {
            log.error("Cannot create JSON Array ",e);
        }
        return jsonObject;
    }

    public static <T extends Object> T mapJSONObjectToDTO(JSONObject jsonObject, Class<T> klazz) {

        ObjectMapper mapper = new ObjectMapper();

        T object = null;
        try {
            object =  (T) mapper.readValue(jsonObject.toString(), klazz);
        } catch (IOException e) {
            log.error("Cannot map object to class ", e);
        }

        return object;
    }
}
