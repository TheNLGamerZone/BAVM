package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.coach.CoachStats;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.player.Position;
import me.nlt.bavm.teams.team.Team;

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
                    "Typ '-4' om je spelers/team te trainen",
                    "Typ '-5' om een andere directeur aan te stellen (verander je naam)",
                    "Typ '-6' om je team een andere naam te geven"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3)
            {
                Team team = BAVM.getTeamManager().playerTeam;
                Player playerInPlacement;
                Player transferPlayer = null;
                Position position = null;

                while (true)
                {
                    display.appendText("\n\t\t- - - - - - - - - - [ Opstelling veranderen ] - - - - - - - - - - ",
                            "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                            "Huidige opstelling:",
                            "   Keeper: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.KEEPER),
                            "   Aanvallers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.ATTACKER),
                            "   Verdedigers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.DEFENDER),
                            "   Middenvelders: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.MIDFIELDER),
                            "Typ het ID van de speler die je wilt wisselen"
                    );

                    allSettingsSet:
                    while (true)
                    {
                        int playerID = (int) display.readDouble(false);
                        playerInPlacement = BAVM.getPlayerManager().getPlayer(playerID);

                        if (playerID == -1)
                        {
                            break;
                        } else if (playerID == -2)
                        {
                            break backToMain;
                        } else if (playerInPlacement == null)
                        {
                            display.appendText("Die speler bestaat niet!");
                        } else if (!team.getTeamInfo().getPlayerPlacement().isPlaced(playerInPlacement))
                        {
                            display.appendText("Die speler zit niet in de huidige opstelling!");
                        } else
                        {
                            while (true)
                            {
                                playerID = (int) display.readDouble(false, "Typ het ID van de speler die je in de opstelling wilt zetten");
                                transferPlayer = BAVM.getPlayerManager().getPlayer(playerID);

                                if (playerID == -2)
                                {
                                    break backToMain;
                                } else if (transferPlayer == null)
                                {
                                    display.appendText("Die speler bestaat niet!");
                                } else if (!team.getTeamInfo().getPlayers().contains(transferPlayer))
                                {
                                    display.appendText("Die speler zit niet in je team!");
                                } else
                                {
                                    while (true)
                                    {
                                        String givenPosition = display.readLine(false, "Typ de eerste twee letters van de positie waar je de speler wilt neerzetten");

                                        for (Position loopPosition : Position.values())
                                        {
                                            if (loopPosition.name().contains(givenPosition.toUpperCase()))
                                            {
                                                position = loopPosition;
                                            }
                                        }

                                        if (position == null)
                                        {
                                            display.appendText("Die positie bestaat niet!");
                                        } else if (position == Position.KEEPER && team.getTeamInfo().getPlayerPlacement().getKeeper() != playerInPlacement)
                                        {
                                            display.appendText("Je kan niet twee keepers hebben!\nAls je een speler als keeper wilt hebben zul je die moeten wisselen met de huidige keeper");
                                        } else
                                        {
                                            break allSettingsSet;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    team.getTeamInfo().getPlayerPlacement().exchangePlayers(playerInPlacement, transferPlayer, position);
                    display.appendText("Je hebt de speler " + transferPlayer.getPlayerName() + " op het veld gezet als een " + position.name().toLowerCase());
                }
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
                                final double givenUpgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");

                                for (Player player : players)
                                {
                                    double currentStat = player.getPlayerStats().getValue(stat);
                                    final double upgrade = (currentStat + givenUpgrade <= 100
                                            ? givenUpgrade
                                            : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));
                                    final double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

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

                                        display.appendText("   " + player.getPlayerName() + " (" + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase() + "): " + new DecimalFormat("###.##").format(player.getPlayerStats().getValue(stats)) + (" (Nieuwe waarde: " + new DecimalFormat("###.##").format((currentStat + upgrade <= 100 ? currentStat + upgrade : 100)) + ")"));
                                    }
                                }

                                if (display.readLine(false, "Typ 123 om te bevestigen").equals("123"))
                                {
                                    if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price)
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

                    if (option == -5)
                    {
                        CoachStats.CStat coachStat = null;

                        display.appendText("\n\t\t- - - - - - - - - - [ Coach trainen ] - - - - - - - - - - ",
                                "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                                "Typ de eerste twee letters van de stat die je wilt trainen: \n"
                        );

                        for (CoachStats.CStat stats : CoachStats.CStat.values())
                        {
                            display.appendText("  " + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase());
                        }

                        while (true)
                        {
                            String statCharacters = display.readLine(false, "");

                            for (CoachStats.CStat stats : CoachStats.CStat.values())
                            {
                                if (stats.name().contains(statCharacters.toUpperCase()))
                                {
                                    coachStat = stats;
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

                            if (coachStat == null)
                            {
                                display.appendText("Die stat bestaat niet!");
                            } else
                            {
                                final Coach coach = BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamCoach();
                                final double stepSize = 2.6;
                                double price = 0;
                                final double givenUpgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");
                                double currentStat = coach.getCoachStats().getValue(coachStat);
                                double upgrade = (currentStat + givenUpgrade <= 100
                                        ? givenUpgrade
                                        : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));
                                double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

                                for (double i = upgrade; i >= 0; i -= stepSize)
                                {
                                    if (currentStat + stepSize > newStat)
                                    {
                                        price += (currentStat * currentStat * (currentStat / 100)) * currentStat + Math.abs(currentStat - newStat) * 15000 - ((int) (Math.random() * 10)) * 1234;
                                        currentStat += Math.abs(currentStat - newStat);
                                    } else
                                    {
                                        price += (currentStat * currentStat * (currentStat / 100)) * currentStat + stepSize * 15000 - ((int) (Math.random() * 10)) * 1234;
                                        currentStat += stepSize;
                                    }
                                }

                                display.appendText("Je staat op het punt " + coachStat.name().substring(0, 1) + coachStat.name().substring(1).toLowerCase() + " voor je coach te upgraden met " + new DecimalFormat("###.##").format(givenUpgrade) + " voor $" + new DecimalFormat("######.##").format(price));
                                display.appendText(" " + coach.getCoachName() + " (" + coachStat.name().substring(0, 1) + coachStat.name().substring(1).toLowerCase() + "): " + new DecimalFormat("###.##").format(coach.getCoachStats().getValue(coachStat)) + " (Nieuwe waarde: " + new DecimalFormat("###.##").format(newStat) + ")");

                                if (display.readLine(false, "Typ 123 om te bevestigen").equals("123"))
                                {
                                    if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price)
                                    {
                                        display.appendText("Je hebt niet genoeg geld voor deze training!");
                                        break trainen;
                                    }

                                    coach.getCoachStats().increaseSkill(coachStat, upgrade);
                                    BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);

                                    display.appendText("De stat '" + coachStat.name().toLowerCase() + "' is voor " + coach.getCoachName() + " verhoogd met " + givenUpgrade + "!");
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

            if (mainNumber == -5)
            {
                while (true)
                {
                    String directorName = display.readLine(false, "Typ de naam van je personage! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

                    if (directorName.equals("-1"))
                    {
                        break;
                    } else if (directorName.equals("-2"))
                    {
                        break backToMain;
                    }

                    display.appendText("Je staat op het punt om je personage de naam " + directorName + " te geven. Weet je het zeker?");

                    if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123"))
                    {
                        BAVM.getTeamManager().getTeam(19).setDirectorName(directorName);
                        break;
                    } else
                    {
                        display.appendText("Naamgeving geannuleerd.");
                    }
                }
            }

            if (mainNumber == -6)
            {
                while (true)
                {
                    String teamName = display.readLine(false, "Typ de naam van je team! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

                    if (teamName.equals("-1"))
                    {
                        break;
                    } else if (teamName.equals("-2"))
                    {
                        break backToMain;
                    }

                    display.appendText("Je staat op het punt om je team de naam " + teamName + " te geven. Weet je het zeker?");

                    if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123"))
                    {
                        BAVM.getTeamManager().getTeam(19).setTeamName(teamName);
                        break;
                    } else
                    {
                        display.appendText("Naamgeving geannuleerd.");
                    }
                }
            }
        }
    }
}
