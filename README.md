# BVotifier #

BVotifier is a great plugin for simple and easy use with networks using BungeeCord.

### Information ###

BVotifier uses the Plugin Messaging Channel instead of opening any more ports, either because you can't or because your host won't let you.

[Git Repository.](https://bitbucket.org/acecheesecr14/bvotifier/)
[Build Server](http://ci.ac3-servers.eu/)

### Maven Integration ###


```
#!html

  <repository>
  	<id>AC3-Servers</id>
  	<url>http://ci.ac3-servers.eu/plugin/repository/everything/</url>
  </repository>
  
  <!-- Both Bukkit and BungeeCord -->
  <dependency>
  	<groupId>eu.ac3-servers.dev</groupId>
  	<artifactId>BVotifier</artifactId>
  	<version>SNAPSHOT</version>
  </dependency>

  <!-- BungeeCord Only. -->
  <dependency>
  	<groupId>eu.ac3-servers.dev</groupId>
  	<artifactId>BVotifier-Bungee</artifactId>
  	<version>1.0.6</version>
  </dependency>

  <!-- Bukkit Only. -->
  <dependency>
  	<groupId>eu.ac3-servers.dev</groupId>
  	<artifactId>BVotifier-Bukkit</artifactId>
  	<version>1.0.5</version>
  </dependency>

```


### How do I get set up? ###

1. Install the [BVotifier.jar](http://www.spigotmc.org/resources/bvotifier.596/) into the plugins folder for all servers, including the BungeeCord server.
2. Restart all the servers and edit the configuration files (ignore config.yml for Bukkit) as necessary.
3. Copy the public.key from the BungeeCord server's "plugins/BVotifier/rsa" folder and put it in the vote site.
4. Install a listener in the Bukkit servers.
5. Get a popular server.

### Issues ###

* Use the issues tab from the BVotifier repo.

### Licencing ###

* Please read the LICENCE.TXT and DISCLAIMER.TXT in the BVotifier repo.