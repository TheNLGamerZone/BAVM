package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;

import java.util.ArrayList;

public class Match implements Manageable
{
    private String matchName;
    private int matchID;
    private int[] matchGoals;
    private int[] teamIDs = new int[2];
    private ArrayList<String> matchLog = new ArrayList<>();

    public boolean unsavedChanges;

    public Match(String matchName, int matchID, int homeID, int visitorID, int[] matchResult, ArrayList<String> matchLog)
    {
        this.matchName = matchName;
        this.matchID = matchID;
        this.matchGoals = matchResult;
        this.teamIDs[0] = homeID;
        this.teamIDs[1] = visitorID;
        this.matchLog = matchLog;
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

    public ArrayList<String> getMatchLog()
    {
        return matchLog;
    }
    
    public void clearMatchLog()
    {
    	this.matchLog.clear();
    }
    
    public void loadLogs()
    {
    	this.matchLog = BAVM.getFileManager().getMatchLog(matchID);
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
        StringBuilder stringBuilder = new StringBuilder();

        for (String logMessage : matchLog)
        {
            stringBuilder.append(logMessage.replaceAll(" ", "_").replaceAll(",", "%").replaceAll("=", "~") + "@");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);

        return "Match{" +
                "id=" + getID() +
                ",teams=" + teamIDs[0] + ":" + teamIDs[1] +
                ",score=" + matchGoals[0] + ":" + matchGoals[1] +
                ",logs=" + stringBuilder.toString() +
                "}";
    }
}
