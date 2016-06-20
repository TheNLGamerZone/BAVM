package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.game.MatchManager;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.TeamAI;
import me.nlt.bavm.teams.team.Geld;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

public class Week
{
    public static int weekNumber;
    public static int seasonNumber;

    /**
     * Deze methode wordt aangeroepen aan het einde van de week en zorgt er o.a. voor dat alle PlannedMatches gespeeld worden
     *
     * @param endSeason Boolean die staat voor het einde van het seizoen
     * @return Boolean die aangeeft of we verder zijn gegaan
     */
    public static boolean endWeek(boolean endSeason)
    {
        if (weekNumber == 38)
        {
            return false;
        }

        TeamManager teamManager = BAVM.getTeamManager();
        MatchManager matchManager = BAVM.getMatchManager();
        MatchWeek matchWeek = BAVM.getSeason().getSeasonWeeks().get(weekNumber);

        // Team AI
        TeamAI.doTeamAI();

        /*
         * Wedstrijden worden berekend
         */
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
                BAVM.getDisplay().printException(e);
            }
        }

        weekNumber++;
        BAVM.getFileManager().addDate("week", false);

        if (endSeason)
        {
            Team winner = BAVM.getTeamManager().getTeam(AllScores.displayScores());

            BAVM.getDisplay().appendText("\n" + winner.getTeamName() + " (ID: " + winner.getID() + ") has won the season!");
            if (winner.getID() == BAVM.getTeamManager().playerTeam.getID())
            {
                BAVM.rollCredits();
            } else
            {
                BAVM.getDisplay().appendText("Je hebt helaas niet gewonnen, volgend seizoen beter!", "Om je opweg te helpen hebben we een extra sponsor voor je geregeld die direct $500.000,00 investeert, gebruik het geld goed!");
                BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().addGeld(500000);
                nextSeason();
            }
        } else
        {

            for (int i = 0; i < 20; i++)
            {
                Geld geld = BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamGeld();

                geld.addGeld(geld.getWeeklyIncome());
            }
        }

        return true;
    }

    /**
     * Deze methode zorgt ervoor dat een seizoen correct wordt afgesloten
     */
    public static void nextSeason()
    {
        BAVM.getDisplay().readLine(false, "Typ iets om verder te gaan naar het volgende seizoen");

        for (int i = 0; i < 20; i++)
        {
            BAVM.getTeamManager().getTeam(i).getTeamInfo().resetTeamScores();
        }

        for (Object object : BAVM.getMatchManager().manageables)
        {
            BAVM.getFileManager().writeData("match", "NULL <-> PH", ((Manageable) object).getID());
        }

        BAVM.getMatchManager().manageables.clear();

        weekNumber = 0;
        seasonNumber++;

        BAVM.getFileManager().addDate("week", true);
        BAVM.getFileManager().addDate("season", false);

        for (int i = 0; i < 20; i++)
        {
            Geld geld = BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamGeld();

            geld.addGeld(geld.getWeeklyIncome());
        }

        BAVM.getDisplay().appendText("\n\t\t- - - - - - - - - - - - [ NIEUW SEIZOEN: WEEK " + (weekNumber + 1) + " ] - - - - - - - - - - -");
    }

    /**
     * Returnt het nummer van de week
     *
     * @return Nummer van de week
     */
    public static int getWeekNumber()
    {
        return weekNumber;
    }
}
