package eu.ac3_servers.dev.bvotifier.bukkit;

import java.io.File;
import java.io.IOException;

import net.komputerking.updater.Updater;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.vexsoftware.votifier.Votifier;


public class BVotifier extends Votifier {
	
	public final String MessageChannel = "BVotifier";

	private BVConfig config;
	
	private static final String ID = "596";
	
	@SuppressWarnings({ "static-access" })
	@Override
	public void onEnable() {
		
		if(getDataFolder().exists()){
			File newConfigFile = new File(getDataFolder(), "bvconfig.yml");
			
			if(!newConfigFile.exists()){
				try {
					FileUtils.deleteDirectory(getDataFolder());
				} catch (IOException e) {
					e.printStackTrace();
					getLogger().severe("[BV] We couldn't delete the old \"plugins/" + getDataFolder().getName() + "\" folder please delete this manually!");
				}
			}
		}
			
		super.onEnable();
		
		//getVoteReceiver().shutdown();
		//getLogger().info("[BV] Shutting down the listener!");
		
		this.config = new BVConfig(this);
		this.config.getConfig().set("bungee", null);
		this.config.saveConfig();
		getLogger().info("[BV] Init BVConfig.");
		
		if(getBVConfig().getBoolean("both.updater") && !getBVConfig().getBoolean("both.updater")){
			
			getLogger().info("[DEBUG] Updating with ID: " + this.ID);
			Updater updater = new Updater(this.ID, this);
			getLogger().info("[DEBUG] Initilised the updater.");
			updater.performUpdateCheck();
			
		}
		
		this.getServer().getMessenger().registerIncomingPluginChannel(this, MessageChannel, new PMListener(this));
		getLogger().info("[BV] Registered \"" + this.MessageChannel + "\" as an incoming channel!");
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(command.getName().equalsIgnoreCase("votifier") && sender.hasPermission("votifier.debug")){
			if(getBVConfig().getBoolean("both.debug")){
				getBVConfig().set("both.debug", false);
				sender.sendMessage(ChatColor.YELLOW + "[Votifier] " +ChatColor.BLUE + "Turned debugging off.");
			}else{
				getBVConfig().set("both.debug", true);
				sender.sendMessage(ChatColor.YELLOW + "[Votifier] " +ChatColor.BLUE + "Turned debugging on.");
			}
			saveBVConfig();
			return true;
		}else if(command.getName().equalsIgnoreCase("votifier")){
			sender.sendMessage(ChatColor.YELLOW + "[Votifier] " + ChatColor.BLUE + "Bungeecord Votifier "+ ChatColor.GREEN + "v"+getDescription().getVersion());
			sender.sendMessage(ChatColor.YELLOW + "[Votifier] " + ChatColor.BLUE + "Thanks a lot, acecheesecr14 " + ChatColor.RED + "<3");
		}
		return false;
	}
	
	

	public FileConfiguration getBVConfig() {
		return this.config.getConfig();
	}
	
	public void saveBVConfig(){
		this.config.saveConfig();
	}
	
}
