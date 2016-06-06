package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;

import java.text.DecimalFormat;

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

            if (mainNumber == -4)
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

                            spelerTrainen:
                            while (true)
                            {
                                int playerID = (int) display.readDouble(false);
                                Player player = BAVM.getPlayerManager().getPlayer(playerID);

                                if (playerID == -1)
                                {
                                    break spelerTrainen;
                                }

                                if (playerID == -2)
                                {
                                    break backToMain;
                                }

                                if (player == null)
                                {
                                    display.appendText("Die speler bestaat niet!");
                                } else
                                {
                                    display.appendText("Typ de eerste twee letters van de stat die je wilt trainen: ");

                                    for (PlayerStats.Stat stat : PlayerStats.Stat.values())
                                    {
                                        display.appendText("  " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase());
                                    }

                                    while (true)
                                    {
                                        String statCharacters = display.readLine(false, "");
                                        PlayerStats.Stat stat = null;

                                        for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                                        {
                                            if (stats.name().contains(statCharacters.toUpperCase()))
                                            {
                                                stat = stats;
                                            }
                                        }

                                        if (statCharacters.equals("-1"))
                                        {
                                            break spelerTrainen;
                                        }

                                        if (statCharacters.equals("-2"))
                                        {
                                            break backToMain;
                                        }

                                        if (stat == null)
                                        {
                                            display.appendText("Die stat bestaat niet!");
                                        } else
                                        {
                                            final double stepSize = 2.6;
                                            double currentStat = player.getPlayerStats().getValue(stat);
                                            double upgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");
                                            double newStat = currentStat + upgrade;
                                            double price = 0;

                                            for (double i = upgrade; i >= 0; i -= stepSize) {
                                                if (currentStat + stepSize > newStat)
                                                {
                                                    double difference = Math.abs(currentStat - newStat);

                                                    price += (currentStat * currentStat * currentStat *  0.65) * currentStat + difference * 15000;
                                                    currentStat += difference;
                                                }
                                                else
                                                {
                                                    price += (currentStat * currentStat * currentStat * 0.65) * currentStat + stepSize * 15000;
                                                    currentStat += stepSize;
                                                }
                                            }

                                            if ((int) display.readDouble(false, "Je staat op het punt " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase() + " te upgraden naar " + currentStat + " voor $" + new DecimalFormat("######.##").format(price) +
                                                    "\nTyp 123 om te bevestigen.") == 123)
                                            {
                                                if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price)
                                                {
                                                    display.appendText("Je hebt niet genoeg geld voor deze training!");
                                                    break spelerTrainen;
                                                }

                                                player.getPlayerStats().increaseSkill(stat, upgrade);
                                                BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);
                                                display.appendText("De stat '" + stat.name().toLowerCase() + "' is verhoogd naar " + newStat + "!");
                                                break spelerTrainen;
                                            } else
                                            {
                                                display.appendText("Trainen geannuleerd.");
                                                break spelerTrainen;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
