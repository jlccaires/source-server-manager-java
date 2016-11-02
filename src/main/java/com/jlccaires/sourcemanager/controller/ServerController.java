package com.jlccaires.sourcemanager.controller;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.jlccaires.sourcemanager.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RestController
public class ServerController {

	@Autowired
	private ServerService serverService;

	@GetMapping("/authenticated")
	public Object authenticated() {
		return serverService.isAuthenticated();
	}

	@GetMapping("/auth")
	public Object auth(@RequestParam(required = false, defaultValue = "27015") int serverPort, @RequestParam String rconPassword)
			throws SteamCondenserException, TimeoutException {
		if (serverService.auth(serverPort, rconPassword)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/serverInfo")
	public Object serverInfo() throws SteamCondenserException, TimeoutException {
		if (!serverService.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return serverService.getServerInfo();
	}

	@GetMapping("/players")
	public Object players() throws SteamCondenserException, TimeoutException {
		if (!serverService.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return serverService.getPlayers();
	}

	@GetMapping("/sendCommand")
	public Object sendCommand(String command) throws SteamCondenserException, TimeoutException {
		if (!serverService.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return serverService.sendCommand(command);
	}

}
