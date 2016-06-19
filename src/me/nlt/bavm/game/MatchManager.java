package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Factory;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.exceptions.FactoryException;

public class MatchManager<T extends Manageable> extends Manager<T>
{
    public MatchManager(boolean firstStart)
    {
        super();

        this.loadManageables();
    }

    @Override
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("matches");


        for (int i = 0; i < amount; i++)
        {
            try
            {
                manageables.add((T) Factory.createMatch(BAVM.getFileManager().readData("match", i)));
            } catch (FactoryException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }
    }

    @Override
    public void saveManageables(boolean firstSave)
    {
        if (!BAVM.getFileManager().firstStart)
        {
            BAVM.getDisplay().appendText("    -> Wedstrijden aan het opslaan ...");
        }

        int counter = 0;

        for (T type : manageables)
        {
            Match match = (Match) type;

            if ((firstSave || match.unsavedChanges()))
            {
                BAVM.getFileManager().writeData("match", match.toString(), match.getID());
                counter++;
            }
        }

        System.out.println((counter == 0 ? "Geen" : counter) + " veranderingen in wedstrijden opgeslagen!");
    }

    @Override
    public void generateManageables()
    {
    }

    public int simulateMatch(int homeID, int visitorID)
    {
        Game game = new Game(homeID, visitorID);
        String matchName = BAVM.getTeamManager().getTeam(homeID).getTeamName() + " - " + BAVM.getTeamManager().getTeam(visitorID).getTeamName();
        int matchID = getNextAvailableID();
        Match match = new Match(matchName, matchID, homeID, visitorID, game.getGameResult(), game.getGameLog());

        manageables.add((T) match);
        match.unsavedChanges = true;

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
