package me.nlt.bavm.game;

import me.nlt.bavm.teams.Manageable;

public class Match implements Manageable
{
    private String matchName;
    private int matchID;
    private int[] matchGoals;
    private int[] teamIDs = new int[2];

    public boolean unsavedChanges;

    public Match(String matchName, int matchID, int homeID, int visitorID, int[] matchResult)
    {
        this.matchName = matchName;
        this.matchID = matchID;
        this.matchGoals = matchResult;
        this.teamIDs[0] = homeID;
        this.teamIDs[1] = visitorID;
        this.unsavedChanges = false;
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

    @Override
    public int getID()
    {
        return this.matchID;
    }

    @Override
    public boolean unsavedChanges()
    {
        return unsavedChanges;
    }

    @Override
    public String toString()
    {
        return "teams=" + teamIDs[0] + ":" + teamIDs[1] +
                ",score=" + matchGoals[0] + ":" + matchGoals[1];
    }
}
