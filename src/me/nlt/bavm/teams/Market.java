package me.nlt.bavm.teams;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Market
{
    public static boolean statsChanged = true;

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

    public static void calculatePlayerValues(PlayerManager playerManager)
    {
        int totalValue = 112341442;
        double totalSkill = 0;

        for (Object object : playerManager.getLoadedPlayers())
        {
            Player player = (Player) object;

            totalSkill += player.getPlayerStats().getTotalSkill();
        }

        for (Object object : playerManager.getLoadedPlayers())
        {
            Player player = (Player) object;
            double promille = player.getPlayerStats().getTotalSkill() / totalSkill * 1000;
            double newValue = totalValue / 1000 * promille;

            player.setMarketValue(newValue);
        }
    }

    public static String[] listPlayers(ArrayList<MarketFilter> marketFilters)
    {
        ArrayList<MarketFilter> positionFilter = new ArrayList<>();
        MarketFilter sortingFilter = null;

        for (MarketFilter filter : marketFilters)
        {
            if (filter.getFilterType() == 0)
            {
                positionFilter.add(filter);
            } else
            {
                if (sortingFilter == null)
                {
                    sortingFilter = filter;
                }
            }
        }

        if (marketFilters.isEmpty())
        {
            positionFilter.add(MarketFilter.ALL);
        }

        ArrayList<Player> filteredPlayers = getFilteredPlayers(positionFilter.toArray(new MarketFilter[positionFilter.size()]));
        ArrayList<Player> sortedPlayers = sortPlayers(filteredPlayers, sortingFilter);
        ArrayList<String> marketStrings = new ArrayList<>();

        for (int i = 0; i < sortedPlayers.size(); i++)
        {
            Player player = sortedPlayers.get(i);

            marketStrings.add((i + 1) + ": " + player.getPlayerName()
                    + " - Position: " + player.getPosition().name().toLowerCase()
                    + " - Skill: " + new DecimalFormat("###.##").format(player.getPlayerStats().getTotalSkill())
                    + " - Price: $" + new DecimalFormat("######.##").format(player.getMarketValue())
                    + " - ID: " + player.getPlayerID());
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

    private static ArrayList<Player> sortPlayers(ArrayList<Player> players, MarketFilter marketFilter)
    {
        ArrayList<Player> playerList = new ArrayList<>();

        if (marketFilter == null)
        {
            return players;
        }

        for (Player player : players)
        {
            double value = (marketFilter.name().contains("PRICE") ? player.getMarketValue() : player.getPlayerStats().getTotalSkill());

            if (playerList.isEmpty())
            {
                playerList.add(player);
                continue;
            }

            ArrayList<Player> playerCopy = new ArrayList<>();

            for (int i = 0; i < playerList.size(); i++)
            {
                Player loopPlayer = playerList.get(i);
                double loopValue = (marketFilter.name().contains("PRICE") ? loopPlayer.getMarketValue() : loopPlayer.getPlayerStats().getTotalSkill());

                if ((marketFilter.name().contains("LOW_HIGH") ? loopValue > value : loopValue < value))
                {
                    int size = playerList.size();
                    int cuts = 0;

                    for (int j = i; j < size; j++)
                    {
                        playerCopy.add(playerList.get(j));
                        cuts++;
                    }

                    for (int j = 0; j < cuts; j++)
                    {
                        int index = (playerList.size() == 1 ? 0 : playerList.size() - 1);
                        playerList.remove(index);
                    }

                    playerList.add(player);
                    playerList.addAll(playerCopy);
                    break;
                }

                if (i + 1 == playerList.size())
                {
                    playerList.add(player);
                    break;
                }
            }
        }

        players.clear();
        players.addAll(playerList);
        return players;
    }
}
