This little project can be used to let you submit data from 
Minecraft-servers that use your Minecraft-plugins to a database.

This project consists of 2 components. One client that has to be 
added as a maven dependency (with scope compile) and a server with database.
The server exists as a docker container that needs to be connected to a MySQL-Database.

**Setup the client:**

Add the jitpack repository:
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Add the client as a Maven-Dependency: (Use the same version that you use for the client for the server)
```
<dependency>
    <groupId>com.github.alex9849.Minecraft-Plugin-Serverlist</groupId>
    <artifactId>client</artifactId>
    <version>1.0</version>
    <scope>compile</scope>
</dependency>
```

The client-classes need to be relocated. The client will not start if you don't do that:
```
<plugins>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>2.4.3</version>
    <configuration>
      <relocations>
        <relocation>
          <pattern>net.alex9849.pluginstats.client</pattern>
          <shadedPattern>YOU.NEW.SHADE.PATTERN</shadedPattern>
        </relocation>
      </relocations>
    </configuration>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>shade</goal>
        </goals>
        <configuration>
        </configuration>
      </execution>
    </executions>
  </plugin>
</plugins>
```

Add this to the onEnable-method of your plugin. Replace YOUR_INSTALLATION_URL with the url of your 
server-installation. Its recommended run the server behind a proxy with Https.
You can let the client send additional data to your server by creating a DataGetter and pass
it in the constructor.
```
new Analytics(this, new URL("https://YOUR_INSTALLATION_URL.TLD"), () -> {
                Map<String, String> pluginSpecificData = new LinkedHashMap<>();
                pluginSpecificData.put("IAmAnExtraAttribute", "IAmAValue");
                return pluginSpecificData;
            });
```


**Setup the server**  
Use the following docker image:  
(Use the same version that you use for the client for the server)  
```docker pull alex9849/mc-pluginstats-server:tagname```

You need to connect the client with a MySQL-Database. You can pass the login Data to the Server
via environment variables. If you want to view the stored data, you can use something like PhPMyAdmin.

**Server environment variables:**  
Docker-Image requires a MySQL database
 - DB_USER: The database user (required)
 - DB_PW: The database password (required)
 - DB_NAME: The database name (required)
 - DB_HOST: The database host (required)
 - DB_PORT: The database port (required)
 - MAX_NEW_REGISTRATIONS_PER_IP_PER_HOUR: Max new registrations per hour per ip (spam protect)
 - LOG_LEVEL: The logging-level. Possible values: trace, debug, info, warn, error, fatal, off
 - TZ: Timezone default: Europe/Berlin
 