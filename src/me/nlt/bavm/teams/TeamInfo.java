package me.nlt.bavm.teams;

import java.util.ArrayList;

public class TeamInfo {
    private ArrayList<Integer> playerIDSet = new ArrayList<>();
    private int coachID;

    public TeamInfo(int[] playerIDList, int coachID) {
        for (int i : playerIDList) {
            playerIDSet.add(i);
        }

        this.coachID = coachID;
    }


    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String infoString;

        // String maken met stats
        for (int i : playerIDSet)
        {
            stringBuilder.append(i + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        infoString = stringBuilder.toString();

        String playerString = "PlayerStats{" +
                infoString +
                "}";

        String coachString = "Coach{" +
                coachID +
                "}";

        return playerString + "," + coachString;
    }
}
