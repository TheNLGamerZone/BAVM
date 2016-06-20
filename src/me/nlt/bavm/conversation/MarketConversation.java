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
    public void startConversation(Display display)
    {
        HashMap<Market.MarketFilter, Boolean> marketFilters = new HashMap<>();

        for (Market.MarketFilter marketFilter : Market.MarketFilter.values())
        {
            marketFilters.put(marketFilter, (marketFilter == Market.MarketFilter.ALL ||
                    marketFilter == Market.MarketFilter.KEEPER ||
                    marketFilter == Market.MarketFilter.ATTACKER ||
                    marketFilter == Market.MarketFilter.DEFENDER ||
                    marketFilter == Market.MarketFilter.MIDFIELDER ||
                    marketFilter == Market.MarketFilter.STATS_HIGH_LOW));
        }

        backToMain:
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

            if (mainNumber == -1 || mainNumber == -2)
            {
                break backToMain;
            }

            if (mainNumber == -3)
            {
                while (true)
                {
                    display.clearText();
                    display.appendText("\t\t- - - - - - - - - - - - - [ Filters aanpassen ]- - - - - - - - - - - - - ", "Typ het nummer van de filter om de filter aan/uit te zetten", "Typ '-1' om terug te keren naar de markt");

                    int counter = 1;
                    for (Market.MarketFilter marketFilter : marketFilters.keySet())
                    {
                        display.appendText(counter + ": " + marketFilter.getDisplayName() + (marketFilters.get(marketFilter) ? "Actief" : "Inactief"));
                        counter++;
                    }

                    int number = (int) display.readDouble(false);

                    if (number == -1)
                    {
                        break;
                    }

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    List<Market.MarketFilter> filters = new ArrayList<>(marketFilters.keySet());

                    for (int i = 0; i < filters.size(); i++)
                    {
                        if (number == i + 1)
                        {
                            marketFilters.put(filters.get(i), !marketFilters.get(filters.get(i)));
                            display.appendText("Je hebt " + filters.get(i).name() + " omgezet naar " + (marketFilters.get(filters.get(i)) ? "actief" : "inactief") + "!\n");

                            for (Market.MarketFilter filter : filters)
                            {
                                if (filter.getFilterType() == filters.get(i).getFilterType()
                                        && filter.getFilterType() == 1
                                        && marketFilters.get(filter)
                                        && filter != filters.get(i))
                                {
                                    marketFilters.put(filter, false);
                                }

                                if (filters.get(i) == Market.MarketFilter.ALL
                                        && filter.getFilterType() == 0
                                        && filter != filters.get(i)
                                        && marketFilters.get(filters.get(i)))
                                {
                                    marketFilters.put(filter, true);
                                }

                                if (filters.get(i).getFilterType() == 0
                                        && marketFilters.get(Market.MarketFilter.ALL)
                                        && !marketFilters.get(filters.get(i)))
                                {
                                    marketFilters.put(Market.MarketFilter.ALL, false);
                                }

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

                    if (number <= 0 || number > filters.size())
                    {
                        display.appendText("Dat is geen geldig nummer!");
                    }
                }
            }

            if (mainNumber == -4)
            {
                ArrayList<Market.MarketFilter> filters = new ArrayList<>();

                for (Market.MarketFilter marketFilter : marketFilters.keySet())
                {
                    if (marketFilters.get(marketFilter))
                    {
                        filters.add(marketFilter);
                    }
                }

                marketFilters.keySet().stream().filter(marketFilter -> filters.contains(Market.MarketFilter.ALL) && marketFilter.getFilterType() == 0 && marketFilter != Market.MarketFilter.ALL).forEach(filters::remove);

                display.clearText();
                display.appendText("\t\t- - - - - - - - - - - - [ Zoeken naar spelers ] - - - - - - - - - - - - ", "Filters: " + filters.toString());

                display.appendText(Market.listPlayers(filters));

                while (true)
                {
                    display.appendText("\nTyp het ID van een speler in om hem te kopen.\nOf typ '-1' om terug te gaan naar de markt.");
                    int number = (int) display.readDouble(false);

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    if (number == -1)
                    {
                        break;
                    }

                    Player player = BAVM.getPlayerManager().getPlayer(number);

                    if (player == null)
                    {
                        display.appendText("Dat is geen speler!");
                    } else
                    {
                        display.appendText("Je staat op het punt " + player.getPlayerName() + " te kopen voor $" + decimalFormat.format(player.getMarketValue()) + ", weet je het zeker?\nTyp 123 om de aankoop te bevestigen."
                                , "Saldo na aankoop: $" + decimalFormat.format(BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamGeld().getCurrentGeld() - player.getMarketValue()));

                        int confirmationNumber = (int) display.readDouble(false);

                        if (confirmationNumber == 123)
                        {
                            TransferResult transferResult = BAVM.getTeamManager().transferPlayer(BAVM.getTeamManager().marketTeam, BAVM.getTeamManager().playerTeam, player, (int) player.getMarketValue());

                            display.appendText(transferResult.getMessage());
                        } else
                        {
                            display.appendText("Aankoop afgebroken.");
                            break;
                        }
                    }
                }
            }

            if (mainNumber == -5)
            {
                ArrayList<Player> players = BAVM.getTeamManager().playerTeam.getTeamInfo().getPlayers();

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

                    if (number == -1)
                    {
                        break;
                    }

                    if (number == -2)
                    {
                        break backToMain;
                    }

                    Player player = BAVM.getPlayerManager().getPlayer(number);

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

                        if (display.readLine(false, "").equals("123"))
                        {
                            TransferResult transferResult = BAVM.getTeamManager().transferPlayer(BAVM.getTeamManager().playerTeam, BAVM.getTeamManager().marketTeam, player, (int) player.getMarketValue());

                            display.appendText(transferResult.getMessage());
                        } else
                        {
                            display.appendText("Verkoop afgebroken.");
                            break;
                        }
                    }
                }
            }

            if (mainNumber == -6)
            {
                display.clearText();
                display.appendText("\t\t- - - - - - - - - - - - [ Coach aannemen ] - - - - - - - - - - - - ");

                for (Object object : BAVM.getCoachManager().getFreeCoaches())
                {
                    Coach coach = (Coach) object;
                    double price = coach.getCoachStats().getTotalSkill() * ((Math.random() + Math.random()) * 1242);

                    display.appendText(" " + coach.getCoachName() + " - ID: " + coach.getID() + " - Skill: " + decimalFormat.format(coach.getCoachStats().getTotalSkill()) + " - Prijs: $" + decimalFormat.format(price));
                }

                while (true)
                {
                    int coachID = (int) display.readDouble(false, "Typ het ID van de coach in die je wilt aannemen");
                    Coach coach = BAVM.getCoachManager().getCoach(coachID);
                    Coach currentCoach = BAVM.getTeamManager().playerTeam.getTeamInfo().getTeamCoach();

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
                        double price = coach.getCoachStats().getTotalSkill() * 1.34 * 1242;
                        double priceCurrentCoach = currentCoach.getCoachStats().getTotalSkill() * 1.21 * 824;

                        if (display.readLine(false, "Je staat op het punt '" + coach.getCoachName() + "' als coach in te ruilen voor '" + currentCoach.getCoachName() + "' voor $" + decimalFormat.format((price - priceCurrentCoach) * 10) + "\nTyp 123 om te bevestigen.").equals("123"))
                        {
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
