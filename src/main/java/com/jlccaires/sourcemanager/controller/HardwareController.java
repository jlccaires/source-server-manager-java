package com.jlccaires.sourcemanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;

/**
 * @author jlccaires - Julio Cesar Caires | 04/11/2016 - 08:31
 */
@PreAuthorize("isAuthenticated()")
@RestController
public class HardwareController {

    private static final SystemInfo systemInfo = new SystemInfo();

    @GetMapping("/cpu")
    public Object cpu() {
        synchronized (systemInfo) {
            return systemInfo.getHardware().getProcessor();
        }
    }

    @GetMapping("/mem")
    public Object mem() {
        synchronized (systemInfo) {
            return systemInfo.getHardware().getMemory();
        }
    }

    @GetMapping("/disk")
    public Object disk() {
        synchronized (systemInfo) {
            return systemInfo.getHardware().getDiskStores();
        }
    }

    @GetMapping("/net")
    public Object net(final Integer index) {
        synchronized (systemInfo) {
            return systemInfo.getHardware().getNetworkIFs()[0];
        }
    }

    @GetMapping("/sensors")
    public Object sensors() {
        synchronized (systemInfo) {
            return systemInfo.getHardware().getSensors();
        }
    }
}
