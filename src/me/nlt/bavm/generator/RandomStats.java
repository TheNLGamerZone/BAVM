package me.nlt.bavm.generator;

import me.nlt.bavm.teams.player.Position;

import java.util.Random;

public class RandomStats {

	/**
	 Deze methode genereert willekeurige stats voor de spelers, gebaseerd op hun positie (0=keeper, 1=defender, 2=midfielder, 3=attacker)
	 @param position Spelerspositie
	 */
	public static double[] randomStats(Position position) {
		double randomStats[] = new double[6];

		Random rnd = new Random();
		double playerTalent = Math.random();

		int keeperBases[] = {20, 0, 30, 40, 25, 50};
		int defenderBases[] = {30, 20, 30, 50, 25, 0};
		int midfielderBases[] = {40, 40, 50, 30, 25, 0};
		int attackerBases[] = {50, 50, 20, 20, 25, 0};

		for (int i = 0; i < 6; i++) {
			//berekent de halfstat (helft van de stat)
			switch (position.getId()) {
				case 0 :
					int randomPartK = 68 - keeperBases[i];
					randomStats[i] =  Math.round(((keeperBases[i] + rnd.nextInt(randomPartK)) + (33 * playerTalent)) * 100) / 100;
					break;
				case 1 :
					int randomPartD = 68 - defenderBases[i];
					randomStats[i] =  Math.round(((defenderBases[i] + rnd.nextInt(randomPartD)) + (33 * playerTalent)) * 100) / 100;
					break;
				case 2 :
					int randomPartM = 68 - midfielderBases[i];
					randomStats[i] =  Math.round(((midfielderBases[i] + rnd.nextInt(randomPartM)) + (33 * playerTalent)) * 100) / 100;
					break;
				case 3 :
					int randomPartA = 68 - attackerBases[i];
					randomStats[i] = Math.round(((attackerBases[i] + rnd.nextInt(randomPartA)) + (33 * playerTalent)) * 100) / 100;
                    break;
			}
		}
		
		return randomStats;
	}
	/**
	 Deze methode genereert willekeurige stats voor de spelers, gebaseerd op hun positie (0=keeper, 1=defender, 2=midfielder, 3=attacker)
	 @param teamTalent Spelerspositie
	 */
	public static double[] randomCStats(double teamTalent) {
		double randomCStats[] = new double[3];
		Random rnd = new Random();

		for (int i = 0; i < 3; i++) {
			randomCStats[i] = (25 + rnd.nextInt(43)) + (33 * teamTalent);
		}

		return randomCStats;
	}
}


