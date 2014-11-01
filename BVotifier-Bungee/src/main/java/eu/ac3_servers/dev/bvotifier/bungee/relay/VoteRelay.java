package eu.ac3_servers.dev.bvotifier.bungee.relay;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import eu.ac3_servers.dev.bvotifier.bungee.*;
import eu.ac3_servers.dev.bvotifier.bungee.configuration.VoteStorage;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
	
	@EventHandler
	public void onVote(VotifierEvent e){
	
		if(!this.plugin.relayEnabled){
			this.plugin.getLogger().warning("Vote received but not relayed!");
			return;
		}
	
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

		if (this.plugin.singleServerVote){
			// Only send to the server that the player is on (or default if not online)
			ProxiedPlayer pp = plugin.getProxy().getPlayer(vote.getUsername());
			ServerInfo server = null;
			if (pp != null){
				// send to player's server
				server = pp.getServer().getInfo();
			}else{
				// send to default server
				server = plugin.getProxy().getServerInfo(this.plugin.defaultVoteServer);
			}
			
			if (server != null){
				if(!this.plugin.emptysend && server.getPlayers().isEmpty()){
					this.plugin.getLogger().info("Server \"" + server.getName() + "\" has no players so vote was dropped.");
				}else{
					if(this.plugin.isDebug()) this.plugin.getLogger().info("Sending a vote to: " + server.getName() + " for " + vote.getUsername());
					server.sendData(BVotifier.channelName, b.toByteArray());
				}
			}else{
				this.plugin.getLogger().log(Level.WARNING, "Player is offline and default vote server does not exist!");
			}
		}else{
			// Send to all active servers
			Map<String, ServerInfo> servers = this.plugin.getProxy().getServers();
			ArrayList<ServerInfo> destinations = new ArrayList<ServerInfo>();
			for (ServerInfo server : servers.values()) {
				if(!this.plugin.emptysend && true == false && server.getPlayers().isEmpty()){
					//destinations.add(server);
					this.plugin.getLogger().info("Server \"" + server.getName() + "\" has no players so vote was dropped.");
				}else{
					if(this.plugin.isDebug()) this.plugin.getLogger().info("Sending a vote to: " + server.getName() + " for " + vote.getUsername());
					server.sendData(BVotifier.channelName, b.toByteArray());
				}
			}
			if(!destinations.isEmpty()){

				this.voteStorage.storeVote(vote, destinations.toArray(new ServerInfo[destinations.size()]));

			}
		}
	}

}
