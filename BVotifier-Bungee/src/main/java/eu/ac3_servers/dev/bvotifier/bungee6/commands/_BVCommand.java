package eu.ac3_servers.dev.bvotifier.bungee6.commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import eu.ac3_servers.dev.bvotifier.bungee.BVotifier;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class _BVCommand extends Command {
	
	private BVotifier plugin;

	public _BVCommand(BVotifier plugin) {
		super("BVotifier", null, new String[0]);
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(args.length == 1 && args[0].equalsIgnoreCase("metrics")){
			if(!sender.hasPermission("BVotifier.admin")) return;
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(this.plugin.metrics.getConfigFile()));
				String optout = properties.getProperty("opt-out");
				if(optout == null) return;
				if(optout.equalsIgnoreCase("false")){
					properties.setProperty("opt-out", "true");
					properties.store(new FileOutputStream(this.plugin.metrics.getConfigFile()), "http://mcstats.org");
					sender.sendMessage("Turned off plugin metrics for Votifier.");
				}else{
					properties.setProperty("opt-out", "false");
					properties.store(new FileOutputStream(this.plugin.metrics.getConfigFile()), "http://mcstats.org");
					sender.sendMessage("Turned on plugin metrics for Votifier.");
				}
			} catch (IOException e) {
				sender.sendMessage("Something went wrong. Please check the logs for more.");
				e.printStackTrace();
			}
			
		}else{
			
			sender.sendMessage("BVotifier by acecheesecr14!");
			sender.sendMessage("Version: " + this.plugin.getDescription().getVersion());
			if(!sender.hasPermission("BVotifier.admin")) return;
			
			sender.sendMessage("Toggle plugin metrics with the command");
			sender.sendMessage("/bvotifer metrics");
			
		}

	}

}
