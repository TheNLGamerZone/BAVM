package me.nlt.bavm.teams.team;

public class TeamScores
{
    private int points;
    private int wins;
    private int losses;
    private int draws;
    private int goalsFor;
    private int goalsAgainst;

    public TeamScores() {
        points = 0;
        wins = 0;
        losses = 0;
        draws = 0;
        goalsFor = 0;
        goalsAgainst = 0;
    }

    public void increasePoints(int increment)
    {
        points = points + increment;
    }

    public void increaseWins(int increment)
    {
        wins = wins + increment;
    }

    public void increaseLosses(int increment)
    {
        losses = losses + increment;
    }

    public void increaseDraws(int increment)
    {
        draws = draws + increment;
    }

    public void increaseGoalsFor(int increment)
    {
        goalsFor = goalsFor + increment;
    }

    public void increaseGoalsAgainst(int increment)
    {
        goalsAgainst = goalsAgainst + increment;
    }

    public int getPoints()
    {
        return points;
    }

    public int getWins()
    {
        return wins;
    }

    public int getLosses()
    {
        return losses;
    }

    public int getDraws()
    {
        return draws;
    }

    public int getGoalsFor()
    {
        return goalsFor;
    }

    public int getGoalsAgainst()
    {
        return goalsAgainst;
    }
}
