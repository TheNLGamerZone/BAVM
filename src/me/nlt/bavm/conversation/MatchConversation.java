package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.game.Match;
import me.nlt.bavm.season.MatchWeek;
import me.nlt.bavm.season.PlannedMatch;
import me.nlt.bavm.season.Season;

public class MatchConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        backToMain:
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - [ Het wedstrijdcentrum ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ -3 voor een lijst van alle wedstrijden",
                    "Typ -4 om een week te bekijken",
                    "Typ -5 om een wedstrijd te bekijken"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3)
            {
                display.appendText("\n\t\t- - - - - - - - - - [ Alle wedstrijden bekijken ] - - - - - - - - - - ");

                for (MatchWeek matchWeek : BAVM.getSeason().getSeasonWeeks())
                {
                    for (PlannedMatch plannedMatch : matchWeek.getMatchesInWeek())
                    {
                        int matchID = plannedMatch.getMatchID();
                        plannedMatch.getMatchName();

                        if (matchID < BAVM.getMatchManager().getNextAvailableID())
                        {
                            BAVM.getDisplay().appendText("Match (ID: " + matchID + "): " + BAVM.getTeamManager().getTeam(plannedMatch.getTeamIDs()[0]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[0] + ")-" + BAVM.getTeamManager().getTeam(plannedMatch.getTeamIDs()[1]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[1] + ")",
                                    "Result: " + BAVM.getMatchManager().getMatch(matchID).getMatchGoals()[0] + "-" + BAVM.getMatchManager().getMatch(matchID).getMatchGoals()[1]);

                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }

                display.readLine("Typ iets om terug te keren naar het wedstrijdcentrum.");
            }

            if (mainNumber == -4)
            {
                MatchWeek matchWeek;

                display.appendText("\n\t\t- - - - - - - - - - [ Week bekijken ] - - - - - - - - - - ");

                while (true)
                {
                    int weekNumber = (int) display.readDouble(false, "Typ het weeknummer om de gespeelde wedstrijden in die week te zien.");
                    matchWeek = BAVM.getSeason().getSeasonWeeks().get(weekNumber);

                    if (weekNumber == -2)
                    {
                        break backToMain;
                    } else if (matchWeek == null)
                    {
                        display.appendText("Die week bestaat niet!");
                    } else
                    {
                        break;
                    }
                }

                for (PlannedMatch plannedMatch : matchWeek.getMatchesInWeek())
                {
                    int matchID = plannedMatch.getMatchID();
                    plannedMatch.getMatchName();

                    BAVM.getDisplay().appendText("\nMatch (ID: " + matchID + "): " + BAVM.getTeamManager().getTeam(plannedMatch.getTeamIDs()[0]).getTeamName() + " (ID: " + plannedMatch.getTeamIDs()[0] + ")-" + BAVM.getTeamManager().getTeam(plannedMatch.getTeamIDs()[1]).getTeamName()  + " (ID: " + plannedMatch.getTeamIDs()[1] + ")",
                            "Result: " + BAVM.getMatchManager().getMatch(matchID).getMatchGoals()[0] + "-" +  BAVM.getMatchManager().getMatch(matchID).getMatchGoals()[1]);

                    try
                    {
                        Thread.sleep(1);
                    } catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                display.readLine("Typ iets om terug te keren naar het wedstrijdcentrum.");
            }

            if (mainNumber == -5)
            {
                Match match;

                display.appendText("\n\t\t- - - - - - - - - - [ Wedstrijd bekijken ] - - - - - - - - - - ");

                while (true)
                {
                    int matchID = (int) display.readDouble(false, "Typ het ID van de wedstrijd om diens informatie te bekijken");
                    match = BAVM.getMatchManager().getMatch(matchID);

                    if (matchID == -2)
                    {
                        break backToMain;
                    } else if (match == null)
                    {
                        display.appendText("Die wedstrijd bestaat niet!");
                    } else
                    {
                        break;
                    }
                }

                match.getMatchLog().forEach(display::appendText);

                display.readLine("Typ iets om terug te keren naar het wedstrijdcentrum.");
            }
        }
    }
}
