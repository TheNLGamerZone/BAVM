package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;

public class Team
{
    private String teamName;
    private int teamID;

    private TeamInfo teamInfo;

    public Team(String teamName, int teamID, int[] playerIDs, int coachID)
    {
        if (playerIDs.length != 21)
        {
            BAVM.getDisplay().appendText("RIP: " + playerIDs.length + " (" + teamID + ")");
        }
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
}