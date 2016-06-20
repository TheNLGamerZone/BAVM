package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.coach.CoachStats;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.player.Position;
import me.nlt.bavm.teams.team.Team;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ManagementConversation implements Conversation
{
    @Override
    /**
     * Start de conversatie met teammanagement
     */
    public void startConversation(Display display)
    {
        backToMain:
        // Management loop
        while (true)
        {
            // Main management menu
            display.clearText();
            display.appendText("\t\t- - - - - - - - - [ Team management ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je de opstelling te veranderen",
                    "Typ '-4' om je spelers/team te trainen",
                    "Typ '-5' om een andere directeur aan te stellen (verander je naam)",
                    "Typ '-6' om je team een andere naam te geven"
            );

            int mainNumber = (int) display.readDouble(false);

            // Checken of de gebruiker terug wil
            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            // Checken of de gebruiker de opstelling wilt veranderen
            if (mainNumber == -3)
            {
                Team team = BAVM.getTeamManager().playerTeam;
                Player playerInPlacement;
                Player transferPlayer = null;
                Position position = null;

                display.clearText();
                managementMenu:
                // ChangeSetup loop
                while (true)
                {
                    // Opstelling veranderen menu
                    display.appendText("\t\t- - - - - - - - - - [ Opstelling veranderen ] - - - - - - - - - - ",
                            "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                            "Huidige opstelling:",
                            "   Keeper: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.KEEPER),
                            "   Aanvallers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.ATTACKER),
                            "   Verdedigers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.DEFENDER),
                            "   Middenvelders: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.MIDFIELDER),
                            "Typ het ID van de speler die je wilt wisselen"
                    );

                    backMenu:
                    // Transfer loop
                    while (true)
                    {
                        // Speler met het gegeven ID verkijgen
                        int playerID = (int) display.readDouble(false);
                        playerInPlacement = BAVM.getPlayerManager().getPlayer(playerID);

                        // Checken of de gebruiker terug wilt en of de speler bestaat en in de huidige opstelling staat
                        if (playerID == -1)
                        {
                            break managementMenu;
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
                                // Lijstje met beschikbare speler laten zien
                                team.getTeamInfo().getPlayers().stream().filter(player -> !team.getTeamInfo().getPlayerPlacement().isPlaced(player)).forEach(player -> display.appendText("   " + player.getPlayerName() + " (ID: " + player.getID() + ")"));

                                // Speler met het gegeven ID verkrijgen
                                playerID = (int) display.readDouble(false, "Typ het ID van de speler die je in de opstelling wilt zetten");
                                transferPlayer = BAVM.getPlayerManager().getPlayer(playerID);

                                // Checken of de gebruiker terug wilt en of de speler bestaat en in de niet al huidige opstelling staat
                                if (playerID == -1)
                                {
                                    break backMenu;
                                } else if (playerID == -2)
                                {
                                    break backToMain;
                                } else if (transferPlayer == null)
                                {
                                    display.appendText("Die speler bestaat niet!");
                                } else if (!team.getTeamInfo().getPlayers().contains(transferPlayer))
                                {
                                    display.appendText("Die speler zit niet in je team!");
                                } else if (team.getTeamInfo().getPlayerPlacement().isPlaced(transferPlayer))
                                {
                                    display.appendText("Die speler zit al in je opstelling!");
                                } else
                                {
                                    // PositionSelection loop
                                    while (true)
                                    {
                                        String givenPosition = display.readLine(false, "Typ de eerste twee letters van de positie waar je de speler wilt neerzetten");

                                        // Gegeven string vergelijken met posities
                                        for (Position loopPosition : Position.values())
                                        {
                                            if (loopPosition.name().contains(givenPosition.toUpperCase()) || loopPosition.getDutchAlias().contains(givenPosition.toUpperCase()))
                                            {
                                                position = loopPosition;
                                            }
                                        }

                                        // Checken of de positie bestaat en of de transfer mogelijk is
                                        if (position == null)
                                        {
                                            display.appendText("Die positie bestaat niet!");
                                        } else if (position == Position.KEEPER && team.getTeamInfo().getPlayerPlacement().getKeeper() != playerInPlacement)
                                        {
                                            display.appendText("Je kan niet twee keepers hebben!\nAls je een speler als keeper wilt hebben zul je die moeten wisselen met de huidige keeper");
                                        } else
                                        {
                                            // Opstelling veranderen en een leuk berichtje sturen
                                            display.clearText();
                                            team.getTeamInfo().getPlayerPlacement().exchangePlayers(playerInPlacement, transferPlayer, position);
                                            display.appendText("Je hebt de speler " + transferPlayer.getPlayerName() + " op het veld gezet als een " + position.getDutchAlias().toLowerCase());

                                            break backMenu;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Checken of de gebruiker mensen wilt trainen
            if (mainNumber == -4)
            {
                // Trainen menu
                display.clearText();
                display.appendText("\t\t- - - - - - - - - - [ Team trainen ] - - - - - - - - - - ",
                        "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                        "Typ '-3' om een speler te trainen",
                        "Typ '-4' om het hele team te trainen",
                        "Typ '-5' om je coach te trainen"
                );

                trainen:
                // Training loop
                while (true)
                {
                    ArrayList<Player> players = new ArrayList<Player>();
                    PlayerStats.Stat stat = null;

                    int option = (int) display.readDouble(false);

                    // Checken of de gebruiker terug wil
                    if (option == -1)
                    {
                        break;
                    }

                    if (option == -2)
                    {
                        break backToMain;
                    }

                    // Checken of de gebruiker maar één speler wilt trainen
                    if (option == -3)
                    {
                        display.clearText();
                        spelerTrainen:
                        // TrainPlayer loop
                        while (true)
                        {
                            display.appendText("\t\t- - - - - - - - - - [ Speler trainen ] - - - - - - - - - - ",
                                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                                    "Typ het ID van de speler om te trainen:"
                            );

                            // Alle spelers laten zien
                            for (Player player : BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers())
                            {
                                display.appendText("  " + player.getPlayerName() + " (ID: " + player.getID() + ")");
                            }

                            // PlayerSelection loop
                            while (true)
                            {
                                // Speler met gegeven ID verkijgen
                                int playerID = (int) display.readDouble(false);
                                Player player = BAVM.getPlayerManager().getPlayer(playerID);

                                // Checken of de gebruiker terug wil
                                if (playerID == -1)
                                {
                                    break;
                                }

                                if (playerID == -2)
                                {
                                    break backToMain;
                                }

                                // Checken of de speler wel bestaat
                                if (player == null)
                                {
                                    display.appendText("Die speler bestaat niet!");
                                } else
                                {
                                    // Speler toevoegen aan de 'train-array'
                                    players.add(player);

                                    // Option naar -4 zetten zodat hij de team training triggerd
                                    option = -4;
                                    break spelerTrainen;
                                }
                            }
                        }
                    }

                    // Checken of de speler het hele team wil trainen
                    if (option == -4)
                    {
                        // Checken of er al spelers is de arraylist zitten (dus of de gebruiker eigenlijk '-3' als optie gaf)
                        if (players.isEmpty())
                        {
                            display.clearText();
                            display.appendText("\n\t\t- - - - - - - - - - [ Team trainen ] - - - - - - - - - - ",
                                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu"
                            );

                            // Alle spelers aan de 'train-array' toevoegen
                            players.addAll(BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers().stream().collect(Collectors.toList()));
                        }

                        display.appendText("Typ de eerste twee letters van de stat die je wilt trainen: ");

                        // Alle stats laten zien
                        for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                        {
                            display.appendText("  " + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase());
                        }

                        // StatSelection loop
                        while (true)
                        {
                            String statCharacters = display.readLine(false, "");

                            // Gegeven string vergelijken met alle stats
                            for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                            {
                                if (stats.name().contains(statCharacters.toUpperCase()))
                                {
                                    stat = stats;
                                }
                            }

                            // Checken of de gebruiker terug wil
                            if (statCharacters.equals("-1"))
                            {
                                break trainen;
                            }

                            if (statCharacters.equals("-2"))
                            {
                                break backToMain;
                            }

                            // Checken of de stat bestaat
                            if (stat == null)
                            {
                                display.appendText("Die stat bestaat niet!");
                            } else
                            {
                                // De prijs van de training berekenen
                                // Formule prijs: | (CURRENSTAT^2 * (CURRENSTAT/100)) * CURRENSTAT + UPGRADE * 15000 - PLAYERS * 1234 |

                                final double stepSize = 2.6;
                                double price = 0;
                                final double givenUpgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");

                                // Door de 'train-array' loopen
                                for (Player player : players)
                                {
                                    double currentStat = player.getPlayerStats().getValue(stat);
                                    final double upgrade = (currentStat + givenUpgrade <= 100
                                            ? givenUpgrade
                                            : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));
                                    final double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

                                    // Prijs berekenen
                                    for (double i = upgrade; i >= 0; i -= stepSize)
                                    {
                                        // Checken of de huidige stat en stapgrootte de nieuwe stat overschrijden
                                        if (currentStat + stepSize > newStat)
                                        {
                                            price += Math.abs((currentStat * currentStat * (currentStat / 100)) * currentStat + Math.abs(currentStat - newStat) * 15000 - players.size() * 1234);
                                            currentStat += Math.abs(currentStat - newStat);
                                        } else
                                        {
                                            price += Math.abs((currentStat * currentStat * (currentStat / 100)) * currentStat + stepSize * 15000 - players.size() * 1234);
                                            currentStat += stepSize;
                                        }
                                    }
                                }

                                display.appendText("Je staat op het punt " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase() + " voor " + (players.size() == 1 ? players.get(0).getPlayerName() : "het hele team") + " te upgraden met " + decimalFormat.format(givenUpgrade) + " voor $" + decimalFormat.format(price));

                                // Door de 'train-array' loopen
                                for (Player player : players)
                                {
                                    double currentStat = player.getPlayerStats().getValue(stat);
                                    double upgrade = (currentStat + givenUpgrade <= 100
                                            ? givenUpgrade
                                            : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));

                                    // Door alle stats loopen
                                    for (PlayerStats.Stat stats : PlayerStats.Stat.values())
                                    {
                                        // Checken of de huidige stat de stat is die getraint wordt
                                        if (stat != stats)
                                        {
                                            continue;
                                        }

                                        // Upgrade laten zien voor iedere speler
                                        display.appendText("   " + player.getPlayerName() + " (" + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase() + "): " + decimalFormat.format(player.getPlayerStats().getValue(stats)) + (" (Nieuwe waarde: " + decimalFormat.format((currentStat + upgrade <= 100 ? currentStat + upgrade : 100)) + ")"));
                                    }
                                }

                                // Training bevestigen
                                if (display.readLine(false, "Typ 123 om te bevestigen").equals("123"))
                                {
                                    // Checken of er genoeg geld is
                                    if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price)
                                    {
                                        display.clearText();
                                        display.appendText("Je hebt niet genoeg geld voor deze training!");
                                        break trainen;
                                    }

                                    // Door de 'train-array' loopen
                                    for (Player player : players)
                                    {
                                        double currentStat = player.getPlayerStats().getValue(stat);
                                        double upgrade = (currentStat + givenUpgrade <= 100
                                                ? givenUpgrade
                                                : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));

                                        // Speler de nieuwe stat geven
                                        player.getPlayerStats().increaseSkill(stat, upgrade);
                                    }

                                    // Geld verwijderen en berichtje sturen
                                    display.clearText();
                                    BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);
                                    display.appendText("De stat '" + stat.name().toLowerCase() + "' is voor " + (players.size() == 1 ? players.get(0).getPlayerName() : "iedereen") + " verhoogd met " + givenUpgrade + "!");
                                    break trainen;
                                } else
                                {
                                    display.clearText();
                                    display.appendText("Trainen geannuleerd.");
                                    break trainen;
                                }
                            }
                        }
                    }

                    // Checken of de gebruiker alleen de coach wil trainen
                    if (option == -5)
                    {
                        CoachStats.CStat coachStat = null;

                        display.clearText();
                        display.appendText("\t\t- - - - - - - - - - [ Coach trainen ] - - - - - - - - - - ",
                                "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                                "Typ de eerste twee letters van de stat die je wilt trainen: "
                        );

                        // Alle coachStats laten zien
                        for (CoachStats.CStat stats : CoachStats.CStat.values())
                        {
                            display.appendText("  " + stats.name().substring(0, 1) + stats.name().substring(1).toLowerCase());
                        }

                        // StatSelection loop
                        while (true)
                        {
                            String statCharacters = display.readLine(false, "");

                            // Gegeven string vergelijken met alle coachStats
                            for (CoachStats.CStat stats : CoachStats.CStat.values())
                            {
                                if (stats.name().contains(statCharacters.toUpperCase()))
                                {
                                    coachStat = stats;
                                }
                            }

                            // Checken of de gebruiker terug wil
                            if (statCharacters.equals("-1"))
                            {
                                break trainen;
                            }

                            if (statCharacters.equals("-2"))
                            {
                                break backToMain;
                            }

                            // Checken of de stat bestaat
                            if (coachStat == null)
                            {
                                display.appendText("Die stat bestaat niet!");
                            } else
                            {
                                // De prijs van de training berekenen
                                // Formule prijs: | (CURRENTSTAT^2 * (CURRENTSTAT / 100)) * currentStat + | CURRENSTAT - NEWSTAT | * 15000 - (RANDOM * 10) * 1234 |
                                final Coach coach = BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamCoach();
                                final double stepSize = 2.6;
                                double price = 0;
                                final double givenUpgrade = display.readDouble(false, "Hoeveel punten moeten erbij komen?");
                                double currentStat = coach.getCoachStats().getValue(coachStat);
                                double upgrade = (currentStat + givenUpgrade <= 100
                                        ? givenUpgrade
                                        : givenUpgrade - Math.abs((currentStat + givenUpgrade) - 100));
                                double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

                                // Prijs berekenen
                                for (double i = upgrade; i >= 0; i -= stepSize)
                                {
                                    // Checken of de huidige stat en stapgrootte de nieuwe stat overschrijden
                                    if (currentStat + stepSize > newStat)
                                    {
                                        price += Math.abs((currentStat * currentStat * (currentStat / 100)) * currentStat + Math.abs(currentStat - newStat) * 15000 - ((int) (Math.random() * 10)) * 1234);
                                        currentStat += Math.abs(currentStat - newStat);
                                    } else
                                    {
                                        price += Math.abs((currentStat * currentStat * (currentStat / 100)) * currentStat + stepSize * 15000 - ((int) (Math.random() * 10)) * 1234);
                                        currentStat += stepSize;
                                    }
                                }

                                display.appendText("Je staat op het punt " + coachStat.name().substring(0, 1) + coachStat.name().substring(1).toLowerCase() + " voor je coach te upgraden met " + decimalFormat.format(givenUpgrade) + " voor $" + decimalFormat.format(price));
                                display.appendText(" " + coach.getCoachName() + " (" + coachStat.name().substring(0, 1) + coachStat.name().substring(1).toLowerCase() + "): " + decimalFormat.format(coach.getCoachStats().getValue(coachStat)) + " (Nieuwe waarde: " + decimalFormat.format(newStat) + ")");

                                // Gebruiker de training laten bevestigen
                                if (display.readLine(false, "Typ 123 om te bevestigen").equals("123"))
                                {
                                    // Checken of er genoeg geld is
                                    if (BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price)
                                    {
                                        display.clearText();
                                        display.appendText("Je hebt niet genoeg geld voor deze training!");
                                        break trainen;
                                    }

                                    // Stats verhogen en geld verwijderen
                                    coach.getCoachStats().increaseSkill(coachStat, upgrade);
                                    BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);

                                    // Leuk berichtje sturen
                                    display.appendText("De stat '" + coachStat.name().toLowerCase() + "' is voor " + coach.getCoachName() + " verhoogd met " + givenUpgrade + "!");
                                    break trainen;
                                } else
                                {
                                    display.clearText();
                                    display.appendText("Trainen geannuleerd.");
                                    break trainen;
                                }
                            }
                        }
                    }
                }
            }

            // Checken of de gebruiker de naam van de directeur wilt veranderen
            if (mainNumber == -5)
            {
                // SelectName loop
                while (true)
                {
                    String directorName = display.readLine(false, "Typ de naam van je personage! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

                    // Checken of de gebruiker terug wilt
                    if (directorName.equals("-1"))
                    {
                        break;
                    } else if (directorName.equals("-2"))
                    {
                        break backToMain;
                    }

                    display.appendText("Je staat op het punt om je personage de naam " + directorName + " te geven. Weet je het zeker?");

                    // Gebruiker de verandering laten bevestigen
                    if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123"))
                    {
                        // Naam veranderen
                        BAVM.getTeamManager().getTeam(19).setDirectorName(directorName);
                        break;
                    } else
                    {
                        display.appendText("Naamgeving geannuleerd.");
                    }
                }
            }

            // Checken of de gebruiker de naam van de club wil veranderen
            if (mainNumber == -6)
            {
                // SelectName loop
                while (true)
                {
                    String teamName = display.readLine(false, "Typ de naam van je team! Typ -1 om de stap over te slaan (de naam wordt dan willekeurig gegenereerd).");

                    // Checken of de gebruiker terug wil
                    if (teamName.equals("-1"))
                    {
                        break;
                    } else if (teamName.equals("-2"))
                    {
                        break backToMain;
                    }

                    display.appendText("Je staat op het punt om je team de naam " + teamName + " te geven. Weet je het zeker?");

                    // Speler de verandering laten bevestigen
                    if (display.readLine(false, "Typ 123 om de naamgeving te bevestigen.").equals("123"))
                    {
                        // Naam veranderen
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
