package me.nlt.bavm.teams.player;

import me.nlt.bavm.teams.Manageable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Player implements Manageable
{
    private String playerName;
    private int playerID;
    private Position position;
    private double marketValue;

    private PlayerStats playerStats;

    public boolean unsavedChanges;

    /**
     * Player constructor
     *
     * @param playerName PlayerName
     * @param playerID   PlayerID
     * @param stats      Stats
     */
    public Player(String playerName, int playerID, Position position, double[] stats)
    {
        this.playerName = playerName;
        this.playerID = playerID;
        this.position = position;
        this.playerStats = new PlayerStats(playerID, stats);

        this.marketValue = new BigDecimal(Math.random() * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.unsavedChanges = false;
    }

    /**
     * Stuurt de naam van de speler terug
     *
     * @return Naam van de speler
     */
    public String getPlayerName()
    {
        return this.playerName;
    }

    /**
     * Stuurt het ID van de speler terug
     *
     * @return ID van de speler
     */
    public int getPlayerID()
    {
        return this.playerID;
    }

    /**
     * Stuurt de aangerade positie van de speler terug
     *
     * @return Positie van de speler
     */
    public Position getPosition()
    {
        return this.position;
    }

    /**
     * Stuurt de stats van de speler terug
     *
     * @return Stats van de speler
     */
    public PlayerStats getPlayerStats()
    {
        return this.playerStats;
    }

    /**
     * Returnt de marktwaarde van de speler
     *
     * @return Marktwaarde
     */
    public double getMarketValue()
    {
        return this.marketValue;
    }

    /**
     * Methode om de martktwaarde van een speler aan te passen
     *
     * @param newValue De nieuwe marktwaarde
     */
    public void setMarketValue(double newValue)
    {
        this.marketValue = newValue;
    }

    /**
     * Maakt en antwoord een string met alle data voor de speler
     *
     * @return De string met alle data
     */
    @Override
    public String toString()
    {
        return "Player{" +
                "name=" + this.playerName.replaceAll(" ", "_") +
                ",id=" + this.playerID +
                ",position=" + this.position.name() +
                ",playerstats=" + this.playerStats.toString() +
                "}";
    }

    @Override
    /**
     * Stuurt het ID van de speler terug
     *
     * @return ID van de speler
     */
    public int getID()
    {
        return this.playerID;
    }

    @Override
    /**
     * Stuurt een boolean terug die aangeeft of er dingen moeten worden opgeslagen
     *
     * @return Boolean voor opslaan
     */
    public boolean unsavedChanges()
    {
        return unsavedChanges;
    }
}
