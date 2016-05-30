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

    public void simulateMatch(int team0ID, int team1ID)
    {

        //TODO let game simulate an actual game with actual coefficients
        int matchResult[] = Game.simulateGame();

        String matchName = BAVM.getTeamManager().getTeam(team0ID).getTeamName() + BAVM.getTeamManager().getTeam(team1ID).getTeamName();

        playedMatches.add(new Match(matchName, getNextAvailableID(), team0ID, team1ID, matchResult));
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
