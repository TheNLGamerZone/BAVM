package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;

import java.util.ArrayList;

public class TeamManager<T extends Manageable> extends Manager<T>
{
    public Team marketTeam;

    public TeamManager(boolean generateTeams)
    {
        super();

        if (generateTeams)
        {
            this.generateManageables();
        }

        // Spelers laden
        this.loadManageables();
    }

    public Team getTeam(int teamID)
    {
        T team = super.getManageable(teamID);

        return team == null ? null : (Team) team;
    }

    public ArrayList<T> getLoadedTeams()
    {
        return manageables;
    }

    @Override
    public void loadManageables()
    {

    }

    @Override
    public void saveManageables()
    {

    }

    @Override
    public void generateManageables()
    {
        for (int i = 0; i < 20; i++)
        {
            double teamTalent = Math.random();
            String name = RandomNames.getTeamName();
            int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, teamTalent);

            manageables.add((T) new Team(name, i, playerIDs, i, teamTalent));
        }

        marketTeam = new Team("marketTeam", -1, BAVM.getPlayerManager().getFreePlayers(this), -1, 0.0);
        BAVM.getDisplay().appendText(20 + " teams gegenereerd!");
    }
}
