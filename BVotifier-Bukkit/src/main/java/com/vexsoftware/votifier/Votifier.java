package com.vexsoftware.votifier;

import org.bukkit.plugin.java.JavaPlugin;

import eu.ac3_servers.dev.bvotifier.bukkit.PMListener;

public abstract class Votifier extends JavaPlugin {

	public PMListener pmlistener;
	
	public PMListener getVoteReceiver(){
		
		return pmlistener;
		
	}
	
}
