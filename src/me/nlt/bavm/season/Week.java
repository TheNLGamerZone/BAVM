package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.game.MatchManager;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

public class Week {
    private static int weekNumber;

    public static void endWeek() {
        TeamManager teamManager = BAVM.getTeamManager();
        MatchManager matchManager = BAVM.getMatchManager();
        MatchWeek matchWeek = BAVM.getSeason().getSeasonWeeks().get(weekNumber);

        for (PlannedMatch plannedMatch : matchWeek.getMatchesInWeek()) {
            int matchID = matchManager.simulateMatch(plannedMatch.getTeamIDs()[0], plannedMatch.getTeamIDs()[1]);
            plannedMatch.getMatchName();

            BAVM.getDisplay().appendText("\nMatch: " + teamManager.getTeam(plannedMatch.getTeamIDs()[0]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[0] + ")-" + teamManager.getTeam(plannedMatch.getTeamIDs()[1]).getTeamName()  + " (ID: " + plannedMatch.getTeamIDs()[1] + ")",
                    "Result: " + matchManager.getMatch(matchID).getMatchGoals()[0] + "-" +  matchManager.getMatch(matchID).getMatchGoals()[1]);

            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }



        weekNumber++;

        BAVM.getDisplay().appendText("\n\t\t- - - - - - - - - - - - - - - [ WEEK " + (weekNumber + 1) + " ] - - - - - - - - - - - - - -\n");

    }
}