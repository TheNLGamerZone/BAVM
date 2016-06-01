package me.nlt.bavm.teams.team;

import me.nlt.bavm.teams.Manageable;

public class Team implements Manageable
{
    private String teamName;
    private int teamID;

    private TeamInfo teamInfo;

    public Team(String teamName, int teamID, int[] playerIDs, int coachID)
    {
        System.out.printf("Created team %s (p=%d, id=%d)%n", teamName, playerIDs.length, teamID);

        this.teamName = teamName;
        this.teamID = teamID;
        this.teamInfo = new TeamInfo(playerIDs, coachID);
    }

    public String getTeamName()
    {
        return this.teamName;
    }

    public int getTeamID()
    {
        return this.teamID;
    }

    public TeamInfo getTeamInfo()
    {
        return this.teamInfo;
    }

    @Override
    public String toString()
    {
        return "Team{" +
                "name=" + this.teamName.replaceAll(" ", "_") +
                ",id=" + this.teamID +
                ",info=" + this.teamInfo.toString() +
                "}";
    }

    @Override
    public int getID()
    {
        return this.teamID;
    }
}