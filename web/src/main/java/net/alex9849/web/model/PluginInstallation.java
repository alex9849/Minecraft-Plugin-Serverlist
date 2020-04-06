package net.alex9849.web.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.UUID;

@XmlRootElement
public class PluginInstallation {

    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    private String uuid;

    @Column(name = "server_port", columnDefinition = "integer")
    private int serverPort;

    @Column(name = "plugin", columnDefinition = "varchar(50)")
    private String plugin;

    @Column(name = "onlinemode", columnDefinition = "boolean")
    private boolean onlineMode;

    @Column(name = "plugin_version", columnDefinition = "varchar(10)")
    private String pluginVersion;

    @Column(name = "server_version", columnDefinition = "varchar(100)")
    private String serverVersion;

    @Column(name = "modt", columnDefinition = "varchar(1024)")
    private String modt;

    @Column(name = "ping_ip", columnDefinition = "varchar(30)")
    private String pingIp;

    @Column(name = "conf_ip", columnDefinition = "varchar(30)")
    private String confIp;

    @Column(name = "created", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp created;

    @Column(name = "last_started", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp lastStarted;

    @Column(name = "last_ping", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Generated(value = GenerationTime.INSERT)
    private Timestamp lastPing;

    @Column(name = "playercount", columnDefinition = "integer")
    private Integer playercount;

    @PrePersist
    public void prePersist() {
        if(getUuid() == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    public String getUuid() {
        return uuid;
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

    public Integer getPlayercount() {
        return playercount;
    }

    public void setPlayercount(Integer playercount) {
        this.playercount = playercount;
    }
}
