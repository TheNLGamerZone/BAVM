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

    /**
     * Position constructor
     *
     * @param id           ID van positie
     * @param minStart     Minimale hoeveelheid spelers
     * @param maxStart     Maximale hoeveeheid spelers
     * @param marketFilter De bijhorende marketfilet
     * @param dutchAlias   De Nederlandse naam
     */
    private Position(int id, int minStart, int maxStart, MarketFilter marketFilter, String dutchAlias)
    {
        this.id = id;
        this.minStart = minStart;
        this.maxStart = maxStart;
        this.marketFilter = marketFilter;
        this.dutchAlias = dutchAlias;
    }

    /**
     * Returnt het ID van de positie
     *
     * @return ID
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Returnt de hoeveelheid spelers die op deze positie speler in de opstelling
     *
     * @return De hoeveelheid spelers
     */
    public int getStartPlayers()
    {
        return Math.random() < .5 ? maxStart : minStart;
    }

    /**
     * Returnt de bijhorende marketfilter
     *
     * @return De marketfilter
     */
    public MarketFilter getMarketFiler()
    {
        return this.marketFilter;
    }

    /**
     * Returnt de Nederlandse naam
     *
     * @return De Nederlandse naam
     */
    public String getDutchAlias()
    {
        return this.dutchAlias;
    }
}
