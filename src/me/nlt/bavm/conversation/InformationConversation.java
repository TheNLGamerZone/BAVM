package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;

public class InformationConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        backToMain:
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - - - - - - - - [ De Markt ] - - - - - - - - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je filters aan te passen",
                    "Typ '-4' om te zoeken",
                    "Typ '-5' om spelers te verkopen"
            );

            int mainNumber = (int) display.readDouble(false);
        }
    }
}
