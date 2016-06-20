package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Factory;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.exceptions.FactoryException;

public class MatchManager<T extends Manageable> extends Manager<T>
{
    public boolean dataLoaded = false;

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
            	Match match = Factory.createMatch(BAVM.getFileManager().readData("match", i));
            	
            	if (match == null)
            	{
            		continue;
            	}
            	
                manageables.add((T) match);
                match.clearMatchLog();
                
                if (i%30 == 0)
                {
                    BAVM.getDisplay().clearText();
                    BAVM.getDisplay().appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked", "Spelers, teams, coaches en wedstrijden worden geladen", "  Alle spelers geladen", "  Alle coaches geladen", "  Alle teams geladen", "  " + i + " wedstrijden geladen ...");
                }
            } catch (FactoryException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }

        System.out.println("Alle wedstrijden geladen");
        dataLoaded = true;
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
            	match.loadLogs();
                BAVM.getFileManager().writeData("match", match.toString(), match.getID());
                
                match.unsavedChanges = false;
                counter++;
            }
        }

        System.out.println((counter == 0 ? "Geen" : counter) + " veranderingen in wedstrijden opgeslagen!");
    }

    @Override
    public void generateManageables()
    {
    }

    /*
     * Hier wordt een nieuw Match object gemaakt en de data daarvoor wordt genereerd uit een Game object
     */

    public int simulateMatch(int homeID, int visitorID)
    {
        Game game = new Game(homeID, visitorID);
        String matchName = BAVM.getTeamManager().getTeam(homeID).getTeamName() + " - " + BAVM.getTeamManager().getTeam(visitorID).getTeamName();
        int matchID = getNextAvailableID();
        Match match = new Match(matchName, matchID, homeID, visitorID, game.getGameResult(), game.getGameLog());

        manageables.add((T) match);
        match.unsavedChanges = true;
        
        BAVM.getFileManager().writeData("match", match.toString(), matchID);
        match.clearMatchLog();
        
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
