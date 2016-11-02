package com.jlccaires.sourcemanager.domain;

public class Player {

    private String connectedTime;
    private int id;
    private String ipAddress;
    private String name;
    private int ping;
    private int realId;
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

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
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

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(String connectedTime) {
        this.connectedTime = connectedTime;
    }
}
