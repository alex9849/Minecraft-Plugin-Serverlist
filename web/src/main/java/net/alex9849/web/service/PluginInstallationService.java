package net.alex9849.web.service;

import net.alex9849.web.db.PluginInstallationRepo;
import net.alex9849.web.model.PluginInstallation;
import net.alex9849.web.model.PluginInstallationDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class PluginInstallationService {
    private static final long SET_PLAYER_TO_ZERO_AFTER_S = 5 * 60;

    @Autowired
    private PluginInstallationRepo repo;

    public PluginInstallationService() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repo.setPlayerCountToZero(new Timestamp(System.currentTimeMillis() - SET_PLAYER_TO_ZERO_AFTER_S));
            }
        }, 10 * 1000, SET_PLAYER_TO_ZERO_AFTER_S * 1000);
    }


    public PluginInstallationDTO processStats(PluginInstallationDTO dto, boolean isStartup) {
        UUID uuid = null;
        PluginInstallation pli = new PluginInstallation();
        if(dto.getUuid() != null) {
            uuid = UUID.fromString(dto.getUuid());
            pli = repo.findById(uuid.toString())
                    .orElse(new PluginInstallation());
        }
        BeanUtils.copyProperties(dto, pli);
        pli.setLastPing(new Timestamp(System.currentTimeMillis()));
        if(isStartup) pli.setLastStarted(new Timestamp(System.currentTimeMillis()));
        pli = repo.save(pli);
        PluginInstallationDTO returnDto = new PluginInstallationDTO();
        BeanUtils.copyProperties(pli, returnDto);
        return returnDto;
    }


}
