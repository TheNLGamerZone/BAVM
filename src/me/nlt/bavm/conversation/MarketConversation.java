package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;
import me.nlt.bavm.teams.Market;

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
            marketFilters.put(marketFilter, (marketFilter == Market.MarketFilter.ALL));
        }

        backToMain:
        while (true)
        {
            display.appendText("Welkom bij de markt!",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je filters aan te passen",
                    "Typ '-4' om te zoeken"
            );

            int mainNumber = (int) display.readDouble();

            if (mainNumber == -1 || mainNumber == -2)
            {
                break backToMain;
            }

            if (mainNumber == -3)
            {
                display.appendText("\n---[ Filters aanpassen ]---", "Typ het nummer van de filter om de filter aan/uit te zetten", "Typ '-1' om terug te keren naar de markt");

                while (true)
                {
                    int counter = 1;
                    for (Market.MarketFilter marketFilter : marketFilters.keySet())
                    {
                        display.appendText(counter + ": " + marketFilter.name() + " -> " + marketFilters.get(marketFilter));
                        counter++;
                    }

                    int number = (int) display.readDouble();

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
                            display.appendText("\nJe hebt " + filters.get(i).name() + " omgezet naar " + marketFilters.get(filters.get(i)));
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

                //TODO: Afmaken
            }
        }
    }
}
