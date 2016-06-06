package me.nlt.bavm.season;

import me.nlt.bavm.generator.MatchSequence;

import java.util.ArrayList;
import java.util.List;

public class MatchWeek {
    private ArrayList<PlannedMatch> matchesInWeek = new ArrayList<>();
    private int weekNumber;
    private int seasonHalf;

    public MatchWeek(int weekNumber, int seasonHalf) {
        this.weekNumber = weekNumber;
        this.seasonHalf = seasonHalf;

        this.matchesInWeek = getMatchesInWeek(weekNumber, seasonHalf);
    }

    public ArrayList<PlannedMatch> getMatchesInWeek(int weekNumber, int seasonHalf) {
        ArrayList<PlannedMatch> matchesInWeek = new ArrayList<>();

        List<String> subMatchSequence = MatchSequence.getMatchSequence().subList(weekNumber * 10, (weekNumber * 10) + 10);

        for (int i = 0; i < 10; i++) {
            matchesInWeek.add(new PlannedMatch(subMatchSequence.get(i), seasonHalf));
        }

        return matchesInWeek;
    }

    public ArrayList<PlannedMatch> getMatchesInWeek() {
        return this.matchesInWeek;
    }
}
