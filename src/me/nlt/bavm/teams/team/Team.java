package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;

public class Team implements Manageable
{
    private String teamName;
    private String directorName;
    private int teamID;

    private TeamInfo teamInfo;

    public boolean unsavedChanges;

    /**
     * Team constructor
     *
     * @param teamName     Naam van het team
     * @param directorName Naam van de directeur
     * @param teamID       ID van het team
     * @param playerIDs    ID's van de spelers in het team
     * @param coachID      ID van de coach
     * @param teamTalent   Teamtalent
     * @param currentGeld  Huidige hoeveelheid geld
     * @param weeklyIncome Wekelijkse inkomen
     * @param placement    Opstelling
     * @param scores       Scores
     */
    public Team(String teamName, String directorName, int teamID, int[] playerIDs, int coachID, double teamTalent, int currentGeld, int weeklyIncome, String placement, String scores)
    {
        this.teamName = teamName;
        this.directorName = directorName;
        this.teamID = teamID;
        this.teamInfo = new TeamInfo(this, playerIDs, coachID, teamTalent, currentGeld, weeklyIncome, placement, scores);

        if (!teamName.equals("marketTeam") && teamID != 19 && playerIDs.length != 21)
        {
            BAVM.getDisplay().appendText("Invalid team: " + teamID + " (" + playerIDs.length + " players)");
        }

        this.unsavedChanges = false;
    }

    /**
     * Returnt de naam van het team
     *
     * @return De naam
     */
    public String getTeamName()
    {
        return this.teamName;
    }

    /**
     * Returnt de naam van de directeur
     *
     * @return De naam
     */
    public String getDirectorName()
    {
        return this.directorName;
    }

    /**
     * Returnt de TeamInfo van dit team
     *
     * @return De TeamInfo van dit team
     */
    public TeamInfo getTeamInfo()
    {
        return this.teamInfo;
    }

    /**
     * Methode om de naam van de directeur aan te passen
     *
     * @param directorName Nieuwe naam
     */
    public void setDirectorName(String directorName)
    {
        this.directorName = directorName;
    }

    /**
     * Methode om de naam van het team aan te passen
     *
     * @param teamName Nieuwe naam
     */
    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    @Override
    /**
     * Maakt en antwoord een string met alle data voor het team
     *
     * @return De string met alle data
     */
    public String toString()
    {
        return "Team{" +
                "name=" + this.teamName.replaceAll(" ", "_") +
                ",id=" + this.teamID +
                ",info=" + this.teamInfo.toString() +
                ",directorName=" + this.directorName.replaceAll(" ", "_") +
                "}";
    }

    @Override
    /**
     * Stuurt het ID van het team terug
     *
     * @return ID van het team
     */
    public int getID()
    {
        return this.teamID;
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