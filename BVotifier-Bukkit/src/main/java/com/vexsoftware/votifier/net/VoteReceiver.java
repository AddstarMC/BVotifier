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

package com.vexsoftware.votifier.net;

import java.util.logging.*;

import com.vexsoftware.votifier.Votifier;

/**
 * The vote receiving server.
 * 
 * @author Blake Beaupain
 * @author Kramer Campbell
 */
public class VoteReceiver extends Thread {
	private static final Logger LOG = Logger.getLogger("Votifier");
	private final Votifier plugin;

	public VoteReceiver(final Votifier plugin, String host, int port) throws Exception {
		this.plugin = plugin;
		LOG.log(Level.INFO, "VoteReceiver construction has been called.");
		if (plugin.doStackTrace()) new Throwable().printStackTrace();
	}

	public void shutdown() {
		LOG.log(Level.INFO, "VoteReceiver.shutdown() has been called.");
		if (plugin.doStackTrace()) new Throwable().printStackTrace();
	}

	@Override
	public void run() {
		LOG.log(Level.INFO, "VoteReceiver.run() has been called.");
		if (plugin.doStackTrace()) new Throwable().printStackTrace();
		plugin.setDoStackTrace(true);
	}
}
