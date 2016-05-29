package me.nlt.bavm.game;

public class Match {
    private String matchName;
    private int matchID;
    private int locationID;

    private MatchInfo matchInfo;

    public Match (String matchName, int matchID, int locationID, int team0ID, int team1ID) {
        this.matchName = matchName;
        this.matchID = matchID;
        this.locationID = locationID;
        this.matchInfo = new MatchInfo(team0ID, team1ID);
    }
}
