package com.jlccaires.sourcemanager.domain;

import com.github.koraktor.steamcondenser.steam.SteamPlayer;

public class Player {
	
    private float connectTime;
    private int id;
    private String ipAddress;
    private String name;
    private int ping;
    private int realId;
    private int score;
    private String steamId;
	
	public static Player create(SteamPlayer steamPlayer) {
		Player player = new Player();
		player.setConnectTime(steamPlayer.getConnectTime());
		player.setId(steamPlayer.getId());
		player.setIpAddress(steamPlayer.getIpAddress());
		player.setName(steamPlayer.getName());
		player.setPing(steamPlayer.getPing());
		player.setRealId(steamPlayer.getRealId());
		player.setScore(steamPlayer.getScore());
		player.setSteamId(steamPlayer.getSteamId());
		return player;
	}

	public float getConnectTime() {
		return connectTime;
	}

	public void setConnectTime(float connectTime) {
		this.connectTime = connectTime;
	}

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
}
