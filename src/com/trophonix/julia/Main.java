package com.trophonix.julia;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	
	@Override
	public void onEnable() {
		System.out.println("[Julia] Initializing...");
		try {
			instance = this;
			getCommand("julia").setExecutor(new CommandJULIA());
			System.out.println("[Julia] Successfully initialized!");
		} catch (Exception ex) {
			System.out.println("[Julia] Something went wrong!");
			ex.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}
