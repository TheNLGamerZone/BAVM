package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.season.Scores;

public class SeasonConversation implements Conversation{
    @Override
    public void startConversation(Display display)
    {
        backToMain:
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - [ Het seizoenscentrum ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ -3 om de competitiestand te bekijken"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3)
            {
                for (int i = 0; i < 20; i++)
                {
                    display.appendText(BAVM.getTeamManager().getTeam(i).getTeamName() + ": " + Scores.getScores().get(i));
                }
            }
        }
    }
}
