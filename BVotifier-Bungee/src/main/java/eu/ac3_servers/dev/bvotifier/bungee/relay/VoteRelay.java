package eu.ac3_servers.dev.bvotifier.bungee.relay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import eu.ac3_servers.dev.bvotifier.bungee.*;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VoteRelay implements Listener {
	
	private BVotifier plugin;

	public VoteRelay(BVotifier plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVote(VotifierEvent e){
		Vote vote = e.getVote();		
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		
		try {
			out.writeUTF(vote.getAddress());
			out.writeUTF(vote.getServiceName());
			out.writeUTF(vote.getTimeStamp());
			out.writeUTF(vote.getUsername());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();
		
		for (String serverName : servers.keySet()) {
			if(this.plugin.isDebug()) this.plugin.getLogger().info("Sending a vote to: " + serverName + "for " + vote.getUsername());
			servers.get(serverName).sendData(BVotifier.channelName, b.toByteArray());
		}
	}

}
