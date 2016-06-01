package me.nlt.bavm.teams.coach;

import me.nlt.bavm.teams.Manageable;

public class Coach implements Manageable
{
    private String coachName;
    private int coachID;

    private CoachStats coachStats;

    /**
     * Coach constructor
     *
     * @param coachName CoachName
     * @param coachID   CoachID
     * @param stats     Stats
     */
    public Coach(String coachName, int coachID, double[] stats)
    {
        this.coachName = coachName;
        this.coachID = coachID;
        this.coachStats = new CoachStats(stats);
    }

    /**
     * Stuurt de naam van de speler terug
     *
     * @return Naam van de speler
     */
    public String getCoachName()
    {
        return this.coachName;
    }

    /**
     * Stuurt het ID van de speler terug
     *
     * @return ID van de speler
     */
    public int getCoachID()
    {
        return this.coachID;
    }

    /**
     * Stuurt de stats van de speler terug
     *
     * @return Stats van de speler
     */
    public CoachStats getCoachStats()
    {
        return this.coachStats;
    }

    /**
     * Maakt en antwoord een string met alle data voor coaches
     *
     * @return De string met alle data
     */
    @Override
    public String toString()
    {
        return "Coach{" +
                "name=" + this.coachName.replaceAll(" ", "_") +
                ",id=" + this.coachID +
                ",coachstats=" + this.coachStats.toString() +
                "}";
    }

    @Override
    public int getID()
    {
        return this.coachID;
    }
}
