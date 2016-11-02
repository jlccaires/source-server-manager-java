package com.jlccaires.sourcemanager.service;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.jlccaires.sourcemanager.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@SessionScope
public class ServerService {

    private static final String TOPIC_CONSOLE = "/topic/console";
    private static final Pattern pPlayers = Pattern.compile("#\\s+(\\d+)\\s+\"(.+)\"\\s+\\[(.+)\\]\\s+(\\d+\\:\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\w+)\\s+(\\d+.\\d+.\\d+.\\d+)");

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ConsoleService consoleService;
    @Value("${server.source.address:127.0.0.1}")
    private String serverAddress;

    private int serverPort;
    private String rconPassword;
    private boolean authenticated = false;

    public boolean auth(int serverPort, String rconPassword)
            throws SteamCondenserException, TimeoutException {
        this.serverPort = serverPort;
        this.rconPassword = rconPassword;

        SourceServer server = getServer(true);
        if (server != null) {
            server.disconnect();
            return authenticated = true;
        } else {
            return false;
        }
    }

    public HashMap<String, Object> getServerInfo() throws SteamCondenserException, TimeoutException {
        SourceServer server = getServer(true);
        if (server == null) {
            return new HashMap<>();
        }
        try {
            return server.getServerInfo();
        } finally {
            server.disconnect();
        }
    }

    public List<Player> getPlayers() throws SteamCondenserException, TimeoutException {
        SourceServer server = getServer(true);
        if (server == null) {
            return new ArrayList<>();
        }

        final String playersString = server.rconExec("status");
        final String[] pStringArray = playersString.split("\n");

        final List<Player> players = new ArrayList<>();
        for (String pLine : pStringArray) {
            Matcher matcher = pPlayers.matcher(pLine);
            if (matcher.find()) {
                final Player player = new Player();
                player.setId(Integer.parseInt(matcher.group(1)));
                player.setName(matcher.group(2));
                player.setSteamId(matcher.group(3));
                player.setConnectedTime(matcher.group(4));
                player.setPing(Integer.parseInt(matcher.group(5)));
                player.setIpAddress(matcher.group(8));
                players.add(player);

            }
        }
        return players;
    }

    public String sendCommand(String command) throws TimeoutException, SteamCondenserException {
        SourceServer server = getServer(true);
        if (server == null) {
            return null;
        }
        try {
            return server.rconExec(command);
        } finally {
            server.disconnect();
        }
    }

    private SourceServer getServer(boolean withRconAuth) throws SteamCondenserException, TimeoutException {
        SourceServer server = new SourceServer(serverAddress, serverPort);
        if (!withRconAuth) {
            return server;
        }
        if (server.rconAuth(rconPassword)) {
            return server;
        }
        return null;
    }

    @PostConstruct
    private void console() {
        consoleService.setLogListener(line -> {
            if (isAuthenticated()) {
                messagingTemplate.convertAndSend(TOPIC_CONSOLE, line);
            }
        });
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
