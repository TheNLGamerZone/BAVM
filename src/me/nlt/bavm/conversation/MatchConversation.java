package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.game.Match;

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
                    "Typ -3 voor een lijst van alle weken", //TODO maak dit
                    "Typ -4 voor een lijst van alle wedstrijden", //TODO maak dit
                    "Typ -5 om een week te bekijken", //TODO maak dit
                    "Typ -6 om een wedstrijd te bekijken" //TODO maak dit
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -6)
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

                for (String str : match.getMatchLog())
                {
                    display.appendText(str);
                }

                display.readLine("Typ iets om terug te keren naar het wedstrijdcentrum.");
            }

            break;
        }
    }
}
