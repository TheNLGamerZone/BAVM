package me.nlt.bavm.teams.team;

import java.util.ArrayList;

public class TeamInfo {
    private ArrayList<Integer> playerIDList = new ArrayList<>();
    private int coachID;

    public TeamInfo(int[] playerIDs, int coachID) {
        for (int i : playerIDs) {
            playerIDList.add(i);
        }

        this.coachID = coachID;
    }

    public ArrayList<Integer> getPlayerIDList() {
        return this.playerIDList;
    }

    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String infoString;

        // String maken met stats
        for (int i : playerIDList)
        {
            stringBuilder.append(i + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        infoString = stringBuilder.toString();

        String playerString = "PlayerIDs{" +
                infoString +
                "}";

        String coachString = "CoachID{" +
                coachID +
                "}";

        return playerString + "," + coachString;
    }
}
