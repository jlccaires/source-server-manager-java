package com.jlccaires.sourcemanager.service;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.jlccaires.sourcemanager.service.ConsoleService.LogListener;

@Service
@SessionScope
public class ServerService {

	private static final String TOPIC_CONSOLE = "/topic/console";

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private ConsoleService consoleService;

	private String serverAddress;
	private int serverPort;
	private String rconPassword;
	private boolean authenticated = false;

	public boolean auth(String serverAddress, int serverPort, String rconPassword)
			throws SteamCondenserException, TimeoutException {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.rconPassword = rconPassword;

		SourceServer server = getServer();
		if (server != null) {
			server.disconnect();
			return authenticated = true;
		} else {
			return false;
		}
	}

	public HashMap<String, Object> getServerInfo() throws SteamCondenserException, TimeoutException {
		SourceServer server = getServer();
		try {
			return server.getServerInfo();
		} finally {
			server.disconnect();
		}
	}

	public HashMap<String, SteamPlayer> getPlayers() throws SteamCondenserException, TimeoutException {
		SourceServer server = getServer();
		try {
			return server.getPlayers();
		} finally {
			server.disconnect();
		}
	}

	public String sendCommand(String command) throws TimeoutException, SteamCondenserException {
		SourceServer server = getServer();
		try {
			return server.rconExec(command);
		} finally {
			server.disconnect();
		}
	}

	private SourceServer getServer() throws SteamCondenserException, TimeoutException {
		SourceServer server = new SourceServer(serverAddress, serverPort);
		if (server.rconAuth(rconPassword)) {
			return server;
		}
		return null;
	}

	@PostConstruct
	private void console() {
		consoleService.setLogListener(new LogListener() {
			public void onNewLine(String line) {
				if (isAuthenticated()) {
					messagingTemplate.convertAndSend(TOPIC_CONSOLE, line);
				}
			}
		});
	}

	public boolean isAuthenticated() {
		return authenticated;
	}
}
