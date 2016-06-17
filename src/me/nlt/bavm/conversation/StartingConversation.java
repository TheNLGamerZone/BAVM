package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;

public class StartingConversation implements Conversation{
    @Override
    public void startConversation(Display display)
    {
        display.appendText("\nWelkom bij de BrinkAnema Voetbal Manager (BAVM)!\n",
                "Voordat je begint willen we graag je de naam van je personage weten (de directeur van het team!) en de naam van je team.");

        while (true) {
            String directorName = display.readLine(false, "Typ de naam van je personage! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

            if (directorName.equals("-1")) {
                break;
            }

            display.appendText("Je staat op het punt om je personage de naam " + directorName + " te geven. Weet je het zeker?");

            if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123")) {
                BAVM.getTeamManager().getTeam(19).setDirectorName(directorName);
                break;
            } else {
                display.appendText("Naamgeving geannuleerd.");
            }
        }

        while (true) {
            String teamName = display.readLine(false, "Typ de naam van je team! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

            if (teamName.equals("-1")) {
                break;
            }

            display.appendText("Je staat op het punt om je team de naam " + teamName + " te geven. Weet je het zeker?");

            if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123")) {
                BAVM.getTeamManager().getTeam(19).setTeamName(teamName);
                break;
            } else {
                display.appendText("Naamgeving geannuleerd.");
            }
        }
    }
}
