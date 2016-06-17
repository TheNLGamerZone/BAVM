package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.game.MatchManager;
import me.nlt.bavm.teams.team.Geld;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

public class Week
{
    public static int weekNumber;

    public static boolean endWeek(boolean endSeason)
    {
        if (weekNumber == 38)
        {
            return false;
        }

        TeamManager teamManager = BAVM.getTeamManager();
        MatchManager matchManager = BAVM.getMatchManager();
        MatchWeek matchWeek = BAVM.getSeason().getSeasonWeeks().get(weekNumber);

        for (PlannedMatch plannedMatch : matchWeek.getMatchesInWeek())
        {
            int matchID = matchManager.simulateMatch(plannedMatch.getTeamIDs()[0], plannedMatch.getTeamIDs()[1]);
            plannedMatch.getMatchName();

            BAVM.getDisplay().appendText("\nMatch (ID: " + matchID + "): " + teamManager.getTeam(plannedMatch.getTeamIDs()[0]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[0] + ")-" + teamManager.getTeam(plannedMatch.getTeamIDs()[1]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[1] + ")",
                    "Result: " + matchManager.getMatch(matchID).getMatchGoals()[0] + "-" + matchManager.getMatch(matchID).getMatchGoals()[1]);

            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }

        weekNumber++;
        BAVM.getFileManager().addWeek();

        if (endSeason)
        {
            Team winner = BAVM.getTeamManager().getTeam(AllScores.displayScores());

            BAVM.getDisplay().appendText("\n" + winner.getTeamName() + " (ID: " + winner.getID() + ") has won the season!");
            if (winner.getID() == BAVM.getTeamManager().playerTeam.getID())
            {
                // TODO: Credits
            } else
            {
                BAVM.getDisplay().appendText("Je hebt helaas niet gewonnen, volgend seizoen beter!", "Om je opweg te helpen hebben we een extra sponsor voor je geregeld die direct $500.000,00 investeert, gebruik het geld goed!");
                BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().addGeld(500000);
            }
        } else
        {

            for (int i = 0; i < 20; i++)
            {
                Geld geld = BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamGeld();

                geld.addGeld(geld.getWeeklyIncome());
            }

            BAVM.getDisplay().appendText("\n\t\t- - - - - - - - - - - - - - - [ WEEK " + (weekNumber + 1) + " ] - - - - - - - - - - - - - -");
        }

        return true;
    }

    public static int getWeekNumber()
    {
        return weekNumber;
    }
}
