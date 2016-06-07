package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;

import java.util.ArrayList;

public class Scores
{
    private static ArrayList<Integer> scores = new ArrayList<>();

    public static void calculateScores()
    {
        for (int i = 0; i < 20; i++)
        {
            scores.add(i, BAVM.getTeamManager().getTeam(i).getTeamScore());
        }
    }

    public ArrayList<Integer> getScores()
    {
        return scores;
    }
}
