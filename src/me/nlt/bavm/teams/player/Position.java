package me.nlt.bavm.teams.player;

import me.nlt.bavm.teams.Market.MarketFilter;

public enum Position
{
    KEEPER(0, 2, 3, MarketFilter.KEEPER, "KEEPER"),
    DEFENDER(1, 5, 6, MarketFilter.DEFENDER, "VERDEDIGER"),
    MIDFIELDER(2, -1, -1, MarketFilter.MIDFIELDER, "MIDDENVELDER"),
    ATTACKER(3, 6, 7, MarketFilter.ATTACKER, "AANVALLER");

    private int id;
    private int minStart;
    private int maxStart;
    private MarketFilter marketFilter;
    private String dutchAlias;

    private Position(int id, int minStart, int maxStart, MarketFilter marketFilter, String dutchAlias)
    {
        this.id = id;
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.marketFilter = marketFilter;
        this.dutchAlias = dutchAlias;
    }

    public int getId()
    {
        return this.id;
    }

    public int getStartPlayers()
    {
        return Math.random() < .5 ? maxStart : minStart;
    }
    
    public MarketFilter getMarketFiler()
    {
    	return this.marketFilter;
    }

    public String getDutchAlias()
    {
        return this.dutchAlias;
    }
}
