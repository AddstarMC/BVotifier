package eu.ac3_servers.dev.bvotifier.bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.komputerking.updater.Updater;

import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.vexsoftware.votifier.Votifier;


public class BVotifier extends Votifier {
	
	public final String MessageChannel = "BVotifier";
	
	private static final String ID = "bvotifier.596";
	
	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		
		File cfg = new File(getDataFolder(), "config.yml");
		if(!cfg.exists()){
			
			try {
				cfg.createNewFile();
				InputStream resource = getResource("config.yml");
				IOUtils.copy(resource, new FileOutputStream(cfg));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(getConfig().getBoolean("both.updater")){
			
			getLogger().info("[DEBUG] Updating with ID: " + this.ID);
			Updater updater = new Updater(this.ID, this);
			getLogger().info("[DEBUG] Initilised the updater.");
			updater.performUpdateCheck();
			
		}
		
		this.getServer().getMessenger().registerIncomingPluginChannel(this, MessageChannel, new PMListener(this));
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(command.getName().equalsIgnoreCase("votifier") && sender.hasPermission("votifier.debug")){
			if(getConfig().getBoolean("both.debug")){
				getConfig().set("both.debug", false);
				sender.sendMessage(ChatColor.YELLOW + "[Votifier] " +ChatColor.BLUE + "Turned debugging off.");
			}else{
				getConfig().set("both.debug", true);
				sender.sendMessage(ChatColor.YELLOW + "[Votifier] " +ChatColor.BLUE + "Turned debugging on.");
			}
			saveConfig();
			return true;
		}else if(command.getName().equalsIgnoreCase("votifier")){
			sender.sendMessage(ChatColor.YELLOW + "[Votifier] " + ChatColor.BLUE + "Bungeecord Votifier "+ ChatColor.GREEN + "v"+getDescription().getVersion());
			sender.sendMessage(ChatColor.YELLOW + "[Votifier] " + ChatColor.BLUE + "Thanks a lot, acecheesecr14 " + ChatColor.RED + "<3");
		}
		return false;
	}
}
