package net.alex9849.pluginstats.web.endpoints;

import jakarta.servlet.http.HttpServletRequest;
import net.alex9849.pluginstats.web.exception.ForbiddenException;
import net.alex9849.pluginstats.web.model.request.PluginInstallationDTO;
import net.alex9849.pluginstats.web.model.response.InstallationResponse;
import net.alex9849.pluginstats.web.service.PluginInstallationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/api/v1")
public class Endpoint {
    private Map<String, Integer> newIdsPerIp = new ConcurrentHashMap<>();
    private static int MAX_NEW_UUIDS_PER_IP_PER_HOUR;

    static {
        if(System.getenv("MAX_NEW_REGISTRATIONS_PER_IP_PER_HOUR") == null) {
            MAX_NEW_UUIDS_PER_IP_PER_HOUR = 10;
        } else {
            MAX_NEW_UUIDS_PER_IP_PER_HOUR = Integer.parseInt(System.getenv("MAX_NEW_REGISTRATIONS_PER_IP_PER_HOUR"));
        }
    }

    @Autowired
    PluginInstallationService pls;


    public Endpoint() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for(String pingIp : newIdsPerIp.keySet()) {
                    Integer newIds = newIdsPerIp.get(pingIp);
                    if(newIds != null) {
                        newIds--;
                        if(newIds <= 0) {
                            newIdsPerIp.remove(pingIp);
                        } else {
                            newIdsPerIp.put(pingIp, newIds);
                        }
                    }
                }
            }
        }, 10 * 1000, (60 * 60 * 1000) / MAX_NEW_UUIDS_PER_IP_PER_HOUR);
    }

    @RequestMapping(value = "sendstats", method = RequestMethod.PUT)
    public InstallationResponse sendStats(HttpServletRequest request, @RequestBody() PluginInstallationDTO piDto,
                                          @RequestParam(required = false, defaultValue = "false") boolean startup) {
        if(!Objects.equals(request.getHeader("User-Agent"), "Analytic Plugin")) {
            throw new ForbiddenException();
        }
        String pingIp = request.getHeader("x-forwarded-for");
        if (pingIp == null) {
            pingIp = request.getHeader("X_FORWARDED_FOR");
            if (pingIp == null){
                pingIp = request.getRemoteAddr();
            }
        }
        Integer newIdsForThisIp = this.newIdsPerIp.get(pingIp);
        if(newIdsForThisIp != null && newIdsForThisIp > MAX_NEW_UUIDS_PER_IP_PER_HOUR) {
            throw new ForbiddenException("Too many requests from IP: " + pingIp + "!");
        }

        piDto.setPingIp(pingIp);
        String sendId = piDto.getInstallId();
        InstallationResponse response = pls.processStats(piDto, startup);

        UUID uuid = UUID.fromString(response.getInstallId());

        if(!Objects.equals(sendId, uuid.toString())) {
            newIdsPerIp.putIfAbsent(pingIp, 1);
            int pingCounter = newIdsPerIp.get(pingIp);
            newIdsPerIp.put(pingIp, ++pingCounter);
        }
        return response;
    }

    @RequestMapping(value = "unregister/{installId}", method = RequestMethod.DELETE)
    public void unregister(HttpServletRequest request, @PathVariable("installId") UUID installId) {
        if(!Objects.equals(request.getHeader("User-Agent"), "Analytic Plugin")) {
            throw new ForbiddenException();
        }
        pls.deleteInstallation(installId.toString());
    }

}
