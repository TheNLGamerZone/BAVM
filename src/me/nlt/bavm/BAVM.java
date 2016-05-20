package me.nlt.bavm;

import me.nlt.bavm.league.RandomNames;

public class BAVM
{
    /**
     * Main method
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        RandomNames randomNames = new RandomNames();

        String BALTeamNames[] = randomNames.getBALTeamNames();

        for (int i = 0; i < 20; i++) {
            System.out.printf("%d. %s%n", i + 1,BALTeamNames[i]);
        }
    }
}
