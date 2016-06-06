package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;

public class MatchManager<T extends Manageable> extends Manager<T>
{
    public MatchManager(boolean firstStart)
    {
        super();

        //TODO: Deze nog opslaan
        this.loadManageables();
    }

    @Override
    public void loadManageables()
    {

    }

    @Override
    public void saveManageables(boolean firstSave)
    {

    }

    @Override
    public void generateManageables()
    {}

    public int simulateMatch(int homeID, int visitorID)
    {
        //TODO let game simulate an actual game with actual coefficients
        int matchResult[] = Game.simulateGame(homeID, visitorID);

        String matchName = BAVM.getTeamManager().getTeam(homeID).getTeamName() + "-" + BAVM.getTeamManager().getTeam(visitorID).getTeamName();

        int matchID = getNextAvailableID();

        manageables.add((T) new Match(matchName, matchID, homeID, visitorID, matchResult));

        return matchID;
    }

    public Match getMatch(int matchID)
    {
        T match = super.getManageable(matchID);

        return match == null ? null : (Match) match;
    }

    public int getNextAvailableID()
    {
        return manageables.size();
    }
}
