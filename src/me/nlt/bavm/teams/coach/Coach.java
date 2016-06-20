package me.nlt.bavm.teams.coach;

import me.nlt.bavm.teams.Manageable;

public class Coach implements Manageable
{
    private String coachName;
    private int coachID;

    private CoachStats coachStats;

    public boolean unsavedChanges;

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
        this.coachStats = new CoachStats(coachID, stats);
        this.unsavedChanges = false;
    }

    /**
     * Stuurt de naam van de coach terug
     *
     * @return Naam van de coach
     */
    public String getCoachName()
    {
        return this.coachName;
    }

    /**
     * Stuurt het ID van de coach terug
     *
     * @return ID van de coach
     */
    public int getCoachID()
    {
        return this.coachID;
    }

    /**
     * Stuurt de stats van de coach terug
     *
     * @return Stats van de coach
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
    /**
     * Stuurt het ID van de coach terug
     *
     * @return ID van de coach
     */
    public int getID()
    {
        return this.coachID;
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
