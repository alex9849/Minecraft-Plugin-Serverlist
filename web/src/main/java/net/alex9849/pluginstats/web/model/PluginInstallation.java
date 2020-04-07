package net.alex9849.pluginstats.web.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "installations")
public class PluginInstallation {

    @Id
    @Column(name = "installId", columnDefinition = "varchar(36)")
    private String installId;

    @Column(name = "serverPort", columnDefinition = "integer")
    private int serverPort;

    @Column(name = "plugin", columnDefinition = "varchar(50)")
    private String plugin;

    @Column(name = "onlinemode", columnDefinition = "boolean")
    private boolean onlineMode;

    @Column(name = "pluginVersion", columnDefinition = "varchar(10)")
    private String pluginVersion;

    @Column(name = "serverVersion", columnDefinition = "varchar(100)")
    private String serverVersion;

    @Column(name = "modt", columnDefinition = "varchar(1024)")
    private String modt;

    @Column(name = "pingIp", columnDefinition = "varchar(30)")
    private String pingIp;

    @Column(name = "confIp", columnDefinition = "varchar(30)")
    private String confIp;

    @Column(name = "created", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp created;

    @Column(name = "lastStarted", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp lastStarted;

    @Column(name = "lastPing", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp lastPing;

    @Column(name = "playercount", columnDefinition = "integer not null DEFAULT 0")
    private int playercount;


    @ElementCollection()
    @JoinTable(name="options", joinColumns=@JoinColumn(name="installId"))
    @MapKeyColumn(name="optionKey", columnDefinition = "varchar(60)")
    @Column(name="optionValue")
    private Map<String, String> options;

    @PrePersist
    public void prePersist() {
        if(getInstallId() == null) {
            this.installId = UUID.randomUUID().toString();
        }
    }

    public String getInstallId() {
        return installId;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getModt() {
        return modt;
    }

    public void setModt(String modt) {
        this.modt = modt;
    }

    public String getPingIp() {
        return pingIp;
    }

    public void setPingIp(String pingIp) {
        this.pingIp = pingIp;
    }

    public String getConfIp() {
        return confIp;
    }

    public void setConfIp(String confIp) {
        this.confIp = confIp;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getLastStarted() {
        return lastStarted;
    }

    public void setLastStarted(Timestamp lastStarted) {
        this.lastStarted = lastStarted;
    }

    public Timestamp getLastPing() {
        return lastPing;
    }

    public void setLastPing(Timestamp lastPing) {
        this.lastPing = lastPing;
    }

    public int getPlayercount() {
        return playercount;
    }

    public void setPlayercount(int playercount) {
        this.playercount = playercount;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
