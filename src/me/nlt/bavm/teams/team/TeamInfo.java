package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;

public class TeamInfo
{
    private ArrayList<Player> teamPlayerList = new ArrayList<>();
    private Coach teamCoach;
    private TeamCoefficients teamCoefficients;

    public TeamInfo(int[] playerIDs, int coachID)
    {
        for (int i : playerIDs)
        {
            teamPlayerList.add(BAVM.getPlayerManager().getPlayer(i));
        }

        this.teamCoach = BAVM.getCoachManager().getCoach(coachID);

        this.teamCoefficients = new TeamCoefficients(teamPlayerList, teamCoach);
    }

    public ArrayList<Player> getPlayers()
    {
        return this.teamPlayerList;
    }

    public Coach getTeamCoach()
    {
        return this.teamCoach;
    }

    public TeamCoefficients getTeamCoefficients() {
        return teamCoefficients;
    }

    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String infoString;

        // String maken met stats
        for (Player player : teamPlayerList)
        {
            stringBuilder.append(player.toString() + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        infoString = stringBuilder.toString();

        String playerString = "PlayerIDs{" +
                infoString +
                "}";

        String coachString = teamCoach.toString();

        return playerString + "," + coachString;
    }
}
