package net.alex9849.pluginstats.web.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "installations")
public class PluginInstallation {

    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    private String uuid;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "installId")
    private Set<Option> options;

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

    public int getPlayercount() {
        return playercount;
    }

    public void setPlayercount(int playercount) {
        this.playercount = playercount;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    @Entity
    @Table(name = "options")
    public static class Option {
        @Embeddable
        public static class OptionPK implements Serializable {
            @ManyToOne
            @JoinColumn(name = "installId")
            private PluginInstallation pi;

            @Column(name = "optionKey", columnDefinition = "varchar(50)")
            private String optionKey;
        }

        @EmbeddedId
        private OptionPK optionPK;

        @Column(name = "optionValue", columnDefinition = "varchar(100) not null")
        private String optionValue;

        public String getOptionKey() {
            return optionPK.optionKey;
        }

        public String getOptionValue() {
            return optionValue;
        }

        public void setOptionValue(String optionValue) {
            this.optionValue = optionValue;
        }
    }
}
