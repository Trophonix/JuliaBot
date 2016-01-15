package com.trophonix.julia;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJULIA implements CommandExecutor {

	public AI ai;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if (args.length == 1) {
			switch (args[0]) {
			case "on":
				ai = new AI(player.getLocation());				
				break;
			case "begin":
				ai.begin();
				break;
			case "off":
				ai.stop();
				break;
			}
		}
		
		return true;
	}
	
}
