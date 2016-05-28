package me.nlt.bavm.league;

import me.nlt.bavm.teams.Player;

public class RandomPlayer {
	public static void createRandomPlayers(int amount23s, double[] placeRatios) {
		int idCounter = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < (placeRatios[i] * amount23s); j++) {
				Player player = new Player(RandomNames.getPeopleName(), idCounter, RandomStats.randomStats(i));
				idCounter++;
				System.out.println("Van player: " + player.toString());
			}
		}
	}
}
