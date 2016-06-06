package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.player.Player;

public class ManagementConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        backToMain:
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - [ Team management ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je de opstelling te veranderen",
                    "Typ '-4' om je spelers/team te trainen"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3)
            {
                display.appendText("\n\t\t- - - - - - - - - - [ Team trainen ] - - - - - - - - - - ",
                        "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                        "Typ '-3' om een speler te trainen",
                        "Typ '-4' om het hele team te trainen",
                        "Typ '-5' om je coach te trainen"
                );

                while (true)
                {
                    int option = (int) display.readDouble(false);

                    if (option == -1)
                    {
                        break;
                    }

                    if (option == -2)
                    {
                        break backToMain;
                    }

                    if (option == -3)
                    {
                        while (true)
                        {
                            display.appendText("\n\t\t- - - - - - - - - - [ Speler trainen ] - - - - - - - - - - ",
                                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                                    "Typ het ID van de speler om te trainen:\n"
                            );

                            for (Player player : BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers())
                            {
                                display.appendText("  " + player.getPlayerName() + " (ID: " + player.getID() + ")");
                            }

                            break;
                        }
                    }
                }
            }
        }
    }
}
