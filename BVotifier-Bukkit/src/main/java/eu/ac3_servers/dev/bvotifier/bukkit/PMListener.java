package eu.ac3_servers.dev.bvotifier.bukkit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class PMListener implements PluginMessageListener {
	
	private BVotifier plugin;

	public PMListener(BVotifier plugin) {
		this.plugin = plugin;
		this.plugin.getLogger().info("[BV] Registered the PluginMessageListener.");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		
		boolean debug = this.plugin.getBVConfig().getBoolean("bukkit.debug");
		if(debug) this.plugin.getLogger().info("[DEBUG] Message received on: " + channel);
		if(!channel.equals(this.plugin.MessageChannel)) return;
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		try {
			//while ((msg = in.readUTF()) != null) this.plugin.getLogger().info(msg);
			
			String address = in.readUTF();
			String serviceName = in.readUTF();
			String timeStamp = in.readUTF();
			String username = in.readUTF();
			Vote vote = new Vote();
			vote.setAddress(address);
			vote.setServiceName(serviceName);
			vote.setTimeStamp(timeStamp);
			vote.setUsername(username);
			
			this.plugin.getServer().getPluginManager().callEvent(new VotifierEvent(vote));
			this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new VoifierListener(vote, plugin));
			this.plugin.getLogger().info(vote.toString());
			
		} catch (IOException e) {
			e.printStackTrace();		
		}
	}

}
