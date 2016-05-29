package me.nlt.bavm.teams.team;

public class Team
{
    private String teamName;
    private int teamID;

    private TeamInfo teamInfo;
    private TeamCoefficients teamCoefficients;

    public Team(String teamName, int teamID, int[] playerIDList, int coachID)
    {
        this.teamName = teamName;
        this.teamID = teamID;
        this.teamInfo = new TeamInfo(playerIDList, coachID);
        this.teamCoefficients = new TeamCoefficients(playerIDList, coachID);
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

    public TeamCoefficients getTeamCoefficients()
    {
        return this.teamCoefficients;
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