package me.nlt.bavm.league;

import me.nlt.bavm.teams.Player;

public class RandomPlayer {
	public static void createRandomPlayers(int amount) {
		RandomNames rn = new RandomNames();
		
		for (int i = 0; i < amount; i++) {
			Player player = new Player(rn.getPeopleName(), i, RandomStats.randomStats(""));
		}
	}
}
