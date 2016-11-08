package com.jlccaires.sourcemanager.service;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.jlccaires.sourcemanager.domain.Player;
import com.jlccaires.sourcemanager.domain.ServerDetails;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServerService {

    private static final String TOPIC_CONSOLE = "/topic/console";
    private static final Pattern pPlayers = Pattern.compile("#\\s+(\\d+)\\s+\"(.+)\"\\s+\\[(.+)\\]\\s+(\\d+\\:\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\w+)\\s+(\\d+.\\d+.\\d+.\\d+)");
    private static final Pattern pTime = Pattern.compile("((\\d+):)?(\\d+):(\\d+)");

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ConsoleService consoleService;
    @Autowired
    private GeoIPService ipService;
    @Value("${server.source.address:127.0.0.1}")
    private String serverAddress;

    public boolean auth(int serverPort, String rconPassword)
            throws SteamCondenserException, TimeoutException {

        SourceServer server = new SourceServer(serverAddress, serverPort);
        try {
            return server.rconAuth(rconPassword);
        } finally {
            server.disconnect();
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

        HashMap<String, SteamPlayer> playersHash = null;
        try {
            playersHash = server.getPlayers(null);
        } catch (Exception e) {
        }

        final List<Player> players = new ArrayList<>();
        for (String pLine : pStringArray) {
            Matcher matcher = pPlayers.matcher(pLine);
            if (matcher.find()) {
                final Player player = new Player();
                player.setId(Integer.parseInt(matcher.group(1)));
                player.setName(matcher.group(2));
                player.setSteamId(matcher.group(3));

                int timeSeconds = 0;
                final Matcher timeMatcher = pTime.matcher(matcher.group(4));
                if (timeMatcher.find()) {
                    if (timeMatcher.group(2) != null) {
                        timeSeconds += TimeUnit.HOURS.toSeconds(Integer.parseInt(timeMatcher.group(2)));
                    }
                    timeSeconds += TimeUnit.MINUTES.toSeconds(Integer.parseInt(timeMatcher.group(3)));
                    timeSeconds += Integer.parseInt(timeMatcher.group(4));
                }
                player.setConnectedTime(timeSeconds);
                player.setPing(Integer.parseInt(matcher.group(5)));
                player.setIpAddress(matcher.group(8));

                try {
                    Country country = ipService.countryCodeBy(player.getIpAddress());
                    player.setCountryCode(country.getIsoCode());
                    player.setCountryName(country.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (playersHash != null) {
                    SteamPlayer steamPlayer = playersHash.get(player.getName());
                    if (steamPlayer != null) {
                        player.setScore(steamPlayer.getScore());
                    }
                }

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

        ServerDetails serverDetails = (ServerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SourceServer server = new SourceServer(serverAddress, serverDetails.port);
        if (!withRconAuth) {
            return server;
        }
        if (server.rconAuth(serverDetails.rconPassword)) {
            return server;
        }
        return null;
    }

    @PostConstruct
    private void console() {
        consoleService.setLogListener(line -> {
            messagingTemplate.convertAndSend(TOPIC_CONSOLE, line);
        });
    }

}
