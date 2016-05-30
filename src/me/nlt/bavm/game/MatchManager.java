package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;

import java.util.ArrayList;

public class MatchManager
{

    private ArrayList<Match> playedMatches;

    public MatchManager()
    {
        this.playedMatches = new ArrayList<>();

        this.loadMatches();
    }

    private void loadMatches()
    {

    }

    private void saveMatches()
    {

    }

    public void simulateMatch(int homeID, int visitorID)
    {

        //TODO let game simulate an actual game with actual coefficients
        int matchResult[] = Game.simulateGame(homeID, visitorID);

        String matchName = BAVM.getTeamManager().getTeam(homeID).getTeamName() + BAVM.getTeamManager().getTeam(visitorID).getTeamName();

        playedMatches.add(new Match(matchName, getNextAvailableID(), homeID, visitorID, matchResult));
    }

    public Match getMatch(int matchID)
    {
        for (Match match : playedMatches)
        {
            if (match.getMatchID() == matchID)
            {
                return match;
            }
        }

        return null;
    }

    public int getNextAvailableID()
    {
        int currentID = -1;

        for (Match match : playedMatches)
        {
            currentID++;
        }

        return currentID + 1;
    }
}
