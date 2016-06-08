package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

                trainen:
                while (true)
                {
                    ArrayList<Player> players = new ArrayList<Player>();
                    PlayerStats.Stat stat = null;

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
                        spelerTrainen:
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

                            while (true)
                            {
                                int playerID = (int) display.readDouble(false);
                                Player player = BAVM.getPlayerManager().getPlayer(playerID);

                                if (playerID == -1)
                                {
                                    break;
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
                                    players.add(player);
                                    option = -4;
                                    break spelerTrainen;
                                }
                            }
                        }
                    }

                    if (option == -4)
                    {
                        if (players.isEmpty())
                        {
                            display.appendText("\n\t\t- - - - - - - - - - [ Team trainen ] - - - - - - - - - - ",
                                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu"
                            );

                            players.addAll(BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers().stream().collect(Collectors.toList()));
                        }

                        display.appendText("Typ de eerste twee letters van de stat die je wilt trainen: \n");

                        for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                        {
                            display.appendText("  " + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase());
                        }

                        while (true)
                        {
                            String statCharacters = display.readLine(false, "");

                            for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                            {
                                if (stats.name().contains(statCharacters.toUpperCase()))
                                {
                                    stat = stats;
                                }
                            }

                            if (statCharacters.equals("-1"))
                            {
                                break trainen;
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
                                double price = 0;
                                double givenUpgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");

                                for (Player player : players)
                                {
                                    double currentStat = player.getPlayerStats().getValue(stat);
                                    double upgrade = (currentStat + givenUpgrade <= 100
                                            ? givenUpgrade
                                            : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));
                                    double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

                                    for (double i = upgrade; i >= 0; i -= stepSize)
                                    {
                                        if (currentStat + stepSize > newStat)
                                        {
                                            price += (currentStat * currentStat * (currentStat / 100)) * currentStat + Math.abs(currentStat - newStat) * 15000 - players.size() * 1234;
                                            currentStat += Math.abs(currentStat - newStat);
                                        } else
                                        {
                                            price += (currentStat * currentStat * (currentStat / 100)) * currentStat + stepSize * 15000 - players.size() * 1234;
                                            currentStat += stepSize;
                                        }
                                    }
                                }

                                display.appendText("Je staat op het punt " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase() + " voor het hele team te upgraden met " + new DecimalFormat("###.##").format(givenUpgrade) + " voor $" + new DecimalFormat("######.##").format(price));

                                for (Player player : players)
                                {
                                    double currentStat = player.getPlayerStats().getValue(stat);
                                    double upgrade = (currentStat + givenUpgrade <= 100
                                            ? givenUpgrade
                                            : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));

                                    for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                                    {
                                        if (stat != stats)
                                        {
                                            continue;
                                        }

                                        display.appendText("   " + player.getPlayerName() + " (" + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase() + "): " + new DecimalFormat("###.##").format(player.getPlayerStats().getValue(stats)) +
                                                (stat == stats ? " (Nieuwe waarde: " + new DecimalFormat("###.##").format((currentStat + upgrade <= 100 ? currentStat + upgrade : 100)) + ")" : ""));
                                    }
                                }

                                if ((int) display.readDouble(false, "Typ 123 om te bevestigen") == 123)
                                {
                                    if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price)
                                    {
                                        display.appendText("Je hebt niet genoeg geld voor deze training!");
                                        break trainen;
                                    }

                                    for (Player player : players)
                                    {
                                        double currentStat = player.getPlayerStats().getValue(stat);
                                        double upgrade = (currentStat + givenUpgrade <= 100
                                                ? givenUpgrade
                                                : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));

                                        player.getPlayerStats().increaseSkill(stat, upgrade);
                                    }

                                    BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);
                                    display.appendText("De stat '" + stat.name().toLowerCase() + "' is voor " + (players.size() == 1 ? players.get(0).getPlayerName() : "iedereen") + " verhoogd met " + givenUpgrade + "!");
                                    break trainen;
                                } else
                                {
                                    display.appendText("Trainen geannuleerd.");
                                    break trainen;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
