package me.nlt.bavm.generator;

import me.nlt.bavm.teams.player.Position;

import java.util.Random;

public class RandomStats
{

    /**
     * Deze methode genereert willekeurige stats voor de spelers, gebaseerd op hun positie (0=keeper, 1=defender, 2=midfielder, 3=attacker)
     *
     * @param position Spelerspositie
     */
    public static double[] randomStats(Position position)
    {
        double randomStats[] = new double[6];

        Random rnd = new Random();
        double playerTalent = Math.random();

        while (true)
        {
            if (playerTalent > 0.657 && playerTalent < 0.957)
            {
                break;
            }

            if (playerTalent < 0.657)
            {
               playerTalent += Math.random();
            }
            else
            {
                playerTalent -= Math.random();
            }
        }

        int keeperBases[] = {20, 14, 30, 40, 30, 50};
        int defenderBases[] = {30, 20, 30, 50, 30, 16};
        int midfielderBases[] = {40, 40, 50, 30, 30, 12};
        int attackerBases[] = {50, 50, 20, 20, 30, 8};

        for (int i = 0; i < 6; i++)
        {
            //berekent de halfstat (helft van de stat)
            switch (position.getId())
            {
                case 0:
                    randomStats[i] = Math.round(((keeperBases[i] + rnd.nextInt((int) (keeperBases[i] * playerTalent) + 1)) * playerTalent) * 100) / 100;
                    break;
                case 1:
                    randomStats[i] = Math.round(((defenderBases[i] + rnd.nextInt((int) (defenderBases[i] * playerTalent) + 1)) * playerTalent) * 100) / 100;
                    break;
                case 2:
                    randomStats[i] = Math.round(((attackerBases[i] + rnd.nextInt((int) (attackerBases[i] * playerTalent) + 1)) * playerTalent) * 100) / 100;
                    break;
                case 3:
                    randomStats[i] = Math.round(((midfielderBases[i] + rnd.nextInt((int) (midfielderBases[i] * playerTalent) + 1)) * playerTalent) * 100) / 100;
                    break;
            }
        }

        return randomStats;
    }

    /**
     * Deze methode genereert willekeurige stats voor de spelers, gebaseerd op hun positie (0=keeper, 1=defender, 2=midfielder, 3=attacker)
     *
     * @param teamTalent Spelerspositie
     */
    public static double[] randomCStats(double teamTalent)
    {
        double randomCStats[] = new double[3];
        Random rnd = new Random();

        for (int i = 0; i < 3; i++)
        {
            randomCStats[i] = (25 + rnd.nextInt(43)) + (33 * teamTalent);
        }

        return randomCStats;
    }
}


