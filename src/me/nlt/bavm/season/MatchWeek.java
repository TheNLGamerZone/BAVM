package me.nlt.bavm.season;

import me.nlt.bavm.generator.MatchSequence;

import java.util.ArrayList;
import java.util.List;

public class MatchWeek
{
    private ArrayList<PlannedMatch> matchesInWeek = new ArrayList<>();

    /**
     * Het matchWeek object heeft alle geplande wedstrijden voor 1 speelronde/week in zich
     *
     * @param weekNumber Nummer van de week
     * @param seasonHalf Seizoenstijd
     */
    public MatchWeek(int weekNumber, int seasonHalf)
    {
        this.matchesInWeek = getMatchesInWeek(weekNumber, seasonHalf);
    }

    /**
     * Returnt alle matches die gespeeld moeten worden
     *
     * @param weekNumber Nummer van de week
     * @param seasonHalf Seizoenstijd
     * @return ArrayList met alle matches die gespeeld moeten worden
     */
    public ArrayList<PlannedMatch> getMatchesInWeek(int weekNumber, int seasonHalf)
    {
        ArrayList<PlannedMatch> matchesInWeek = new ArrayList<>();

        List<String> subMatchSequence = MatchSequence.getMatchSequence().subList(weekNumber * 10, (weekNumber * 10) + 10);

        for (int i = 0; i < 10; i++)
        {
            matchesInWeek.add(new PlannedMatch(subMatchSequence.get(i), seasonHalf, (weekNumber * 10) + i));
        }

        return matchesInWeek;
    }

    /**
     * Returnt alle matches die gespeeld moeten worden
     *
     * @return ArrayList met alle matches die gespeeld moeten worden
     */
    public ArrayList<PlannedMatch> getMatchesInWeek()
    {
        return this.matchesInWeek;
    }
}
