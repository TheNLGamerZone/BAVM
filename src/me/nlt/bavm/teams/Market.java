package me.nlt.bavm.teams;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Market
{
    public enum MarketFilter
    {
        ALL(0), KEEPER(0), ATTACKER(0), DEFENDER(0), MIDFIELDER(0),
        PRICE_HIGH_LOW(1), PRICE_LOW_HIGH(1), STATS_HIGH_LOW(1), STATS_LOW_HIGH(1);

        private int filterType;

        private MarketFilter(int filterType)
        {
            this.filterType = filterType;
        }

        public int getFilterType()
        {
            return this.filterType;
        }
    }

    public static String[] listPlayers(MarketFilter... marketFilters)
    {
        ArrayList<MarketFilter> positionFilter = new ArrayList<>();
        ArrayList<MarketFilter> sortingFilter = new ArrayList<>();

        for (MarketFilter filter : marketFilters)
        {
            if (filter.getFilterType() == 0)
            {
                positionFilter.add(filter);
            } else
            {
                sortingFilter.add(filter);
            }
        }

        ArrayList<Player> filteredPlayers = getFilteredPlayers(positionFilter.toArray(new MarketFilter[positionFilter.size()]));
        ArrayList<Player> sortedPlayers = sortPlayers(filteredPlayers, sortingFilter.toArray(new MarketFilter[sortingFilter.size()]));
        ArrayList<String> marketStrings = new ArrayList<>();

        for (int i = 0; i < sortedPlayers.size(); i++)
        {
            Player player = sortedPlayers.get(i);

            marketStrings.add((i + 1) + ": " +player.getPlayerName() + " - Position: " + player.getPosition().name().toLowerCase() + " - Skill: " + player.getPlayerStats().getTotalSkill() + " - Price: $100");
        }

        return marketStrings.toArray(new String[marketStrings.size()]);
    }

    private static ArrayList<Player> getFilteredPlayers(MarketFilter... marketFilters)
    {
        ArrayList<Player> playersInMarket = BAVM.getTeamManager().marketTeam.getTeamInfo().getPlayers();
        ArrayList<Player> filteredPlayers = new ArrayList<>();

        for (MarketFilter filter : marketFilters)
        {
            if (filter == MarketFilter.ALL)
            {
                return playersInMarket;
            }

            filteredPlayers.addAll(playersInMarket.stream().filter(player -> player.getPosition().getMarketFiler() == filter).collect(Collectors.toList()));
        }

        return filteredPlayers;
    }

    private static ArrayList<Player> sortPlayers(ArrayList<Player> players, MarketFilter... marketFilters)
    {
        if (marketFilters == null || marketFilters.length == 0)
        {
            return players;
        }

        return players;
    }
}
