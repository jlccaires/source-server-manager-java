package com.jlccaires.sourcemanager.controller;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.jlccaires.sourcemanager.domain.Player;
import com.jlccaires.sourcemanager.service.GeoIPService;
import com.jlccaires.sourcemanager.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@PreAuthorize("isAuthenticated()")
@RestController
public class ServerController {

	@Autowired
	private ServerService serverService;

	@GetMapping("/serverInfo")
	public Object serverInfo() throws SteamCondenserException, TimeoutException {
		return serverService.getServerInfo();
	}

	@GetMapping("/players")
	public Object players() throws SteamCondenserException, TimeoutException {
		return serverService.getPlayers();
	}

	@GetMapping("/sendCommand")
	public Object sendCommand(String command) throws SteamCondenserException, TimeoutException {
		return serverService.sendCommand(command);
	}

}
