package me.nlt.bavm.teams.player;

import me.nlt.bavm.teams.Market.MarketFilter;

public enum Position
{
    KEEPER(0, 2, 3, MarketFilter.KEEPER), 
    DEFENDER(1, 5, 6, MarketFilter.DEFENDER), 
    MIDFIELDER(2, -1, -1, MarketFilter.MIDFIELDER), 
    ATTACKER(3, 6, 7, MarketFilter.ATTACKER);

    private int id;
    private int minStart;
    private int maxStart;
    private MarketFilter marketFilter;

    private Position(int id, int minStart, int maxStart, MarketFilter marketFilter)
    {
        this.id = id;
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.marketFilter = marketFilter;
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
}
