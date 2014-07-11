package eu.ac3_servers.dev.bvotifier.bungee.commands;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import eu.ac3_servers.dev.bvotifier.bungee.BVotifier;
import eu.ac3_servers.dev.bvotifier.bungee.model.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class testVoteCommand extends Command implements TabExecutor {
	
	private BVotifier plugin;

	public testVoteCommand(BVotifier plugin) {
		super("BVTestVote", "BVotifier.test", new String[]{"BVTV", "Bungeevotifiertestvote"});
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 1){
			Vote vote = new Vote("Fake Vote[BC]", args[0], "132.123.123.1", (new StringBuilder()).append(System.currentTimeMillis()).toString());
			VotifierEvent voteEvent = new VotifierEvent(vote);
			this.plugin.getProxy().getPluginManager().callEvent(voteEvent);
			TextComponent message0 = new TextComponent("Fake vote sent.");
			message0.setColor(ChatColor.BLUE);
			sender.sendMessage(message0);
		}else if(args.length == 2){
			Vote vote = new Vote(args[1], args[0], "132.123.123.1", (new StringBuilder()).append(System.currentTimeMillis()).toString());
			VotifierEvent voteEvent = new VotifierEvent(vote);
			this.plugin.getProxy().getPluginManager().callEvent(voteEvent);
			TextComponent message0 = new TextComponent("Fake vote sent.");
			message0.setColor(ChatColor.BLUE);
			sender.sendMessage(message0);
		}else{
			TextComponent Message0 = new TextComponent("Incorrect usage! /bvtv <Username> [Service name]");
			Message0.setColor(ChatColor.RED);
			TextComponent Message1 = new TextComponent("Click here to try again.");
			Message1.setColor(ChatColor.GREEN);
			Message1.setUnderlined(true);
			Message1.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/bvtv "));
			sender.sendMessage(Message0);
			sender.sendMessage(Message1);
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
