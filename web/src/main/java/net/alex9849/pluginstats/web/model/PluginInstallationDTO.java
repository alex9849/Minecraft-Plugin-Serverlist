package net.alex9849.pluginstats.web.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class PluginInstallationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uuid;
    private int serverPort;
    private String plugin;
    private boolean onlineMode;
    private String pluginVersion;
    private String serverVersion;
    private String modt;
    private String pingIp;
    private String confIp;
    private int playercount;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Integer getPlayercount() {
        return playercount;
    }

    public void setPlayercount(Integer playercount) {
        this.playercount = playercount;
    }

    public class OptionDTO {
        private String option;
        private String value;

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
