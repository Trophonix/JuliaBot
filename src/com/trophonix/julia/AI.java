package com.trophonix.julia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;

public class AI {

	private int task;

	private Location current;
	private Location last;

	private final Map<Block, Integer> visits = new HashMap<Block, Integer>();

	public AI(Location start) {
		this.current = start;

		init();
	}

	public void init() {
		current.getBlock().setType(Material.DIAMOND_BLOCK);
		Bukkit.broadcastMessage(ChatColor.BLUE + "[Julia] " + ChatColor.YELLOW
				+ "Hello, my name is Julia, and I am here to " + ChatColor.DARK_RED + "WIN THIS RACE");
		current.getWorld().playSound(current, Sound.AMBIENCE_THUNDER, 1, 0.5F);
	}

	public void begin() {
		Bukkit.broadcastMessage(ChatColor.BLUE + "[Julia] " + ChatColor.YELLOW + "Let's do this!");
		last = current;
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (!visits.containsKey(current.getBlock())) {
					visits.put(current.getBlock(), 1);
				} else {
					visits.put(current.getBlock(), visits.get(current.getBlock()) + 1);
				}

				Location next = randDir(current, last);
				Location possibleNext = getPossibleNext();

				if (possibleNext != null)
					next = possibleNext;

				last = current;
				current = next;

				last.getBlock().setType(Material.AIR);
				current.getBlock().setType(Material.DIAMOND_BLOCK);

				if (current.clone().add(0, -1, 0).getBlock().getType().equals(Material.GOLD_BLOCK)) {
					Bukkit.getScheduler().cancelTask(task);
					Bukkit.broadcastMessage(ChatColor.BLUE + "[Julia] " + ChatColor.YELLOW + "I've won the maze!");
					current.getBlock().setType(Material.AIR);
					task = -1;
				}
			}
		}, 0L, 20L);
	}

	public void stop() {
		if (task != -1)
			Bukkit.getScheduler().cancelTask(task);

		Bukkit.broadcastMessage(ChatColor.BLUE + "[Julia] " + ChatColor.YELLOW + "Goodbye.");
		current.getBlock().setType(Material.AIR);
		task = -1;
	}

	public Location randDir(Location base, Location previous) {
		List<Location> testable = new ArrayList<Location>();
		for (int x = -1; x < 3; x += 2) {
			Location test = base.clone().add(x, 0, 0);
			if (test.getBlock().equals(previous.getBlock()))
				continue;
			if (test.getBlock().getType().equals(Material.AIR)) {
				testable.add(test);
			}
		}

		for (int z = -1; z < 3; z += 2) {
			Location test = base.clone().add(0, 0, z);
			if (test.getBlock().equals(previous.getBlock()))
				continue;
			if (test.getBlock().getType().equals(Material.AIR)) {
				testable.add(test);
			}
		}

		if (testable.size() == 0)
			return previous;
		else if (testable.size() == 1)
			return testable.get(0);
		else {
			for (int i = 0; i < randInt(2, 5); i++) {
				Collections.shuffle(testable);
			}
			Location next = testable.get(0);
			for (int i = 0; i < testable.size(); i++) {
				int test;
				if (!visits.containsKey(testable.get(i).getBlock()))
					test = 0;
				else
					test = visits.get(testable.get(i).getBlock());

				int test2;
				if (!visits.containsKey(next.getBlock()))
					test2 = 0;
				else
					test2 = visits.get(next.getBlock());

				if (test <= test2)
					next = testable.get(i);

			}
			return next;
		}

	}

	public Location getPossibleNext() {
		Location possibleNext = null;

		for (int x = -1; x > -6; x--) {
			if (!current.clone().add(x, 0, 0).getBlock().getType().equals(Material.AIR)) {
				possibleNext = null;
				break;
			}

			Block test = current.clone().add(x, -1, 0).getBlock();
			if (test.getType().equals(Material.GOLD_BLOCK)) {
				possibleNext = current.clone().add(-1, 0, 0);
				break;
			}
		}

		if (possibleNext == null) {
			for (int x2 = 1; x2 < 6; x2++) {
				if (!current.clone().add(x2, 0, 0).getBlock().getType().equals(Material.AIR)) {
					possibleNext = null;
					break;
				}

				Block test = current.clone().add(x2, -1, 0).getBlock();
				if (test.getType().equals(Material.GOLD_BLOCK)) {
					possibleNext = current.clone().add(1, 0, 0);
					break;
				}
			}
		}

		if (possibleNext == null) {
			for (int z = -1; z > -6; z--) {
				if (!current.clone().add(0, 0, z).getBlock().getType().equals(Material.AIR)) {
					possibleNext = null;
					break;
				}

				Block test = current.clone().add(0, -1, z).getBlock();
				if (test.getType().equals(Material.GOLD_BLOCK)) {
					possibleNext = current.clone().add(0, 0, -1);
					break;
				}
			}
		}

		if (possibleNext == null) {
			for (int z = 1; z < 6; z++) {
				if (!current.clone().add(0, 0, z).getBlock().getType().equals(Material.AIR)) {
					possibleNext = null;
					break;
				}

				Block test = current.clone().add(0, -1, z).getBlock();
				if (test.getType().equals(Material.GOLD_BLOCK)) {
					possibleNext = current.clone().add(0, 0, 1);
					break;
				}
			}
		}

		return possibleNext;
	}

	public int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

}
