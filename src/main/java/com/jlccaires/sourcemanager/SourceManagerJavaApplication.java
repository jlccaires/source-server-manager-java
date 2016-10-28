package com.jlccaires.sourcemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;

@SpringBootApplication
public class SourceManagerJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SourceManagerJavaApplication.class, args);
		SteamSocket.setTimeout(5000);
	}

}
