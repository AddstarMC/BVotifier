/*
 * Copyright (C) 2012 Vex Software LLC
 * This file is part of Votifier.
 * 
 * Votifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Votifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Votifier.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.ac3_servers.dev.bvotifier.bungee.net;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;

import com.vexsoftware.votifier.crypto.RSA;

import eu.ac3_servers.dev.bvotifier.bungee.*;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
/**
 * The vote receiving server.
 * 
 * @author Cory Redmond
 * @author Blake Beaupain
 * @author Kramer Campbell
 */
public class VoteReceiver implements Runnable {
	
	private Map<String, Long> threshold = new HashMap<String, Long>();
	
	private final BVotifier plugin;

	/** The host to listen on. */
	private final String host;

	/** The port to listen on. */
	private final int port;

	/** The server socket. */
	private ServerSocket server;

	/** The running flag. */
	private boolean running = true;

	/**
	 * Instantiates a new vote receiver.
	 * 
	 * @param host
	 *            The host to listen on
	 * @param port
	 *            The port to listen on
	 */
	public VoteReceiver(final BVotifier plugin, String host, int port)
			throws Exception {
		this.plugin = plugin;
		this.host = host;
		this.port = port;

		initialize();
	}

	public void initialize() throws Exception {
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress(host, port));
		} catch (Exception ex) {
			plugin.getLogger().severe("Error initializing vote receiver. Please verify that the configured");
			plugin.getLogger().severe("IP address and port are not already in use. This is a common problem");
			plugin.getLogger().severe("with hosting services and, if so, you should check with your hosting provider." + 
					ex);
			throw new Exception(ex);
		}
	}

	/**
	 * Shuts the vote receiver down cleanly.
	 */
	public void shutdown() {
		running = false;
		if (server == null)
			return;
		try {
			server.close();
		} catch (Exception ex) {
			plugin.getLogger().warning("Unable to shut down vote receiver cleanly.");
		}
	}

	public void run() {

		// Main loop.
		while (running) {
			try {
				Socket socket = server.accept();
				socket.setSoTimeout(3000); // Don't hang on slow connections.
				if(this.plugin.threshold){
					String ip = socket.getInetAddress().getHostAddress();
					if((System.currentTimeMillis() - threshold.get(ip)) < this.plugin.thresholdTime){
						this.plugin.getLogger().warning("Too many packets from " + ip);
						return;
					}
					threshold.put(ip, System.currentTimeMillis());
				}
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream()));
				InputStream in = socket.getInputStream();

				// Send them our version.
				writer.write("VOTIFIER " + BVotifier.getInstance().getVersion());
				writer.newLine();
				writer.flush();

				// Read the 256 byte block.
				byte[] block = new byte[256];
				in.read(block, 0, block.length);

				// Decrypt the block.
				block = RSA.decrypt(block, BVotifier.getInstance().getKeyPair()
						.getPrivate());
				int position = 0;

				// Perform the opcode check.
				String opcode = readString(block, position);
				position += opcode.length() + 1;
				if (!opcode.equals("VOTE")) {
					// Something went wrong in RSA.
					throw new Exception("Unable to decode RSA");
				}

				// Parse the block.
				String serviceName = readString(block, position);
				position += serviceName.length() + 1;
				String username = readString(block, position);
				position += username.length() + 1;
				String address = readString(block, position);
				position += address.length() + 1;
				String timeStamp = readString(block, position);
				position += timeStamp.length() + 1;

				// Create the vote.
				final Vote vote = new Vote(serviceName, username, address, timeStamp);

				if (plugin.isDebug())
					plugin.getLogger().info("Received vote record -> " + vote);

				// Dispatch the vote to all listeners.
				for (VoteListener listener : BVotifier.getInstance()
						.getListeners()) {
					try {
						listener.voteMade(vote);
					} catch (Exception ex) {
						String vlName = listener.getClass().getSimpleName();
						plugin.getLogger().warning("Exception caught while sending the vote notification to the '"
										+ vlName + "' listener" + ex);
					}
				}

				// Call event in a synchronized fashion to ensure that the
				// custom event runs in the
				// the main server thread, not this one.
				plugin.getProxy().getScheduler()
						.schedule(plugin, new Runnable() {
							public void run() {
								plugin.getProxy().getPluginManager()
										.callEvent(new VotifierEvent(vote));
							}
						}, 0, TimeUnit.SECONDS);

				// Clean up.
				writer.close();
				in.close();
				socket.close();
			} catch (SocketException ex) {
				plugin.getLogger().warning("Protocol error. Ignoring packet - "
						+ ex.getLocalizedMessage());
			} catch (BadPaddingException ex) {
				plugin.getLogger().warning("Unable to decrypt vote record. Make sure that that your public key");
				plugin.getLogger().warning("matches the one you gave the server list." + ex);
			} catch (Exception ex) {
				plugin.getLogger().warning("Exception caught while receiving a vote notification" +
						ex);
			}
		}
	}

	/**
	 * Reads a string from a block of data.
	 * 
	 * @param data
	 *            The data to read from
	 * @return The string
	 */
	private String readString(byte[] data, int offset) {
		StringBuilder builder = new StringBuilder();
		for (int i = offset; i < data.length; i++) {
			if (data[i] == '\n')
				break; // Delimiter reached.
			builder.append((char) data[i]);
		}
		return builder.toString();
	}
	
}
