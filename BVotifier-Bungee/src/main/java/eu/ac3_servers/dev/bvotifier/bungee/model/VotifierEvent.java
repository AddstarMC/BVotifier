package eu.ac3_servers.dev.bvotifier.bungee.model;

import net.md_5.bungee.api.plugin.Event;

/**
 * {@code VotifierEvent} is a custom Bukkit event class that is sent
 * synchronously to CraftBukkit's main thread allowing other plugins to listener
 * for votes.
 * 
 * NOW SENT TO BUNGEECORDS MAIN THREAD ;P
 * 
 * @author Cory Redmond
 * @author frelling
 * 
 */
public class VotifierEvent extends Event {

	/**
	 * Encapsulated vote record.
	 */
	private Vote vote;

	/**
	 * Constructs a vote event that encapsulated the given vote record.
	 * 
	 * @param vote
	 *            vote record
	 */
	public VotifierEvent(final Vote vote) {
		this.vote = vote;
	}

	/**
	 * Return the encapsulated vote record.
	 * 
	 * @return vote record
	 */
	public Vote getVote() {
		return vote;
	}
}
