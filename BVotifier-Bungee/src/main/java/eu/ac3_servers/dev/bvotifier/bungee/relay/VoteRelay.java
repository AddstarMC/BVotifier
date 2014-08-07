package eu.ac3_servers.dev.bvotifier.bungee.relay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import eu.ac3_servers.dev.bvotifier.bungee.*;
import eu.ac3_servers.dev.bvotifier.bungee.configuration.VoteStorage;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VoteRelay implements Listener {
	
	private BVotifier plugin;
	private VoteStorage voteStorage;

	public VoteRelay(BVotifier plugin) {
		this.plugin = plugin;
		
		this.voteStorage = new VoteStorage(plugin);
	}
	
	@EventHandler
	public void onServerChange(ServerSwitchEvent e){
		ServerInfo serverInfo = e.getPlayer().getServer().getInfo();
		voteStorage.loadVotes(serverInfo);
	}
	
	@SuppressWarnings("unused")
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
		ArrayList<ServerInfo> destinations = new ArrayList<ServerInfo>();
		for (ServerInfo server : servers.values()) {
			if(false == true && server.getPlayers().isEmpty()){
				destinations.add(server);
				if(this.plugin.isDebug()) this.plugin.getLogger().info("Server: " + server.getName() + " has no players so queued");
			}else{
				if(this.plugin.isDebug()) this.plugin.getLogger().info("Sending a vote to: " + server.getName() + "for " + vote.getUsername());
				server.sendData(BVotifier.channelName, b.toByteArray());
			}
		}
		if(!destinations.isEmpty()){
			
			this.voteStorage.storeVote(vote, destinations.toArray(new ServerInfo[destinations.size()]));
			
		}
	}

}
