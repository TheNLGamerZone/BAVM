package me.nlt.bavm.teams.team;

public class Team
{
    private String teamName;
    private int teamID;

    private TeamInfo teamInfo;

    public Team(String teamName, int teamID, int[] playerIDs, int coachID)
    {
        if (playerIDs.length != 21)
        {
            System.out.println("RIP: " + playerIDs.length + " (" + teamID + ")");
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