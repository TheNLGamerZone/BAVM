package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.Market;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.team.TransferResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketConversation implements Conversation
{
    @Override
    /**
     * Starten van conversatie voor de markt
     */
    public void startConversation(Display display)
    {
        HashMap<Market.MarketFilter, Boolean> marketFilters = new HashMap<>();

        // Door alle filters loopen en die in de hashmap doen
        for (Market.MarketFilter marketFilter : Market.MarketFilter.values())
        {
            // Standaard filters zijn: Alle posities en stats van hoog naar laag
            marketFilters.put(marketFilter, (marketFilter == Market.MarketFilter.ALL ||
                    marketFilter == Market.MarketFilter.KEEPER ||
                    marketFilter == Market.MarketFilter.ATTACKER ||
                    marketFilter == Market.MarketFilter.DEFENDER ||
                    marketFilter == Market.MarketFilter.MIDFIELDER ||
                    marketFilter == Market.MarketFilter.STATS_HIGH_LOW));
        }

        backToMain:
        // Market loop
        while (true)
        {
            display.clearText();
            display.appendText("\t\t- - - - - - - - - - - - - - - - [ De Markt ] - - - - - - - - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je filters aan te passen",
                    "Typ '-4' om te zoeken met de actieve filters",
                    "Typ '-5' om spelers te verkopen",
                    "Typ '-6' om een coach aan te nemen"
            );

            int mainNumber = (int) display.readDouble(false);

            // Checken of de gebruiker terug wil
            if (mainNumber == -1 || mainNumber == -2)
            {
                break backToMain;
            }

            // Checken of de gebruiker de filters wilt aanpassen
            if (mainNumber == -3)
            {
                while (true)
                {
                    display.clearText();
                    display.appendText("\t\t- - - - - - - - - - - - - [ Filters aanpassen ]- - - - - - - - - - - - - ",
                            "Typ '-1' om terug te keren naar de markt",
                            "Typ het nummer van de filter om de filter aan/uit te zetten"
                    );

                    int counter = 1;
                    // Door de filters loopen en ze laten zien
                    for (Market.MarketFilter marketFilter : marketFilters.keySet())
                    {
                        display.appendText(counter + ": " + marketFilter.getDisplayName() + (marketFilters.get(marketFilter) ? "Actief" : "Inactief"));
                        counter++;
                    }

                    int number = (int) display.readDouble(false);

                    // Checken of de gebruiker terug wil
                    if (number == -1)
                    {
                        break;
                    }

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    List<Market.MarketFilter> filters = new ArrayList<>(marketFilters.keySet());

                    // Door de filters loopen
                    for (int i = 0; i < filters.size(); i++)
                    {
                        // Checken of de huidige filter degene is die de gebruiker wilt togglen
                        if (number == i + 1)
                        {
                            // Marketfilter togglen en een berichtje sturen
                            marketFilters.put(filters.get(i), !marketFilters.get(filters.get(i)));
                            display.appendText("Je hebt " + filters.get(i).name() + " omgezet naar " + (marketFilters.get(filters.get(i)) ? "actief" : "inactief") + "!\n");

                            // Weer door alle filters loopen
                            for (Market.MarketFilter filter : filters)
                            {
                                // Alle sorteer naar false zetten behalve degene die aangepast is
                                if (filter.getFilterType() == filters.get(i).getFilterType()
                                        && filter.getFilterType() == 1
                                        && marketFilters.get(filter)
                                        && filter != filters.get(i))
                                {
                                    marketFilters.put(filter, false);
                                }

                                // Alle positie filters naar true zetten als de de ALL filter naar true is gezet
                                if (filters.get(i) == Market.MarketFilter.ALL
                                        && filter.getFilterType() == 0
                                        && filter != filters.get(i)
                                        && marketFilters.get(filters.get(i)))
                                {
                                    marketFilters.put(filter, true);
                                }

                                // ALL naar false zetten als een positiefilter naar false is gezet
                                if (filters.get(i).getFilterType() == 0
                                        && marketFilters.get(Market.MarketFilter.ALL)
                                        && !marketFilters.get(filters.get(i)))
                                {
                                    marketFilters.put(Market.MarketFilter.ALL, false);
                                }

                                // ALL naar true zetten als alle positiefilters op true staan
                                if (marketFilters.get(Market.MarketFilter.ATTACKER)
                                        && marketFilters.get(Market.MarketFilter.DEFENDER)
                                        && marketFilters.get(Market.MarketFilter.KEEPER)
                                        && marketFilters.get(Market.MarketFilter.MIDFIELDER)
                                        && !marketFilters.get(Market.MarketFilter.ALL)
                                        && filters.get(i) != Market.MarketFilter.ALL)
                                {
                                    marketFilters.put(Market.MarketFilter.ALL, true);
                                }
                            }
                            break;
                        }
                    }

                    // Checken of het een geldig nummer was
                    if (number <= 0 || number > filters.size())
                    {
                        display.appendText("Dat is geen geldig nummer!");
                    }
                }
            }

            // Checken of de gebruiker wilt zoeken naar spelers
            if (mainNumber == -4)
            {
                ArrayList<Market.MarketFilter> filters = new ArrayList<>();

                // Door de filters loopen
                for (Market.MarketFilter marketFilter : marketFilters.keySet())
                {
                    // Als de filter actief is wordt ie toegevoegd aan de zoekfilters
                    if (marketFilters.get(marketFilter))
                    {
                        filters.add(marketFilter);
                    }
                }

                // Als de ALL filter aanwezig is hoeven de overige positiefilters niet vermeld te worden bij de zoekpagina
                marketFilters.keySet().stream().filter(marketFilter -> filters.contains(Market.MarketFilter.ALL) && marketFilter.getFilterType() == 0 && marketFilter != Market.MarketFilter.ALL).forEach(filters::remove);

                display.clearText();
                display.appendText("\t\t- - - - - - - - - - - - [ Zoeken naar spelers ] - - - - - - - - - - - - ",
                        "Filters: " + filters.toString());

                // Gesorteerde spelerlijst laten zien
                display.appendText(Market.listPlayers(filters));

                // PlayerSelection loop
                while (true)
                {
                    // Speler met gegeven ID verkrijgen
                    display.appendText("\nTyp het ID van een speler in om hem te kopen.\nOf typ '-1' om terug te gaan naar de markt.");
                    int number = (int) display.readDouble(false);

                    // Checken of de gebruiker terug wil
                    if (number == -1)
                    {
                        break;
                    }

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    Player player = BAVM.getPlayerManager().getPlayer(number);

                    // Kijken of de speler bestaat
                    if (player == null)
                    {
                        display.appendText("Dat is geen speler!");
                    } else
                    {
                        display.appendText("Je staat op het punt " + player.getPlayerName() + " te kopen voor $" + decimalFormat.format(player.getMarketValue()) + ", weet je het zeker?\nTyp 123 om de aankoop te bevestigen."
                                , "Saldo na aankoop: $" + decimalFormat.format(BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() - player.getMarketValue()));

                        int confirmationNumber = (int) display.readDouble(false);

                        // Speler de aankoop laten bevestigen
                        if (confirmationNumber == 123)
                        {
                            // Transfer starten
                            TransferResult transferResult = BAVM.getTeamManager().transferPlayer(BAVM.getTeamManager().marketTeam, BAVM.getTeamManager().playerTeam, player, (int) player.getMarketValue());

                            // Resultaat printen
                            display.appendText(transferResult.getMessage());
                        } else
                        {
                            display.appendText("Aankoop afgebroken.");
                            break;
                        }
                    }
                }
            }

            // Checken of de gebruiker spelers wilt verkopen
            if (mainNumber == -5)
            {
                ArrayList<Player> players = BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers();

                // PlayerSelection loop
                while (true)
                {
                    display.clearText();
                    display.appendText("\t\t- - - - - - - - - - - - [ Spelers verkopen ] - - - - - - - - - - - - ");

                    int counter = 1;

                    for (Player player : players)
                    {
                        display.appendText(counter + ": " + player.getPlayerName()
                                + " - Positie: " + player.getPosition().getDutchAlias().toLowerCase()
                                + " - Skill: " + decimalFormat.format(player.getPlayerStats().getTotalSkill())
                                + " - Waarde: $" + decimalFormat.format(player.getMarketValue())
                                + " - ID: " + player.getPlayerID()
                                + " - In opstelling: " + BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayerPlacement().isPlaced(player));
                        counter++;
                    }

                    display.appendText("\nTyp het ID van de speler in om hem te verkopen.\n" +
                            "Of typ '-1' om terug te gaan naar de markt.");

                    int number = (int) display.readDouble(false);

                    // Checken of de gebruiker terug wil
                    if (number == -1)
                    {
                        break;
                    }

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    // Speler met gegeven ID verkrijgen
                    Player player = BAVM.getPlayerManager().getPlayer(number);

                    // Checken of de speler bestaat
                    if (player == null)
                    {
                        display.appendText("Dat is geen speler!");
                    } else if (!BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers().contains(player))
                    {
                        display.appendText("Deze speler zit niet in je team!");
                    } else
                    {
                        display.appendText("Je staat op het punt " + player.getPlayerName() + " te verkopen voor $" + decimalFormat.format(player.getMarketValue()) + ", weet je het zeker?\nTyp 123 om de verkoop te bevestigen."
                                , "Saldo na verkoop: $" + decimalFormat.format(BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() + player.getMarketValue()));

                        // Speler de verkoop laten bevestigen
                        if (display.readLine(false, "").equals("123"))
                        {
                            // Transfer starten
                            TransferResult transferResult = BAVM.getTeamManager().transferPlayer(BAVM.getTeamManager().playerTeam, BAVM.getTeamManager().marketTeam, player, (int) player.getMarketValue());

                            // Resultaat printen
                            display.appendText(transferResult.getMessage());
                        } else
                        {
                            display.appendText("Verkoop afgebroken.");
                            break;
                        }
                    }
                }
            }

            // Checken of de gebruiker een coach wilt aannemen
            if (mainNumber == -6)
            {
                display.clearText();
                display.appendText("\t\t- - - - - - - - - - - - [ Coach aannemen ] - - - - - - - - - - - - ");

                // Alle beschikbare coaches laten zien
                for (Object object : BAVM.getCoachManager().getFreeCoaches())
                {
                    Coach coach = (Coach) object;
                    double price = coach.getCoachStats().getTotalSkill() * ((Math.random() + Math.random()) * 1242);

                    display.appendText(" " + coach.getCoachName() + " - ID: " + coach.getID() + " - Skill: " + decimalFormat.format(coach.getCoachStats().getTotalSkill()) + " - Prijs: $" + decimalFormat.format(price));
                }

                // CoachSelection loop
                while (true)
                {
                    // Coach met gegeven ID verkrijgen
                    int coachID = (int) display.readDouble(false, "Typ het ID van de coach in die je wilt aannemen");
                    Coach coach = BAVM.getCoachManager().getCoach(coachID);
                    Coach currentCoach = BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamCoach();

                    // Checken of de gebruiker terug wil en of de coach bestaat
                    if (coachID == 1)
                    {
                        break;
                    } else if (coachID == -2)
                    {
                        break backToMain;
                    } else if (coach == null)
                    {
                        display.appendText("Die coach bestaat niet!");
                    } else
                    {
                        // Prijzen berekenen
                        double price = coach.getCoachStats().getTotalSkill() * 1.34 * 1242;
                        double priceCurrentCoach = currentCoach.getCoachStats().getTotalSkill() * 1.21 * 824;

                        // Gebruiker de aankoop laten bevestigen
                        if (display.readLine(false, "Je staat op het punt '" + coach.getCoachName() + "' als coach in te ruilen voor '" + currentCoach.getCoachName() + "' voor $" + decimalFormat.format((price - priceCurrentCoach) * 10) + "\nTyp 123 om te bevestigen.").equals("123"))
                        {
                            // Transfer starten en resultaat printen
                            display.appendText(BAVM.getCoachManager().transferCoach(coach, BAVM.getTeamManager().playerTeam, (price - priceCurrentCoach) * 10).getMessage());
                        } else
                        {
                            display.appendText("Coach aannemen afgebroken.");
                        }

                        break;
                    }
                }
            }
        }
    }
}
