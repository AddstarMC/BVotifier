package eu.ac3_servers.dev.bvotifier.bungee;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import com.vexsoftware.votifier.crypto.RSAIO;
import com.vexsoftware.votifier.crypto.RSAKeygen;

import eu.ac3_servers.dev.bvotifier.bungee.relay.VoteRelay;
import eu.ac3_servers.dev.bvotifier.bungee.net.VoteReceiver;
import eu.ac3_servers.dev.bvotifier.bungee.commands.*;
import eu.ac3_servers.dev.bvotifier.bungee.configuration.BVConfig;
import eu.ac3_servers.dev.bvotifier.bungee.model.VoteListener;
import eu.ac3_servers.dev.bvotifier.bungee6.commands._BVCommand;
import eu.ac3_servers.dev.bvotifier.bungee6.commands._testVoteCommand;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;


/**
 * The main Votifier plugin class.
 * 
 * @author Cory Redmond
 * @author Blake Beaupain
 * @author Kramer Campbell
 */
public class BVotifier extends Plugin {

	/** The Votifier instance. */
	private static BVotifier instance;

	public static String channelName = "BVotifier";

	/** The current Votifier version. */
	private String version;

	/** The vote listeners. */
	private final List<VoteListener> listeners = new ArrayList<VoteListener>();

	/** The vote receiver. */
	private VoteReceiver voteReceiver;

	/** The RSA key pair. */
	private KeyPair keyPair;

	/** Debug mode flag */
	private boolean debug;

	private BVConfig cfg;

	public boolean threshold;

	public long thresholdTime;

	public Metrics metrics;

	private ScheduledTask voteReceiverTask;
	
	@Override
	public void onEnable() {
		BVotifier.instance = this;
		
		getLogger().info("Starting metrics.");
		try {
		    this.metrics = new Metrics(this);
		    this.metrics.start();
		    getLogger().info("Started metrics.");
		} catch (IOException e) {
			getLogger().severe("Starting metrics has not succeeded!");
			e.printStackTrace();
		}
		
		version = getDescription().getVersion();
		
		getProxy().registerChannel(channelName);
		
		try {
			File dataFolder = getDataFolder();
			if(!dataFolder.exists()) dataFolder.mkdir();
			File rsaFolder = new File(dataFolder, "rsa");
			if(!rsaFolder.exists()){
				rsaFolder.mkdir();
				keyPair = RSAKeygen.generate(2048);
				RSAIO.save(rsaFolder, keyPair);
			}else{
				keyPair = RSAIO.load(rsaFolder);
				getLogger().info("Loaded RSA files.");
			}
		} catch (Exception e) {
			gracefulExit();
			e.printStackTrace();
		}
		
		
		this.cfg = new BVConfig(this);
		
		String host = cfg.getConfig().getString("bungee.hostname");
		int port = cfg.getConfig().getInt("bungee.port");
		debug = cfg.getConfig().getBoolean("bungee.debugging");
		this.threshold = cfg.getConfig().getBoolean("bungee.enableThreshold");
		this.thresholdTime = cfg.getConfig().getLong("bungee.thresholdTime");

		if (debug)
			getLogger().info("DEBUG mode enabled!");
		
		// Initialize the receiver.
		try {
			voteReceiver = new VoteReceiver(this, host, port);
			voteReceiver.initialize();
			voteReceiverTask = getProxy().getScheduler().runAsync(this, voteReceiver);
			if(cfg.getConfig().getBoolean("StartupOptions.v16")){
				getProxy().getPluginManager().registerCommand(this, new testVoteCommand(this));
			}else{
				getProxy().getPluginManager().registerCommand(this, new _testVoteCommand(this));
			}
		
			//Register the vote relay..
			getProxy().getPluginManager().registerListener(this, new VoteRelay(this));
			
			getLogger().info("Votifier enabled.");
		} catch (Exception ex) {
			gracefulExit();
			return;
		}
		
		if(cfg.getConfig().getBoolean("StartupOptions.v1_6")){
			getProxy().getPluginManager().registerCommand(this, new BVCommand(this));
		}else{
			getProxy().getPluginManager().registerCommand(this, new _BVCommand(this));
		}
		
	}
	
	@Override
	public void onDisable() {
		
		voteReceiver.shutdown();
		voteReceiverTask.cancel();
		
	}

	private void gracefulExit() {
		getLogger().severe("Votifier did not initialize properly!");
		voteReceiver.shutdown();
		voteReceiverTask.cancel();
	}

	/**
	 * Gets the instance.
	 * 
	 * @return The instance
	 */
	public static BVotifier getInstance() {
		return instance;
	}

	/**
	 * Gets the version.
	 * 
	 * @return The version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the listeners.
	 * 
	 * @return The listeners
	 */
	public List<VoteListener> getListeners() {
		return listeners;
	}

	/**
	 * Gets the vote receiver.
	 * 
	 * @return The vote receiver
	 */
	public VoteReceiver getVoteReceiver() {
		return voteReceiver;
	}

	/**
	 * Gets the keyPair.
	 * 
	 * @return The keyPair
	 */
	public KeyPair getKeyPair() {
		return keyPair;
	}

	public boolean isDebug() {
		return debug;
	}
	
}
