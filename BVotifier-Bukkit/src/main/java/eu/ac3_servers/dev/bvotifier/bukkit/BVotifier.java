package eu.ac3_servers.dev.bvotifier.bukkit;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.vexsoftware.votifier.Votifier;


public class BVotifier extends Votifier {
	
	public final String MessageChannel = "BVotifier";
	
	
	
	@Override
	public void onEnable() {
		
		File cfg = new File(getDataFolder(), "config.yml");
		if(!cfg.exists()) saveDefaultConfig();
		saveConfig();
		
		this.getServer().getMessenger().registerIncomingPluginChannel(this, MessageChannel, new PMListener(this));
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(command.getName().equalsIgnoreCase("votifier") && sender.hasPermission("votifier.debug")){
			if(getConfig().getBoolean("bukkit.debug")){
				getConfig().set("bukkit.debug", false);
				sender.sendMessage(ChatColor.YELLOW + "[Votifier] " +ChatColor.BLUE + "Turned debugging off.");
			}else{
				getConfig().set("bukkit.debug", true);
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
