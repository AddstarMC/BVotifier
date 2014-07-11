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
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		
		boolean debug = this.plugin.getConfig().getBoolean("bukkit.debug");
		if(debug) this.plugin.getLogger().info("Message received on: " + channel);
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
			this.plugin.getLogger().info(vote.toString());
			
		} catch (IOException e) {
			e.printStackTrace();		
		}
	}

}
