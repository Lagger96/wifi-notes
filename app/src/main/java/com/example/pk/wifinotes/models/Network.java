package com.example.pk.wifinotes.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Network {
    private Integer id;
    private String ssid;
    private String password;
    private String description;
    private String category;

    public Network(Integer id, String ssid, String password, String description, String category) {
        this.id = id;
        this.ssid = ssid;
        this.password = password;
        this.description = description;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("ssid", ssid);
            jsonObject.put("password", password);
            jsonObject.put("description", description);
            jsonObject.put("category", category);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
