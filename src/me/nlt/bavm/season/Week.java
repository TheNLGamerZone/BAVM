package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.game.MatchManager;

public class Week {
    private static int weekNumber;

    public static void endWeek() {
        MatchManager matchManager = BAVM.getMatchManager();
        MatchWeek matchWeek = BAVM.getSeason().getSeasonWeeks().get(weekNumber);

        for (PlannedMatch plannedMatch : matchWeek.getMatchesInWeek()) {
            int matchID = matchManager.simulateMatch(plannedMatch.getTeamIDs()[0], plannedMatch.getTeamIDs()[1]);
            plannedMatch.getMatchName();
            BAVM.getDisplay().appendText(matchManager.getMatch(matchID).toString());
        }

        weekNumber++;
    }
}
