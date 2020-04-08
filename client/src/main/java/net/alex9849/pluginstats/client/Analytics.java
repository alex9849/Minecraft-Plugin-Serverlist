package net.alex9849.pluginstats.client;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Analytics {
    static {
        final String defaultPackage = new String(
                new byte[]{'n', 'e', 't', '.',
                        'a', 'l', 'e', 'x', '9', '8', '4', '9', '.',
                        'p', 'l', 'u', 'g', 'i', 'n', 's', 't', 'a', 't', 's', '.',
                        'c', 'l', 'i', 'e', 'n', 't'});
        final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
        // We want to make sure nobody just copy & pastes the example and use the wrong package names
        if (Analytics.class.getPackage().getName().equals(defaultPackage) || Analytics.class.getPackage().getName().equals(examplePackage)) {
            throw new IllegalStateException("Analytics class has not been relocated correctly!");
        }
    }

    private final String apiPath = "/api/v1";
    private final URL serverUrl;
    private DataGetter additionalDataGetter;
    private final YamlConfiguration config;
    private final File confFile;
    private Plugin plugin;

    public Analytics(Plugin plugin, URL serverUrl) {
        this(plugin, serverUrl, null);
    }

    public Analytics(Plugin plugin, URL serverUrl, DataGetter additionalDataGetter) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.additionalDataGetter = additionalDataGetter;
        this.serverUrl = serverUrl;
        this.plugin = plugin;
        this.confFile = new File(plugin.getDataFolder() + "/analytics.yml");
        this.config = YamlConfiguration.loadConfiguration(this.confFile);
        this.config.options().copyDefaults(true).copyHeader(true);
        if(this.config.get("enabled") == null) {
            this.config.addDefault("enabled", true);
            this.config.options().header("This plugin collects some data like the online players, \n" +
                    "ip address, port, etc. so I can visit your server. To see what you do with my plugin helps \n" +
                    "to motivate myself to continue to develop this plugin. If you don't want that \n" +
                    "just set \"enabled\" to false. All collected data will be deleted automatically, \n" +
                    "after 2 weeks. Alternatively you can delete your data manually by pasting your \n" +
                    "installId under this link: " + this.serverUrl + "/unregister\n" +
                    "If you don't see an installId in this file, no data has been sent. Thanks for using this plugin!");
            try {
                this.config.save(confFile);
            } catch (IOException e) {
                //Ignore
            }
        }
        if(this.config.getBoolean("enabled")) {
            startSubmitting();
        } else {
            if (this.config.getString("installId") != null) {
                unregisterServer(UUID.fromString(this.config.getString("installId")));
            }
        }
    }

    private void unregisterServer(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            synchronized (Bukkit.getScheduler()) {
                try {
                    final String databaseUrl = this.serverUrl + this.apiPath + "/unregister/" + uuid.toString();
                    HttpURLConnection connection = (HttpURLConnection) new URL(databaseUrl).openConnection();
                    connection.setRequestProperty("User-Agent", "Analytic Plugin");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestMethod("DELETE");
                    connection.connect();
                    this.config.set("installId", null);
                    Future<Boolean> done =  Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
                        this.config.save(this.confFile);
                        return true;
                    });
                    done.get();
                } catch (Exception e) {
                    //TODO Ignore
                    //Ignore
                    e.printStackTrace();
                }
            }
        });
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                submitData();
            }
        }, 1000 * 10, 1000 * 60 * 5);
    }

    private JSONObject getData() {
        JSONObject data = new JSONObject();

        String installId = this.config.getString("installId");
        data.put("serverVersion", Bukkit.getVersion());
        data.put("serverPort", Bukkit.getPort());
        data.put("modt", Bukkit.getServer().getMotd());
        data.put("confIp", Bukkit.getIp());
        data.put("onlinePlayers", Bukkit.getOnlinePlayers().size());
        data.put("onlineMode", Bukkit.getOnlineMode());
        data.put("plugin", this.plugin.getName());
        data.put("pluginVersion", this.plugin.getDescription().getVersion());
        if(this.additionalDataGetter != null) {
            data.put("options", this.additionalDataGetter.getData());
        }
        if(installId != null) {
            data.put("installId", installId);
        }
        return data;
    }

    /**
     * DO NOT RUN THIS ON THE MAIN THREAD!
     * @throws IOException
     * @throws ParseException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void submitData() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            synchronized(Bukkit.getScheduler()) {
                try {
                    final String databaseUrl = this.serverUrl + this.apiPath + "/sendstats";
                    Future<JSONObject> sendDataFuture = Bukkit.getScheduler().callSyncMethod(plugin, this::getData);
                    JSONObject sendData = sendDataFuture.get();
                    String sendDataString = sendData.toJSONString();

                    HttpURLConnection connection = (HttpURLConnection) new URL(databaseUrl).openConnection();
                    connection.setRequestProperty("User-Agent", "Analytic Plugin");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Content-Length", sendDataString.getBytes().length + "");
                    connection.setRequestMethod("PUT");
                    connection.setDoOutput(true);
                    PrintStream ps = new PrintStream(connection.getOutputStream());
                    ps.print(sendDataString);
                    ps.flush();
                    ps.close();

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    bufferedReader.close();

                    if(connection.getResponseCode() != 200) {
                        return;
                    }
                    JSONParser jsonParser = new JSONParser();
                    JSONObject response = (JSONObject) jsonParser.parse(sb.toString());
                    String installId = (String) response.get("installId");

                    if(!Objects.equals(this.config.getString("installId"), installId)) {
                        this.config.set("installId", installId);

                        Future<Boolean> done = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                            this.config.save(this.confFile);
                            return true;
                        });
                        done.get();

                    }
                } catch (Exception e) {
                    //TODO Ignore
                    //Ignore
                    e.printStackTrace();
                }
            }
        });
    }

    public interface DataGetter {
        Map<String, String> getData();
    }
}
