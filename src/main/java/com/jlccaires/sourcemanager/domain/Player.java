package com.jlccaires.sourcemanager.domain;

public class Player {

    private Integer connectedTime;
    private int id;
    private String ipAddress;
    private String countryCode;
    private String countryName;
    private String name;
    private int ping;
    private int score;
    private String steamId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSteamId() {
        return steamId;
    }

    public Integer getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(Integer connectedTime) {
        this.connectedTime = connectedTime;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(final String countryName) {
        this.countryName = countryName;
    }
}
