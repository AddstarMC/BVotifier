package eu.ac3_servers.dev.bvotifier.bukkit;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.vexsoftware.votifier.Votifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;

public class VoifierListener implements Runnable {
	
	private Vote vote;
	private Plugin plugin;

	public VoifierListener(Vote vote, Plugin plugin) {
		
		this.vote = vote;
		this.plugin = plugin;
		
	}

	@Override
	public void run() {
		
		for (VoteListener listener : Votifier.getInstance()
				.getListeners()) {
			try {
				listener.voteMade(vote);
			} catch (Exception ex) {
				String vlName = listener.getClass().getSimpleName();
				plugin.getLogger().log(Level.WARNING,
						"Exception caught while sending the vote notification to the '"
								+ vlName + "' listener", ex);
			}
		}
		
	}

}
