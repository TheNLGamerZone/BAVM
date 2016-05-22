package me.nlt.bavm.teams;

public class Player
{
    private String playerName;
    private int playerID;

    private PlayerStats playerStats;

    public Player(String playerName, int playerID, int[] stats)
    {
        this.playerName = playerName;
        this.playerID = playerID;
        this.playerStats = new PlayerStats(stats);
    }

    /**
     * Stuurt de naam van de speler terug
     * @return Naam van de speler
     */
    public String getPlayerName()
    {
        return this.playerName;
    }

    /**
     * Stuurt het ID van de speler terug
     * @return ID van de speler
     */
    public int getPlayerID()
    {
        return this.playerID;
    }

    /**
     * Stuurt de stats van de speler terug
     * @return Stats van de speler
     */
    public PlayerStats getPlayerStats()
    {
        return this.playerStats;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "playerstats=" + this.playerStats.toString() +
                ",name=" + this.playerName.replaceAll(" ", "_") +
                ",id=" + this.playerID +
                "}";
    }
}
