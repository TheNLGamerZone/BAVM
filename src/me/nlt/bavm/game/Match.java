package me.nlt.bavm.game;

public class Match
{
    private String matchName;
    private int matchID;
    private int[] matchGoals;
    private int[] teamIDs =  new int[2];

    public Match(String matchName, int matchID, int homeID, int visitorID, int[] matchResult)
    {
    	this.matchName = matchName;
        this.matchID = matchID;
        this.matchGoals = matchResult;
        this.teamIDs[0] = homeID;
        this.teamIDs[1] = visitorID;
    }

    public String getMatchName()
    {
        return matchName;
    }

    public int getMatchID()
    {
        return matchID;
    }

    public int[] getMatchGoals()
    {
        return matchGoals;
    }

    public int[] getTeamIDs()
    {
        return teamIDs;
    }
}
