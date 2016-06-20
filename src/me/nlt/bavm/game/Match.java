package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;

import java.util.ArrayList;

public class Match implements Manageable
{
    private String matchName;
    private int matchID;
    private int[] matchGoals;
    private int[] teamIDs = new int[2];
    private ArrayList<String> matchLog = new ArrayList<>();

    public boolean unsavedChanges;

    /**
     * Match constructor
     *
     * @param matchName   Naam van de match
     * @param matchID     ID van de match
     * @param homeID      ID van het team dat thuis speelt
     * @param visitorID   ID van het team dat uit speelt
     * @param matchResult Resultaat van de match
     * @param matchLog    Logs van de match
     */
    public Match(String matchName, int matchID, int homeID, int visitorID, int[] matchResult, ArrayList<String> matchLog)
    {
        this.matchName = matchName;
        this.matchID = matchID;
        this.matchGoals = matchResult;
        this.teamIDs[0] = homeID;
        this.teamIDs[1] = visitorID;
        this.matchLog = matchLog;
        this.unsavedChanges = false;
    }

    /**
     * Returnt de naam van de match
     *
     * @return Naam van de match
     */
    public String getMatchName()
    {
        return matchName;
    }

    /**
     * Returnt het ID van de match
     *
     * @return ID van de match
     */
    public int getMatchID()
    {
        return matchID;
    }

    /**
     * Returnt de gemaakte goals
     *
     * @return Gemaakte goals
     */
    public int[] getMatchGoals()
    {
        return matchGoals;
    }

    /**
     * Return de matchlogs
     *
     * @return De matchlogs
     */
    public ArrayList<String> getMatchLog()
    {
        return matchLog;
    }

    /**
     * Verwijderd de logs uit het geheugen
     */
    public void clearMatchLog()
    {
        this.matchLog.clear();
    }

    /**
     * Laadt de logs uit het databestand
     */
    public void loadLogs()
    {
        this.matchLog.addAll(BAVM.getFileManager().getMatchLog(matchID));
    }


    @Override
    /**
     * Returnt het ID van de match
     * @return ID van de match
     */
    public int getID()
    {
        return this.matchID;
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

    @Override
    /**
     * Maakt en antwoord een string met alle data voor coaches
     *
     * @return De string met alle data
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (String logMessage : matchLog)
        {
            stringBuilder.append(logMessage.replaceAll(" ", "_").replaceAll(",", "%").replaceAll("=", "~") + "@");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);

        return "Match{" +
                "id=" + getID() +
                ",teams=" + teamIDs[0] + ":" + teamIDs[1] +
                ",score=" + matchGoals[0] + ":" + matchGoals[1] +
                ",logs=" + stringBuilder.toString() +
                "}";
    }
}
