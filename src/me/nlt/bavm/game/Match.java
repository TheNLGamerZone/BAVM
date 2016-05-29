package me.nlt.bavm.game;

public class Match {
    private String matchName;
    private int matchID;
    private int[] matchGoals;
    private int[] teamIDs;

    public Match (String matchName, int matchID, int team0ID, int team1ID, int[] matchResult) {
        this.matchName = matchName;
        this.matchID = matchID;
        this.matchGoals = matchResult;
        this.teamIDs[0] = team0ID;
        this.teamIDs[1] = team1ID;
    }

    public String getMatchName() {
        return matchName;
    }

    public int getMatchID() {
        return matchID;
    }

    public int[] getMatchGoals() {
        return matchGoals;
    }

    public int[] getTeamIDs() {
        return teamIDs;
    }
}
