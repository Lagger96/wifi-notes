package com.example.pk.wifinotes.models;

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
}
