package me.nlt.bavm.season;


public class PlannedMatch
{
    private String matchName;
    private int matchID;
    private int[] teamIDs = new int[2];

    /**
     * PlannedMatch heeft alle data nodig om een Game te simuleren
     *
     * @param matchName  Naam van de match
     * @param seasonHalf Seizoenstijd
     * @param matchID    ID van de match
     */
    public PlannedMatch(String matchName, int seasonHalf, int matchID)
    {
        this.matchName = matchName;
        this.matchID = matchID;

        String[] splitName = matchName.split("-");
        if (seasonHalf == 1)
        {
            teamIDs[0] = Integer.parseInt(splitName[0]);
            teamIDs[1] = Integer.parseInt(splitName[1]);
        } else
        {
            teamIDs[1] = Integer.parseInt(splitName[0]);
            teamIDs[0] = Integer.parseInt(splitName[1]);
        }

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
     * Returnt de ID's van de spelende teams
     *
     * @return ID's van de spelende teams
     */
    public int[] getTeamIDs()
    {
        return teamIDs;
    }
}
