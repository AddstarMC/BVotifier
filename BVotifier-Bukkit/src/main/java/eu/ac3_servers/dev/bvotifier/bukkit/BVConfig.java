package eu.ac3_servers.dev.bvotifier.bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class BVConfig {
	
	private Plugin plugin;

	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	public BVConfig(Plugin plugin) {
				
		this.plugin = plugin;
		
		this.customConfigFile = new File(this.plugin.getDataFolder(), "bvconfig.yml");
		if(!customConfigFile.exists()){
			saveDefaultConfig(true);
		}
		
	}
	
	public void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(this.plugin.getDataFolder(), "bvconfig.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(this.plugin.getResource("bvconfig.yml"), "UTF8");
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        customConfig.setDefaults(defConfig);
		        saveConfig();
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getConfig() {
	    if (customConfig == null) {
		reloadCustomConfig();
	    }
	    return customConfig;
	}

	public void saveConfig() {
	    if (customConfig == null || customConfigFile == null) {
	        return;
	    }
	    try {
	        getConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public void saveDefaultConfig() {
	    if (customConfigFile == null) {
	        customConfigFile = new File(this.plugin.getDataFolder(), "bvconfig.yml");
	    }
	    if (!customConfigFile.exists()) {
	    	plugin.saveResource("bvconfig.yml", false);
	     }
	}
	
	public void saveDefaultConfig(Boolean override) {
	    if (customConfigFile == null) {
	        customConfigFile = new File(this.plugin.getDataFolder(), "bvconfig.yml");
	    }
	    if (!customConfigFile.exists()) {
	    	plugin.saveResource("bvconfig.yml", override);
	     }
	}
	
}
