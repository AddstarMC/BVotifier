package eu.ac3_servers.dev.bvotifier.bungee.commands;

//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Properties;

import eu.ac3_servers.dev.bvotifier.bungee.BVotifier;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BVCommand extends Command {
	
	private eu.ac3_servers.dev.bvotifier.bungee.BVotifier plugin;

	public BVCommand(BVotifier plugin) {
		super("BVotifier", null, new String[0]);
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(args.length == 1 && args[0].equalsIgnoreCase("metrics")){
			if(!sender.hasPermission("BVotifier.admin")) return;
//			Properties properties = new Properties();
//			try {
//				properties.load(new FileInputStream(this.plugin.metrics.getConfigFile()));
//				String optout = properties.getProperty("opt-out");
//				if(optout == null) return;
//				if(optout.equalsIgnoreCase("false")){
//					properties.setProperty("opt-out", "true");
//					properties.store(new FileOutputStream(this.plugin.metrics.getConfigFile()), "http://mcstats.org");
//					sender.sendMessage("Turned off plugin metrics for Votifier.");
//				}else{
//					properties.setProperty("opt-out", "false");
//					properties.store(new FileOutputStream(this.plugin.metrics.getConfigFile()), "http://mcstats.org");
//					sender.sendMessage("Turned on plugin metrics for Votifier.");
//				}
//			} catch (IOException e) {
//				TextComponent tcErrr = new TextComponent("Something went wrong. Please check the logs for more.");
//				tcErrr.setColor(ChatColor.RED);
//				tcErrr.setBold(true);
//				sender.sendMessage(tcErrr);
//				e.printStackTrace();
//			}
			sender.sendMessage(ChatColor.RED.toString() + "Metrics is currently \"jammed on\" in this version of BVotifier!");
			
		}else{
			
			TextComponent tc0 = new TextComponent("BVotifier by acecheesecr14!");
			TextComponent tc1 = new TextComponent("Version: " + this.plugin.getDescription().getDescription());
			
			tc0.setColor(ChatColor.BLUE);
			tc1.setColor(ChatColor.BLUE);
			
			sender.sendMessage(tc0);
			sender.sendMessage(tc1);
/*			if(!sender.hasPermission("BVotifier.admin")) return;
			
			TextComponent tc2 = new TextComponent("Toggle plugin metrics with the command");
			TextComponent tc3 = new TextComponent("/bvotifer metrics");
			tc3.setColor(ChatColor.GREEN);
			tc2.setColor(ChatColor.GREEN);
			
			sender.sendMessage(tc2);
			sender.sendMessage(tc3);*/
			
		}

	}

}
