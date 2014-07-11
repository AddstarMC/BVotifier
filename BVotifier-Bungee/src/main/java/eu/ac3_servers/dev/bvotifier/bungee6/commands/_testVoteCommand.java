package eu.ac3_servers.dev.bvotifier.bungee6.commands;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import eu.ac3_servers.dev.bvotifier.bungee.BVotifier;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class _testVoteCommand extends Command implements TabExecutor {
	
	private BVotifier plugin;

	public _testVoteCommand(BVotifier plugin) {
		super("BVTestVote", "BVotifier.test", new String[]{"BVTV", "Bungeevotifiertestvote"});
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 1){
			Vote vote = new Vote("Fake Vote[BC]", args[0], "132.123.123.1", (new StringBuilder()).append(System.currentTimeMillis()).toString());
			VotifierEvent voteEvent = new VotifierEvent(vote);
			this.plugin.getProxy().getPluginManager().callEvent(voteEvent);
			sender.sendMessage("Fake vote sent.");
		}else if(args.length == 2){
			Vote vote = new Vote(args[1], args[0], "132.123.123.1", (new StringBuilder()).append(System.currentTimeMillis()).toString());
			VotifierEvent voteEvent = new VotifierEvent(vote);
			this.plugin.getProxy().getPluginManager().callEvent(voteEvent);
			sender.sendMessage("Fake vote sent.");
		}else{
			sender.sendMessage("Incorrect usage! /bvtv <Username> [Service name]");
			sender.sendMessage("Click here to try again.");
		}
	}
	
    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args)
    {
        if ( args.length != 1 )
        {
            return ImmutableSet.of();
        }

        Set<String> matches = new HashSet<>();
        if ( args.length == 1 )
        {
            String search = args[0].toLowerCase();
            for ( ProxiedPlayer player : ProxyServer.getInstance().getPlayers() )
            {
                if ( player.getName().toLowerCase().startsWith( search ) )
                {
                    matches.add( player.getName() );
                }
            }
        }
		return ImmutableSet.of();
    }
}
