package eu.ac3_servers.dev.bvotifier.bungee.configuration;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.google.common.io.ByteStreams;

import eu.ac3_servers.dev.bvotifier.bungee.BVotifier;
import eu.ac3_servers.dev.bvotifier.bungee.model.Vote;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class VoteStorage {

	private Plugin plugin;
	private File file;
	private Configuration config;

	public VoteStorage(Plugin plugin) {
		
		this.plugin = plugin;
		
		this.file = new File(getDataFolder(), "votes.yml");
		
		if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!file.exists()) {
            try {
            	file.createNewFile();
                try (InputStream is = getResourceAsStream("votes.yml");
                     OutputStream os = new FileOutputStream(file)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create storage file", e);
            }
        }
	}
	
	public Configuration getConfig(){
		try {
			return (this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveConfig(){
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "votes.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().severe("Couldn't save storage file!");
		}
	}
	
	private File getDataFolder(){
		return plugin.getDataFolder();
	}
	
	private InputStream getResourceAsStream(String file){
		return plugin.getResourceAsStream(file);
	}
	
	public void storeVote(Vote vote, ServerInfo... destination){
		
		for (ServerInfo serverInfo : destination) {
			getConfig().set("votes."+ serverInfo.getName() + "." + vote.getTimeStamp() + ".name", vote.getUsername());
			getConfig().set("votes."+ serverInfo.getName() + "." + vote.getTimeStamp() + ".address", vote.getAddress());
			getConfig().set("votes."+ serverInfo.getName() + "." + vote.getTimeStamp() + ".service", vote.getServiceName());
			BVotifier.getInstance().d("Stored", vote.toString());
		}
		saveConfig();
		
	}
	
	public void loadVotes(ServerInfo serverInfo){
		
		this.plugin.getProxy().getScheduler().runAsync(plugin, new LoadVoteTask(this.plugin, getConfig().getSection("votes."+serverInfo.getName()), serverInfo));
		return;
		
	}
}

class LoadVoteTask implements Runnable {
	
	private Configuration section;
	@SuppressWarnings("unused")
	private Plugin plugin;
	private ServerInfo server;

	public LoadVoteTask(Plugin plugin, Configuration section, ServerInfo server) {
		
		this.plugin = plugin;
		this.section = section;
		this.server = server;
		
	}

	@Override
	public void run() {
		String serverName = server.getName();
		Collection<String> index = section.getKeys();
		for (String time : index) {
			Vote vote = new Vote(
					section.getString(serverName + "." + time + ".service"),
					section.getString(serverName + "." + time + ".name"),
					section.getString(serverName + "." + time + ".address"),
					time
			);
			BVotifier.getInstance().d(vote.toString());
			
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
			if(!server.getPlayers().isEmpty()){
				
				server.sendData(BVotifier.channelName, b.toByteArray());
				
			}
		}
		
	}
	
}