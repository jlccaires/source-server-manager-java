package com.jlccaires.sourcemanager.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.record.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author jlccaires - Julio Cesar Caires | 01/11/2016 - 09:06
 */
@Service
public class GeoIPService {

    @Autowired
    private ResourceLoader resourceLoader;
    private DatabaseReader reader;

    @PostConstruct
    public void init() throws IOException {
        reader = new DatabaseReader.Builder(resourceLoader.getResource("classpath:GeoLite2-Country.mmdb").getFile()).build();
    }

    @PreDestroy
    public void preDestroy() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    public Country countryCodeBy(String ip) throws IOException, GeoIp2Exception {
        return reader.country(InetAddress.getByName(ip)).getCountry();
    }

}
