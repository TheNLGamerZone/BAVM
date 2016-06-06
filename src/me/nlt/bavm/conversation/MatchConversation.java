package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;

public class MatchConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - [ Het wedstrijdcentrum ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ -3 voor een lijst van alle weken", //TODO maak dit
                    "Typ -4 voor een lijst van alle wedstrijden", //TODO maak dit
                    "Typ -5 om een week te bekijken", //TODO maak dit
                    "Typ -6 om een wedstrijd te bekijken" //TODO maak dit
            );
            break;
        }
    }
}
