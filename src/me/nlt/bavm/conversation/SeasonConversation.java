package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.season.AllScores;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;

public class SeasonConversation implements Conversation
{
    /*
     * In deze "Conversation" kun je de standen zien van het huidige seizoen
     */
    @Override
    public void startConversation(Display display)
    {
        display.clearText();

        backToMain:
        while (true)
        {
            display.appendText("\t\t- - - - - - - - - [ Het seizoenscentrum ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ -3 om de competitiestand te bekijken",
                    "Typ -4 om de stand van een team te bekijken"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3)
            {
                display.clearText();
                AllScores.displayScores();
            }

            if (mainNumber == -4)
            {
                Team team;

                display.clearText();
                display.appendText("\t\t- - - - - - - - - - [ Stand van een team ] - - - - - - - - - - ");

                while (true)
                {
                    int teamID = (int) display.readDouble(false, "Typ het ID van het team dat je wilt bekijken.");
                    team = BAVM.getTeamManager().getTeam(teamID);

                    if (teamID == -2)
                    {
                        break backToMain;
                    } else if (team == null)
                    {
                        display.appendText("Dat team bestaat niet!");
                    } else
                    {
                        break;
                    }
                }

                BAVM.getDisplay().appendText("Pts\tW\tD\tL\tF\tA");

                String toDisplay = team.getTeamName() + " (ID: " + team.getID() + "):\n";

                for (TeamInfo.Score score : TeamInfo.Score.values())
                {
                    toDisplay = toDisplay + team.getTeamInfo().getTeamScores().get(score) + "\t";
                }

                BAVM.getDisplay().appendText(toDisplay + "\n");
            }
        }
    }
}
