package net.alex9849.pluginstats.web.db;

import net.alex9849.pluginstats.web.model.PluginInstallation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface PluginInstallationRepo extends CrudRepository<PluginInstallation, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE PluginInstallation i SET i.playercount = 0 WHERE i.lastPing < :lastPingOlderThan")
    void setPlayerCountToZero(@Param("lastPingOlderThan") Timestamp lastPingOlderThan);

}
