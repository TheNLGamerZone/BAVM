package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;

public class TeamManager<T extends Manageable> extends Manager<T>
{
    public Team marketTeam;
    public Team playerTeam;
    
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
    	int teams = 20;
    	
        for (int i = 0; i < teams; i++)
        {
            double teamTalent = Math.random();
            String name = RandomNames.getTeamName();
            int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, teamTalent);

            manageables.add((T) new Team(name, i, playerIDs, i, teamTalent, 1000000, BAVM.getPlayerManager().getPlacementString(playerIDs)));
        }

        int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, 0.457);

        marketTeam = new Team("marketTeam", -666, BAVM.getPlayerManager().getFreePlayers(this), -1, 0.0, 234730247, "");
        playerTeam = new Team(RandomNames.getTeamName(), -1, playerIDs, teams, 0.457, 27500, BAVM.getPlayerManager().getPlacementString(playerIDs));
        
        BAVM.getDisplay().appendText(teams + " teams gegenereerd!");
    }
    
    public TransferResult transferPlayer(Team sendingTeam, Team receivingTeam, Player player, int price)
    {
        if (!sendingTeam.getTeamInfo().getPlayers().contains(player))
        {
            return TransferResult.FAILED_NOT_IN_TEAM;
        }

    	if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price && receivingTeam != this.playerTeam)
    	{
    		return TransferResult.FAILED_NO_MONEY_OTHER;
    	}
    	
    	if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price && receivingTeam == this.playerTeam)
    	{
    		return TransferResult.FAILED_NO_MONEY;
    	}
    	
    	if (sendingTeam.getTeamInfo().getPlayers().size() - 1 < 14)
    	{
    		return TransferResult.FAILED_NOT_ENOUGH_PLAYERS;
    	}
    	
    	sendingTeam.getTeamInfo().getPlayers().remove(player);
    	sendingTeam.getTeamInfo().getTeamGeld().removeGeld(price);
    	receivingTeam.getTeamInfo().getPlayers().add(player);
    	receivingTeam.getTeamInfo().getTeamGeld().addGeld(price);
    	
    	return TransferResult.SUCCESS;
    }
}
